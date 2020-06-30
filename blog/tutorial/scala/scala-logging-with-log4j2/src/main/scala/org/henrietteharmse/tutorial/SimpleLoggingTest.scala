package org.henrietteharmse.tutorial

import com.typesafe.scalalogging.Logger

class SimpleLoggingTest {
  import SimpleLoggingTest.logger
  logger.trace("Hello while instance of SimpleLoggingTest is created.")
}

object SimpleLoggingTest {
  private val logger = Logger[SimpleLoggingTest]

  def main(args: Array[String]):Unit = {
    logger.trace("Hello from SimpleLoggingTest companion object")
    val simpleLoggingTest = new SimpleLoggingTest
    try {
      throw new RuntimeException("Some error")
    } catch {
      case t : Throwable => logger.error(s"Error: ${t.getMessage}")
    }
  }
}
