package db

import com.github.rs17.mulligan.{DefaultElement, DefaultModule, DefaultObject, DefaultStruct}

class SQLiteDB extends DefaultDB {

  import java.sql.DriverManager

  val conn = DriverManager.getConnection("jdbc:sqlite:test.db")

  def init(module: DefaultModule): Unit ={
    // TODO: create table if not exist for every object type in module
  }

  // this will overwrite data if it already exists at the key.  It is up to the rest of Mulligan to ensure this doesn't
  // happen unintentionally
  override def write(obj: DefaultStruct) = {
    val cmd = "INSERT INTO " + obj.key + "VALUES('this isn't done yet')"
  }
  override def read(key: DefaultElement[Any]): Option[DefaultObject] = {
    None
  }
}
