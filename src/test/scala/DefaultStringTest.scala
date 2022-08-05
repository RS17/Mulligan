import org.scalatest.flatspec.AnyFlatSpec

class DefaultStringTest extends AnyFlatSpec{
  "this test" should "return true" in {
    assert(DefaultString().testMe)
  }
}