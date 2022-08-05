// why not abstract?  Because making it abstract means you can't test the class itself.
// Is having a non-abstract class that's used entirely as an abstract class an anti-pattern?  Probably.  Do I care?  No.
import cats.effect.IO
import org.http4s.circe._
import io.circe.generic.auto._

trait DefaultObject extends Serializable with Ordered[DefaultObject]{
  /**
   * This tests the object.
   */
  def testMe: Boolean = {
    true
  }

  /**
   * This is a default object used for testing.  E.g. A is a defaultObject and A.B is a default object.  A's test()
   * method will use the selfTest version of B by default
   */
  def constructSample: DefaultObject = DefaultObject
  // not clear what payload is - is this (column -> value)?  Or does it contain multiple rows?
  def save(db: Option[DefaultDB]): Unit = {}

  override def equals(obj: Any): Boolean = obj match {
    case defObj: DefaultObject => this.compare(defObj) == 0
    case _ => false
  }
  override def compare(that: DefaultObject) = (this.getClass.getCanonicalName + this.toString) compare (that.getClass.getCanonicalName + that.toString)
}
object DefaultObject extends DefaultObject {
  def load(db: DefaultDB, key: DefaultElement[Any]): Option[DefaultObject] = {
    db.read(key)
  }

  def main(args: Array[String]): Unit = {
    println("main not implemented")
  }

}

