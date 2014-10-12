package parthenon.scheduler.functions

import akka.actor.Actor
import org.slf4j.LoggerFactory
import parthenon.scheduler.config.Config
import parthenon.scheduler.model.DateQuery
import scala.util.Try
import java.sql.DriverManager
import parthenon.scheduler.Globals
import scala.util.Success
import scala.util.Failure

class SpecialDayActor extends Actor {
  import SpecialDayActor._
  
  private val logger = LoggerFactory.getLogger(this.getClass)
  val connections = Config.connections
  val hDays = Config.holidays
  val bDays = Config.irregularBankDays
  
  override def receive = {
    case FollowUpOnSpecialDays => {
      hDays match {
        case None => Globals.holidays = Some(Set.empty)
        case Some(days) => extractDays(days) match {
          case Success(s) => Globals.holidays = Some(s)
          case Failure(d) => {
            
          }
        }
      }
      
    }
  }
  
  def extractDays(query : DateQuery) = {
    Try{
      val connection = connections.find(_.name == query.connection).get
      val conn = DriverManager.getConnection(connection.url, connection.user, connection.password)
      val statement = conn.createStatement
      val rs = statement.executeQuery(query.query)
      val res = scala.collection.mutable.ArrayBuffer.empty[java.sql.Date]
      while(rs.next) res += rs.getDate(1)
      statement.close
      conn.close
      res.toSet
    }
  }

}

object SpecialDayActor {
  case object FollowUpOnSpecialDays
  
}