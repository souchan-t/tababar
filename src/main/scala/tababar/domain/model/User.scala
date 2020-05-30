package tababar.domain.model

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import tababar.util.IDGenerator

/**
 * ユーザーモデル
 *
 * @param id    UUID
 * @param email e-mail 重複があってはならない
 * @param name
 * @param passwordHash
 * @param role
 */
case class User(id: String,
                email: String,
                name: String,
                passwordHash: String,
                role: Int
               ) extends Role {

  override def isAdmin: Boolean = this.role == Role.ADMIN

  override def isUser: Boolean = this.role == Role.ADMIN || this.role == Role.USER
}

object User {
  def apply(id: String, email: String, name: String, passwordHash: String, role: Int): User = {
    new User(id, email, name, passwordHash, role: Int)
  }

  def apply(email: String, name: String, password: String): User = {
    val bcrypt = new BCryptPasswordEncoder()
    val passwordHash = bcrypt.encode(password)
    new User(IDGenerator.generate(), email, name, passwordHash, 0)
  }
}