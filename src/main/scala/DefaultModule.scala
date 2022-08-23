package com.github.rs17.mulligan

import io.circe.Json
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._

class DefaultModule extends DefaultStruct(Some(DefaultString("DefaultModule"))){
  val db: Option[DefaultDB] = Some(new DefaultDB)
  val ui: Option[DefaultUI] = Some(new DefaultUI)
  val api: Option[DefaultAPI] = Some(new DefaultAPI(this))

  val savedPayload: Option[DefaultMap] = load
  val payload: DefaultMap = if(savedPayload.isDefined){
    savedPayload.get
  } else {
    new DefaultMap(Some(DefaultString("DefaultModuleMap")))
  }

  //override def save(db: DefaultDB): Unit = payload.save(db)
  def save:Unit = payload.save(db)

  def output = ui.map(_.output(this))

  // why these instead of calling load/save?  Because depending on circumstances it may be better to override
  // these so it wont load or save every time.
  def beforeGet(): Option[DefaultMap] = load
  def afterUpdate(): Unit = save

  def load: Option[DefaultMap] = {
    val loadResult = db.map(_.read(DefaultString("DefaultModuleMap"))).getOrElse(None)
    if(loadResult.isDefined) {
      loadResult.asInstanceOf[Some[DefaultMap]]
    } else {
      None
    }
  }

  // API stuff
  def get(): Option[DefaultObject] = {
    beforeGet()
    Some(payload)
  }

  def get(key: DefaultObject): Option[DefaultObject] = {
    beforeGet()
    payload.get(key)
  }

  def getJsonSamples(): Option[DefaultString] = {
    Some(DefaultString(Seq(DefaultString("test").asJson, DefaultLong(0).asJson).mkString("\n")))
  }

  def put(key: DefaultObject, value: DefaultObject): Option[DefaultObject] ={
    payload.set(key, value)
    afterUpdate()
    Some(DefaultString("Added " + value + " at " + key))
  }

  def post(value: DefaultObject): Option[DefaultObject] = {
    payload.add(value)
    afterUpdate()
    Some(value)
  }

  def delete(key: DefaultObject): Option[DefaultObject] = {
    payload.remove(key)
    afterUpdate()
    Some(DefaultString("deleted key " + key))
  }
}

object DefaultModule extends DefaultModule {
  load
  def main(args: Array[String]): Unit = {
    output
  }
}
