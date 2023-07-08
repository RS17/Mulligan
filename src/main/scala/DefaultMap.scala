package com.github.rs17.mulligan

import java.util.concurrent.atomic.AtomicLong

import scala.collection.mutable

class DefaultMap(key_i: Option[DefaultElement[Any]] = None) extends DefaultStruct(key_i) {
  val index: AtomicLong = new AtomicLong(0)
  val internalMap = mutable.Map[DefaultObject, DefaultObject]().withDefaultValue(DefaultLong(0L))

  override def toString: String = internalMap.toList.sortBy(_._1).map(elem => elem._1.toString + " " + elem._2.toString).foldLeft("")(_ + "\n" + _)

  def get(key: DefaultObject): Option[DefaultObject] = {
    internalMap.get(key)
  }

  def addOne(elem: (DefaultObject, DefaultObject)): DefaultMap.this.type = {
    internalMap.addOne(elem)
    this
  }

  def subtractOne(elem: DefaultObject): DefaultMap.this.type = {
    internalMap.subtractOne(elem)
    this
  }

  def add(obj: DefaultObject) = {
    addOne((DefaultLong(index.getAndAdd(1)), obj))
  }

  def contains(key: DefaultObject) = internalMap.contains(key)

  def set(key: DefaultObject, value: DefaultObject) = {
    internalMap(key) = value
  }

  def remove(key: DefaultObject) = {
    internalMap.remove(key)
  }

  def keys(): Iterable[DefaultObject] = internalMap.keys

}
