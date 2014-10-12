package parthenon.scheduler
import scala.reflect.runtime.universe
import scala.reflect.runtime
import scala.tools.reflect.ToolBox
import parthenon.scheduler.tools.IDGenerator
import scala.util.Try

object ScalaHandler extends IDGenerator {
  
  val ThisObjectName = this.getClass.getName.replaceAll("\\$","")
  val globalContextMap = scala.collection.mutable.Map.empty[Long, Map[String, (Any, String)]]
  
  

  def executeExpressionWithContext(expression: String, context: Map[String, (Any,String)], fixedPrefix : Option[String] = None) = {
    val executionID = nextID
    globalContextMap(executionID) = context
    val prefix = context.map {
      case (key,(value,typeString)) => "val " + key + " = " + ThisObjectName + ".globalContextMap(" + executionID + ")(\"" + key + "\")._1.asInstanceOf[" + typeString +  "]"
    }.mkString("\n")+ "\n" + fixedPrefix.getOrElse("") +"\n" 
    
    val mirror = universe.runtimeMirror(this.getClass.getClassLoader)
    val toolBox = mirror.mkToolBox()
    val completeExpression = prefix + "\n" + expression
    //println("Execution complete expression:\n" + completeExpression)
    val parsed = toolBox.parse(completeExpression)
    val res = Try(toolBox.eval(parsed))
    globalContextMap -= executionID
    res
  }
  
  def createContextForExecution(holidays : Set[java.sql.Date], bankdays : Set[java.sql.Date], today : java.sql.Date) = (expression : String) => {
    val context = Map(
        "holidays" -> (holidays, "Set[java.sql.Date]"),
        "bankdays" -> (bankdays, "Set[java.sql.Date]"),
        "today" -> (today, "java.sql.Date")
    )
    
    val prefix = """implicit def dateExtensions(date : java.sql.Date) = new {
      def isBankDay = {
        import org.apache.commons.lang3.time.DateUtils
        def setContainsDate(set : Set[java.sql.Date]) = set.find(inSet => DateUtils.truncatedEquals(inSet, date, java.util.Calendar.DATE)) != None
        if(setContainsDate(bankdays)) true
        else if(setContainsDate(holidays)) false
        else {
          val cal = new java.util.GregorianCalendar
          cal.setTimeInMillis(date.getTime)
          !Set(java.util.Calendar.SATURDAY, java.util.Calendar.SUNDAY).contains(cal.get(java.util.Calendar.DAY_OF_WEEK))
        }
      }
    }"""
      executeExpressionWithContext(expression, context, Some(prefix))
      
      
      
    
    
  }
  

}