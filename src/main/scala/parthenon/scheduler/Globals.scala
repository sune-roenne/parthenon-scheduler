package parthenon.scheduler

import java.sql.Date

object Globals {
  
  var holidays : Option[Set[Date]] = None
  var bankdays : Option[Set[Date]] = None

}