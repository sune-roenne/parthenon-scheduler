package parthenon.scheduler.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.{Config => TypesafeConfig}
import scala.collection.JavaConversions._
import parthenon.scheduler.model.Execution
import java.text.SimpleDateFormat
import java.sql.Time
import parthenon.scheduler.model.DateQuery

object Config {
  
  def conf = ConfigFactory.load
  val timeFormat = new SimpleDateFormat("HH:mm")
  
  private implicit def confConv(config : TypesafeConfig) = new {
    def >[A](confName : String)(func : TypesafeConfig => A ) = if (!conf.hasPath(confName)) None else Some(func(conf.getConfig(confName)))
    def >![A](confName : String)(func : TypesafeConfig => A ) = func(conf.getConfig(confName))
    def >>[A](confName : String)(func : TypesafeConfig => A ) = if (!conf.hasPath(confName)) List.empty else conf.getConfigList(confName).map(func).toList
    def ?(confName : String) = if(!config.hasPath(confName)) None else Some(config.getString(confName))
    
  }
  
  def connections = (conf >>("connections"))(connConf => new {
        val name = connConf.getString("name")
        val driverClass = connConf.getString("driver-class")
        val url = connConf.getString("url")
        val user = connConf.getString("user")
        val password = connConf.getString("password")
  }) 
  
  def specialDayUpdate = (conf ? "special-days-update-frequency").map(_.toInt)
  
  def holidays = (conf > "holidays")(holConf => DateQuery(
    holConf.getString("connection"),
    holConf.getString("query")
  ))
  
  def irregularBankDays = (conf > "irregular-bank-days")(bdConf => DateQuery(
    bdConf.getString("connection"),
    bdConf.getString("query")
  ))
  
  def executions = (conf >> "executions")(execConf => Execution(
    execConf.getString("batch-group-name"),
    new Time(timeFormat.parse(execConf.getString("time-of-day")).getTime),
    execConf.getString("output-file-name"),
    execConf.getStringList("xml-prefix").toList,
    execConf.getString("group-element-name"),
    execConf.getString("batch-date-element-name"),
    (execConf ? "execution-day-condition"),
    execConf.getString("date-format"),
    execConf.getString("output-file-encoding"),
    execConf.getString("batch-date-expression")
  ))
  
  def dal = (conf >! "persistence")(dalConf => new {
    val url = dalConf.getString("url")
    val user = dalConf.getString("user")
    val password = dalConf.getString("password")
  })
  
    
  

}