package parthenon.scheduler

import java.sql.Date
import scala.xml.Elem
import scala.xml.NodeSeq

object XMLHandler {
  
  
  def buildXML(prefix : List[String], batchGroupName : (String,String), batchDate : (String,String)) = {
    def buildRec(prefList : List[String]) : NodeSeq = prefList match {
      case Nil => <a>{batchGroupName._2}</a>.copy(label = batchGroupName._1) ++ <a>{batchDate._2}</a>.copy(label = batchDate._1)
      case car :: cldr => <a>{buildRec(cldr)}</a>.copy(label = car)
    }
    val elemName :: sequence = prefix
    <a>{buildRec(sequence)}</a>.copy(label = elemName)
  }
  

}