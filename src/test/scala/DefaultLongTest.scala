import org.scalatest.flatspec.AnyFlatSpec

class DefaultLongTest extends AnyFlatSpec{
  "this test" should "return true" in {
    assert(DefaultLong().testMe)
  }
}