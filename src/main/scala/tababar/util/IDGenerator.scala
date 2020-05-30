package tababar.util

import java.util.UUID

/**
 * UUID
 */
object IDGenerator {
  def generate(): String = UUID.randomUUID().toString.replaceAll("-", "")

}
