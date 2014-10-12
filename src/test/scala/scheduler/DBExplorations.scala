package scheduler

import parthenon.scheduler.dal.ExecutionDAO

object DBExplorations {

  def main(args: Array[String]): Unit = {
    println(ExecutionDAO.executions)
    ExecutionDAO.insertExecution("BJG_TESSA", System.currentTimeMillis)
    ExecutionDAO.insertExecution("BJG_TESSA", System.currentTimeMillis)
    ExecutionDAO.insertExecution("BJG_TESSA1", System.currentTimeMillis)
    ExecutionDAO.insertExecution("BJG_TESSA2", System.currentTimeMillis)
    ExecutionDAO.insertExecution("BJG_TESSA3", System.currentTimeMillis)
    println("BJG_TESSA")
    ExecutionDAO.executionsFor("BJG_TESSA").foreach(println)
    println("BJG_TESSA1")
    ExecutionDAO.executionsFor("BJG_TESSA1").foreach(println)
    println("BJG_TESSA2")
    ExecutionDAO.executionsFor("BJG_TESSA2").foreach(println)
    println("BJG_TESSA4")
    ExecutionDAO.executionsFor("BJG_TESSA4").foreach(println)
    
    
  }

}