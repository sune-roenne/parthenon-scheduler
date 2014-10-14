package parthenon.scheduler

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import parthenon.scheduler.config.Config
import parthenon.scheduler.functions.ExecutionActor
import akka.actor.Props
import parthenon.scheduler.functions.SpecialDayActor
import scala.concurrent.duration._

object Launcher {

  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load
    val system = ActorSystem("SchedulerSystem", conf)
    implicit val executor = system.dispatcher
    val specialDayActor = system.actorOf(Props(new SpecialDayActor))
    val specDayUpdateInterval = Config.specialDayUpdate.getOrElse(60*24)
    system.scheduler.schedule(5 seconds, specDayUpdateInterval minutes, specialDayActor, SpecialDayActor.FollowUpOnSpecialDays)
    val executions = Config.executions
    executions.foreach {
      case execution => {
        system.actorOf(Props(new ExecutionActor(execution)))
      }
    }
    
    
  }

}