import org.scalatest.flatspec.AnyFlatSpec

class DefaultObjectTest extends AnyFlatSpec{
  "this test" should "return true" in {
    assert(DefaultObject.testMe)
  }
}