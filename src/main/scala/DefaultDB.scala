package com.github.rs17.mulligan

import java.io.{File, FileInputStream, FileOutputStream, InvalidClassException, ObjectInputStream, ObjectOutputStream}
import java.lang.System.Logger

class DefaultDB extends DefaultStruct {
  // based on https://stackoverflow.com/questions/47795723/save-a-case-class-in-scala-to-a-text-file-and-recreate-it-without-any-modificati
  def write(defObj: DefaultStruct) = {
    val path = s"./${defObj.key.getOrElse(new Exception("Key is missing"))}"
    val out = new ObjectOutputStream(new FileOutputStream(path))
    out.writeObject(defObj)
    out.close()
  }
  def read(key: DefaultElement[Any]): Option[DefaultObject] = {
    val path = s"./${key}"
    val file = new File(path)
    if(file.exists()) {
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
      assert(inResult!=null, "Error: null read from existing file. This might be suppressed error in instantiating object, such as due to reading in class definition.")
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
