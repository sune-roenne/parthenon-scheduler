package parthenon.scheduler.dal

import scala.slick.driver.H2Driver.simple._
import com.jolbox.bonecp.BoneCPDataSource
import parthenon.scheduler.config.Config
import scala.util.Try


object DalController {
  val dbConfig = Config.dal
  
  val ds = new BoneCPDataSource
  ds.setDriverClass("org.h2.Driver")
  ds.setJdbcUrl(dbConfig.url)
  ds.setUser(dbConfig.user)
  ds.setPassword(dbConfig.password)
  ds.setDefaultAutoCommit(true)

  val database = Database.forDataSource(ds)
  createTables
  
  def createTables {
    List(ExecutionDAO.executionTable).map(tab => Try(database.withSession{implicit session => tab.ddl.create}))
  }
  

}