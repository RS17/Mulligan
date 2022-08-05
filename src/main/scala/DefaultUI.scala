class DefaultUI extends DefaultStruct {
  def output(defaultModule: DefaultModule) = {
    println(makeText(defaultModule))
  }
  def makeText(module: DefaultModule): DefaultString = {
    DefaultString(module.key.getOrElse(DefaultString("")).toString + "\n" + module.payload.toString)
  }
}
