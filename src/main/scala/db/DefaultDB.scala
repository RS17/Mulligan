package db

import com.github.rs17.mulligan.{DefaultElement, DefaultObject, DefaultStruct}

trait DefaultDB extends DefaultStruct {
  // based on https://stackoverflow.com/questions/47795723/save-a-case-class-in-scala-to-a-text-file-and-recreate-it-without-any-modificati
  val fileDirectory = "./"

  def write(defObj: DefaultStruct)

  def read(key: DefaultElement[Any]): Option[DefaultObject]
}
