package parthenon.scheduler.dal

import scala.slick.driver.H2Driver.simple._
import java.sql.Timestamp
import scala.util.Try
import scala.slick.lifted.AbstractTable

object ExecutionDAO {

  class ExecutionTable(tag: Tag) extends Table[(String, Timestamp)](tag, "Executions") {
    def batchGroup = column[String]("BatchGroup", O.NotNull)
    def executionTime = column[Timestamp]("ExecutionTime", O.NotNull)
    def * = (batchGroup, executionTime)
  }

  val executionTable = TableQuery[ExecutionTable]

  def executionsFor(batchGroup: String) = DalController.database.withSession { implicit session =>
    {
      val q = for (exec <- executionTable if exec.batchGroup === batchGroup) yield exec
      q.list
    }
  }
  
  def executions = DalController.database.withSession { implicit session =>
    {
      val q = for (exec <- executionTable) yield exec
      q.list
    }
  }


  def insertExecution(batchGroup: String, executionTime: Long) = DalController.database.withSession { implicit session =>
    {
      executionTable += (batchGroup, new Timestamp(executionTime))
    }
  }

}