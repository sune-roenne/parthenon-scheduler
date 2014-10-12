package parthenon.scheduler.functions

import akka.actor.Actor
import parthenon.scheduler.model.Execution
import org.slf4j.LoggerFactory
import parthenon.scheduler.dal.ExecutionDAO
import java.sql.Date

class ExecutionActor(execution : Execution) extends Actor {
  import ExecutionActor._
  
  private val logger = LoggerFactory.getLogger(this.getClass)
  
  override def receive = {
    case FollowUpOnExecution => {
      logger.debug("Following up on Execution: "+ execution)
      val allExecutions = ExecutionDAO.executionsFor(execution.batchGroupName).map(p => p._1 -> new Date(p._2.getTime))
      
      
      
    }
  }
  
  

}

object ExecutionActor {
  case object FollowUpOnExecution
}