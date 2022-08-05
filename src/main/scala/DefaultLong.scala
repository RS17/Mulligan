
case class DefaultLong(v: Long) extends DefaultElement[Long] with Ordered[DefaultObject]{
  override val value = v
  override def testMe: Boolean = {
    constructSample.isInstanceOf[DefaultElement[Long]] &&
    DefaultLong(2L) == (DefaultLong(2L)) &&
    DefaultLong(2L) != (DefaultLong(1L))
  }

  override def constructSample = new DefaultLong(1L)
  override def toString: String = value.toString
  /*override def compare(that: DefaultObject) = that match {
    case DefaultLong(thatLong) => this.value compare thatLong
    case _ => super.compare(that)
  }*/
}

object DefaultLong {
  def apply(x: Long): DefaultLong = {
    new DefaultLong(x)
  }

  def apply(): DefaultLong = {
    new DefaultLong(-1L)
  }

  def unapply(arg: DefaultLong): Option[Long] = Some(arg.value)
}
