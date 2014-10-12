package parthenon.scheduler.model

case class Connection(
   name : String,
   driverClass : String,
   url : String,
   user : String,
   password : String
)