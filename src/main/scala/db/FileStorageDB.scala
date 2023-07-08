package db

import com.github.rs17.mulligan.{DefaultElement, DefaultObject, DefaultStruct}

import java.io._

class FileStorageDB extends DefaultDB {
  override def write(defObj: DefaultStruct) = {
    val path = s"$fileDirectory${defObj.key.getOrElse(new Exception("Key is missing"))}"
    val out = new ObjectOutputStream(new FileOutputStream(path))
    out.writeObject(defObj)
    out.close()
  }

  override def read(key: DefaultElement[Any]): Option[DefaultObject] = {
    val path = s"./${key}"
    val file = new File(path)
    if (file.exists()) {
      val in = new ObjectInputStream(new FileInputStream(file))
      val inResult = try {
        in.readObject()
      } catch {
        case ic: InvalidClassException => throw new InvalidClassException("read object at location " + file + " failed due to incompatible saved file, may need to delete old class: " + ic.getMessage)
        case ex: Throwable => throw ex
        //   case ex: Exception => "Read object failed with error: " + ex.toString
      } finally {
        in.close()
      }
      assert(inResult != null, "Error: null read from existing file. This might be suppressed error in instantiating object, such as due to reading in class definition.")
      val defObj: Option[DefaultObject] = if (inResult.isInstanceOf[DefaultObject]) {
        Some(inResult.asInstanceOf[DefaultObject])
      } else {
        None
      }
      defObj
    } else {
      None
    }
  }
}
