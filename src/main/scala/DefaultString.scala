package com.github.rs17.mulligan

case class DefaultString(v: String) extends DefaultElement[String]{
  override val value = v
  override def testMe ={
    constructSample.isInstanceOf[DefaultElement[String]]
  }
  override def constructSample = new DefaultString("")
  override def toString: String = this.value
}
object DefaultString{
  def apply(v: String): DefaultString ={
    new DefaultString(v)
  }
  def apply(): DefaultString = {
    new DefaultString("")
  }
}