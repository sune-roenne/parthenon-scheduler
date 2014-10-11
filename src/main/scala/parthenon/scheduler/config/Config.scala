package parthenon.scheduler.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.{Config => TypesafeConfig}

import scala.collection.JavaConversions._

object Config {
  
  def conf = ConfigFactory.load
  
  private implicit def confConv(config : TypesafeConfig) = new {
    def >[A](confName : String)(func : TypesafeConfig => A ) = if (!conf.hasPath(confName)) None else Some(func(conf.getConfig(confName)))
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
  
  def holidays = (conf > "holidays")(holConf => new {
    val connectionName = holConf.getString("connection")
    val query = holConf.getString("query")
  })
  
  def irregularBankDays = (conf > "irregular-bank-days")(bdConf => new {
    val connectionName = bdConf.getString("connection")
    val query = bdConf.getString("query")
  })
  
  def executions = (conf >> "executions")(execConf => {
    val batchGroupName = execConf.getString("batch-group-name")
    val timeOfDay = execConf.getString("tome-of-day")
    val outputFileName = execConf.getString("output-file-name")
    val xmlPrefix = execConf.getStringList("xml-prefix")
    val batchGroupElementName = execConf.getString("group-name-element")
    val batchDateElementName = execConf.getString("batch-date-element-name")
    val executionDayCondition = (execConf ? "execution-day-condition")
  })
    

}