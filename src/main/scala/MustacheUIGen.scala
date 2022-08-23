import org.fusesource.scalate.TemplateEngine

import java.io.{File, FileWriter}

object MustacheUIGen {
  val sourceDataPath = new java.io.File("./src/templates/test.mustache").getCanonicalPath
  val title = DefaultModule.key
  val outputPath = new File("/index.html")
  outputPath.createNewFile()
  val engine = new TemplateEngine
  val someAttributes = Map(
    "title" -> title,
    "linkify" ->  ((moduleName: String) => "<link title=" + moduleName + " href = /" + moduleName),
    "linked_modules" -> "DefaultModule"
  )
  val result = engine.layout(sourceDataPath, someAttributes)
  println("can write?" + outputPath.canWrite)
  val writer = new FileWriter(outputPath)
  writer.append(result.toString)
  writer.close()

  println("wrote ui to: " + outputPath.getAbsolutePath)
}
