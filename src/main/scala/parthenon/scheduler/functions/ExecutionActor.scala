package parthenon.scheduler.functions

import akka.actor.Actor
import parthenon.scheduler.model.Execution
import org.slf4j.LoggerFactory
import parthenon.scheduler.dal.ExecutionDAO
import java.sql.Date
import org.apache.commons.lang3.time.DateUtils
import java.util.Calendar
import java.util.GregorianCalendar
import scala.concurrent.duration._
import parthenon.scheduler.ScalaHandler
import parthenon.scheduler.Globals
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import parthenon.scheduler.XMLHandler
import java.text.SimpleDateFormat
import java.io.File
import org.apache.commons.io.IOUtils
import java.util.logging.XMLFormatter
import java.io.FileOutputStream

class ExecutionActor(execution: Execution) extends Actor {
  import ExecutionActor._

  private val logger = LoggerFactory.getLogger(this.getClass)
  private val shortDelay = 2 minutes
  private val longDelay = 1 hours

  override def receive = {
    case FollowUpOnExecution => {
      logger.debug("Following up on Execution: " + execution)
      Try(executeOrReschedule)
    }
  }

  private def executeOrReschedule = {
    val allExecutions = ExecutionDAO.executionsFor(execution.batchGroupName).map(p => new Date(p._2.getTime))
    val today = new Date(System.currentTimeMillis())
    // Already executed today
    if (allExecutions.exists(date => DateUtils.truncatedEquals(date, today, Calendar.DATE))) {
      logger.debug("Execution of: " + execution.batchGroupName + " has already occured today")
      rescheduleForTomorrow
    } else {
      // Continue based on execution condition
      val continue = execution.executionDayCondition match {
        case Some(cond) => {
          (Globals.holidays, Globals.bankdays) match {
            case (Some(hDays), Some(bDays)) => {
              val context = ScalaHandler.createContextForExecution(hDays, bDays, today)
              Try(context(cond).asInstanceOf[Boolean]) match {
                case Success(true) => true
                case Success(false) => {rescheduleForTomorrow; false}
                case Failure(err) => {
                  logger.error("Error occured while executing condition: " + execution.executionDayCondition)
                  logger.debug("Error details", err)
                  reschedule(longDelay.toMillis)
                  false
                }
              }
            }
            case _ => {
              logger.debug("Bankdays and/or holidays are not yet loaded. Scheduling short delay")
              reschedule(shortDelay.toMillis)
              false
            }
          }
          
        }
        case None => true
      }
      if(continue) {
        val earliestCal = new GregorianCalendar
        setTime(earliestCal)
        val currentTime = System.currentTimeMillis
        if(currentTime > earliestCal.getTimeInMillis) execute
        else {
          reschedule(earliestCal.getTimeInMillis - currentTime + 10000)
        }
      }
    }
  }
  
  private def setTime(cal: Calendar) {
    cal.set(Calendar.HOUR_OF_DAY, execution.timeOfDay.getHours)
    cal.set(Calendar.MINUTE, execution.timeOfDay.getMinutes)
    cal.set(Calendar.SECOND, execution.timeOfDay.getSeconds)
  }
  

  private def rescheduleForTomorrow {
    val nextCheck = new GregorianCalendar
    nextCheck.add(Calendar.DAY_OF_YEAR, 1)
    setTime(nextCheck)
    reschedule(nextCheck.getTimeInMillis - System.currentTimeMillis)
  }

  private def reschedule(delayInMillis: Long) {
    implicit val executor = context.system.dispatcher
    context.system.scheduler.scheduleOnce(delayInMillis millis, self, FollowUpOnExecution)
  }
  
  private def execute {
    val context = ScalaHandler.createContextForExecution(Globals.holidays.getOrElse(Set.empty) , Globals.holidays.getOrElse(Set.empty), new java.sql.Date(System.currentTimeMillis))
    val res = for(
        execRes <- Try(context(execution.batchDateExpression).asInstanceOf[Date]);
        formattedDate <- Try{val form = new SimpleDateFormat(execution.dateFormat); form.format(execRes)}) yield formattedDate
    res match {
      case Failure(err) => {
        logger.error("Execution of date expression failed: "+ execution.batchDateExpression, err)
        reschedule(longDelay.toMillis)
      }
      case Success(formattedDate) => {
        val elem = XMLHandler.buildXML(execution.xmlPrefix , execution.batchGroupElementName  -> execution.batchGroupName , execution.batchDateElementName -> formattedDate)
        val encoding = execution.outputFileEncoding
        val xmlFormatter = new scala.xml.PrettyPrinter(100,2)
        val bytes = xmlFormatter.format(elem).getBytes(encoding)
        val outputFile = new File(execution.outputFileName)
        if(!outputFile.getParentFile.exists)
          outputFile.getParentFile.mkdirs
        if(outputFile.exists) outputFile.delete()
        outputFile.createNewFile()
        val os = new FileOutputStream(outputFile)
        IOUtils.write(bytes, os)
        os.close
        logger.info("Wrote: " + elem + " to file: "+ outputFile.getAbsolutePath)
        rescheduleForTomorrow
      }
    }    
  }

}

object ExecutionActor {
  case object FollowUpOnExecution
}