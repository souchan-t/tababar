package tababar.domain.model

trait Role {
  def isAdmin: Boolean

  def isUser: Boolean
}

object Role {
  val USER = 0
  val ADMIN = 1
}
