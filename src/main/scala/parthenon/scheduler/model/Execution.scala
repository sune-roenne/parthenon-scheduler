package parthenon.scheduler.model

import java.sql.Time

case class Execution (
    batchGroupName : String,
    timeOfDay : Time,
    outputFileName : String,
    xmlPrefix : List[String],
    batchGroupElementName : String,
    batchDateElementName : String,
    executionDayCondition : Option[String],
    dateFormat : String,
    outputFileEncoding : String
)