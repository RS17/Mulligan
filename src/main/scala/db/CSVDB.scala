package db

import com.github.rs17.mulligan.{DefaultElement, DefaultObject, DefaultStruct}

import java.io.File

// WIP
class CSVDB extends DefaultDB {
  override def write(defObj: DefaultStruct) = {
    //TBD, don't need yet
  }

  override def read(key: DefaultElement[Any]): Option[DefaultObject] = {
    val path = s"./${key}"
    val file = new File(path)
    None
  }
}
