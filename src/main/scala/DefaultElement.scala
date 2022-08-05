trait DefaultElement[+A] extends DefaultObject with Castable[A] {
  override def constructSample: DefaultElement[Any] = DefaultElement

  override def save(db: Option[DefaultDB]): Unit = {}
}

object DefaultElement extends DefaultElement[Any]{
  override val value: Any = -1
}