package scheduler

import java.text.SimpleDateFormat
import java.sql.Time

object TimeExplorations {

  val timeFormat = new SimpleDateFormat("HH:mm")
  def t(str : String) = new Time(timeFormat.parse(str).getTime)
  def print(time : Time) = println(time + " -> " + time.getHours + ":" + time.getMinutes + ":" + time.getSeconds)
  
  def main(args: Array[String]): Unit = {
    print(t("00:01"))
    print(t("08:01"))
    print(t("12:01"))
    print(t("23:59"))

    
  }

}