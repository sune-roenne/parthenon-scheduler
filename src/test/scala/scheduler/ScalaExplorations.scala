package scheduler

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import parthenon.scheduler.ScalaHandler
import java.util.Calendar

object ScalaExplorations {

  val dateFormat = new SimpleDateFormat("dd-MM-yyyy")
  def d(dateString: String) = new Date(dateFormat.parse(dateString).getTime)
  def d(time: Long) = new Date(time)

  def main(args: Array[String]): Unit = {
    val holidays = Set(d("12-10-2014"), d("13-10-2014")) ++ generateDays("01-01-2001", "31-12-2013")
    val bankdays = Set(d("19-10-2014"), d("20-10-2014")) ++ generateDays("01-01-2001", "31-12-2013")
    val expression = "today isBankDay"
    for (
      dayToTry <- List(
        d("12-10-2014"),
        d("13-10-2014"),
        d("14-10-2014"),
        d("18-10-2014"),
        d("19-10-2014"),
        d("20-10-2014"),
        d("21-10-2014"))
    ) {
      val context = ScalaHandler.createContextForExecution(holidays, bankdays, dayToTry)
      //for(i <- 0 until 100) 
      println(dayToTry + " -> " + context(expression))
    }

  }
  
  def generateDays(from : String, to : String) = {
    val fromCal = new GregorianCalendar
    fromCal.setTimeInMillis(d(from).getTime)
    val toCal = new GregorianCalendar
    toCal.setTimeInMillis(d(to).getTime)
    val ret = scala.collection.mutable.ArrayBuffer.empty[java.sql.Date]
    while(fromCal.getTimeInMillis() < toCal.getTimeInMillis()) {
      ret += new java.sql.Date(fromCal.getTimeInMillis())
      fromCal.add(Calendar.DAY_OF_YEAR, 1)
    }
    ret.toList
    
  }
  

}