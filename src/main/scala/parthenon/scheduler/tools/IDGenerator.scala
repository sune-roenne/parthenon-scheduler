package parthenon.scheduler.tools

trait IDGenerator { self => 
  var currentID = 0L
  def nextID = self.synchronized{currentID += 1L; currentID}
}