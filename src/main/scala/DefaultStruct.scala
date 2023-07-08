package com.github.rs17.mulligan

import db.{DefaultDB, FileStorageDB}

class DefaultStruct(key_i: Option[DefaultElement[Any]] = None) extends DefaultObject {
  // key is used to ensure that it's unique when stored
  val key: Option[DefaultElement[Any]] = Some(key_i.getOrElse(DefaultString(this.getClass.getName + this.hashCode())))

  override def constructSample: DefaultStruct = DefaultStruct

  override def testMe: Boolean = {
    super.testMe && saveTest
  }

  def saveTest: Boolean = {
    val db = new FileStorageDB
    val sample = constructSample
    sample.save(Some(db))
    val loaded = DefaultObject.load(db, sample.key.get)
    loaded.equals(Some(sample))
  }
  override def save(dbOpt: Option[DefaultDB]): Unit ={
    dbOpt.map(_.write(this))
  }
}

object DefaultStruct extends DefaultStruct(None){

}