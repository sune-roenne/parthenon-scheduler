package parthenon.scheduler

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import parthenon.scheduler.config.Config
import parthenon.scheduler.functions.ExecutionActor
import akka.actor.Props

object Launcher {

  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load
    val system = ActorSystem("SchedulerSystem", conf)
    val executions = Config.executions
    executions.foreach {
      case execution => {
        system.actorOf(Props(new ExecutionActor(execution)))
      }
    }
    
    
  }

}