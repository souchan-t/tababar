package tababar.util

import org.slf4j.LoggerFactory

trait WithLogger {
  lazy val logger = LoggerFactory.getLogger(this.getClass)
}
