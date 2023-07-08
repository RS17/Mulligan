package html

import com.github.rs17.mulligan.{DefaultModule, DefaultString}

import java.io.{File, FileWriter}

object HTMLUIGen {
  //val sourceDataPath = new java.io.File("./src/templates/test.mustache").getCanonicalPath
  val thisModule = DefaultModule //to be changed to parameter
  val title = thisModule.key.get
  val outputPath = new File(s"$title.html")
  outputPath.createNewFile()
  //val engine = new TemplateEngine
  /*val pageAttributes = Map(
    "title" -> title,
    "linkify" ->  ((moduleName: String) => ),
    "linked_modules" -> "DefaultModule"
  )*/
  val modules = List(thisModule) //todo: add linkedmodules
  val header = DefaultString(s"<head><title>$title</title></head>")
  val linkedModules = DefaultString(modules.map(module => linkify(module.key.get.toString)).mkString("\n"))
  val moduleData = thisModule.payload.toString

  // unfortunately I'm unable to find any way with http4s to read the endpoints and paths from the API.
  // Need to re-specify what points we want to call here and manually update in case of changes.
  val apiPoints = thisModule.api.map(api => {
    DefaultString(fetcher(api.path.toString()))
  }).getOrElse("")
  val body = s"<body><h1>$title</h1>" + linkedModules + "<br><br>" + moduleData + "<br><br>" + apiPoints + "</body>"
  val result = header + "\n" + body
  val writer = new FileWriter(outputPath)
  writer.append(result)
  writer.close()

  println("wrote ui to: " + outputPath.getAbsolutePath)

  def linkify(linkName: String) = {
    "<a href = \"" + linkName + "\">" + linkName + "</a>"
  }

  def javascriptify(code: String): String = {
    s"<script type=\"text/javascript\">\n$code\n</script>"
  }

  def fetcher(path: String): String = {
    javascriptify(s"fetch('$path').then(response => \n{return response.json();})")
  }
}
