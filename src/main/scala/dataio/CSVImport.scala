package dataio

import api.DefaultAPI
import com.github.rs17.mulligan
import com.github.rs17.mulligan.{DefaultLong, DefaultMap, DefaultModule, DefaultObject, DefaultString, DefaultStruct}
import db.DefaultDB

import scala.io.Source
import scala.reflect.ClassTag

object CSVImporter extends DefaultModule{
  override lazy val db: Option[DefaultDB] = None
  override lazy val api: Option[DefaultAPI] = None

  /** Does the following:
   * 1. Pulls CSV data
   * 2. Tries to match with list of objects (objects should be ordered by priority)
   * 3. Outputs those objects as options
   * */
  def importCSVGreedy[T](filename: String, expectedObj: ImportMatcher[T]):List[Option[T]] = {
    val bufferedSource = Source.fromFile(filename)
    val results: List[Option[T]] = for (line <- bufferedSource.getLines.toList) yield {
      val cols = line.split(",").map(_.trim)
      cols match {
        case expectedObj(_) => Some(expectedObj.builder(cols))
        case _ => None
      }
    }
    bufferedSource.close
    results
  }

  def main(args: Array[String]): Unit = {
    object testMatch extends ImportMatcher[String]{
      override def matcher  = (strs: Array[String]) => strs.length > 2 && strs(2) == "Stocks"

      override def builder: Array[String] => String = (strs: Array[String]) => strs(0)
    }

    val results = importCSVGreedy("/home/ravi/Downloads/ibkrdata.csv", testMatch)
    for( result <- results) println("got result " + result)
  }

  trait ImportMatcher[T] extends DefaultObject {
    protected def matcher: Array[String] => Boolean
    def builder: Array[String] => T
    final def unapply(strs: Array[String]): Option[String] = {
      if(matcher.apply(strs)) Some(strs(0)) else None
    }
  }
}

