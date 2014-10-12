package scheduler

import parthenon.scheduler.config.Config

object ConfigExplorations {

  def main(args: Array[String]): Unit = {
    Config.connections.foreach(conn => println(conn.driverClass + ", " + conn.url + ",  " +conn.name + ", " + conn.password))
    Config.executions foreach println
    
  }

}