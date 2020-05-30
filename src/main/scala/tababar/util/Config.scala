package tababar.util

/**
 * 読み取り専用コンフィギュレーション
 */
trait Config {

  def get(key: String): Option[String]

  def getOrElse(key: String, _else: String): String =
    this.get(key) match {
      case Some(value) => value
      case None => _else
    }

  def getOrElse(key: String, _else: Int): Int =
    this.get(key) match {
      case Some(value) => value.toInt
      case None => _else
    }

  def getOrElse(key: String, _else: Long): Long =
    this.get(key) match {
      case Some(value) => value.toLong
      case None => _else
    }

  def getOrElse(key: String, _else: Double): Double =
    this.get(key) match {
      case Some(value) => value.toDouble
      case None => _else
    }

  def getOrElse(key: String, _else: Boolean): Boolean =
    this.get(key) match {
      case Some(value) => value.toBoolean
      case None => _else
    }
}
