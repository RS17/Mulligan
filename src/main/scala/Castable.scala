// this is probably totally unnecessary but it's a fun scala exercise
// It's a monad that allows you to cast without casting or something.  I imagine this will help somehow with apis
// but that may be silly
trait Castable[+A] {
  val value: A
  private val internal: A = value
  // runs the function on the internal value and returns the result
  def flatMap[B](f: (=>A) => B): B = f(get)
  // does the same but puts it back in the Monad at the end
  //  def map[B](f: A=>B): Castable[B] = flatMap(x=>new Castable[B](f(x)))
  def get = internal.asInstanceOf[A]
  def origClass: Class[_ <: A] = value.getClass
}
