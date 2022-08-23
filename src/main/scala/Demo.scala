package com.github.rs17.mulligan

object Demo {
  // just takes input and stores it and displays it
  // this is basically a single-module workflow, could also be standardized to remove even these lines
  def main(args: Array[String]): Unit = {
    while(true) {
      println("Welcome to Mulligan demo! Enter text to have it stored and displayed forever.  Note the minimal code.")
      val module = DefaultModule
      MustacheUIGen
      module.output
      module.post(DefaultString(scala.io.StdIn.readLine("input: ").stripLineEnd))
    }
  }
}
