package scheduler

import parthenon.scheduler.XMLHandler

object XMLExplorations {

  def main(args: Array[String]): Unit = {
    val result = XMLHandler.buildXML(List("Data", "Level1", "Level2","Level3"), "BatchGroup" -> "TestName", "BatchDate" -> "31-05-2014")
    println(result)
    
    
  }

}