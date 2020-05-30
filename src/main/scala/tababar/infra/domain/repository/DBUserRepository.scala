package tababar.infra.domain.repository

import com.google.inject.Singleton
import scalikejdbc._
import tababar.domain.model.User
import tababar.domain.repository.{TransactionContext, UserRepository}

@Singleton
class DBUserRepository extends UserRepository[DBSession] {

  private def resultSet2User(result: WrappedResultSet): User =
    User(
      result.string("id"),
      result.string("email"),
      result.string("name"),
      result.string("passwordHash"),
      result.int("role"))

  override def findByEmail(email: String)(implicit trxContext: TransactionContext[DBSession]): Option[User] =
    sql"SELECT * FROM users WHERE email = ${email}"
      .map(resultSet2User)
      .single()
      .apply()

  override def findById(id: String)(implicit trxContext: TransactionContext[DBSession]): Option[User] =
    sql"SELECT * FROM users WHERE id = ${id}"
      .map(resultSet2User)
      .single()
      .apply()


  override def save(user: User)(implicit trxContext: TransactionContext[DBSession]): Option[User] = {
    val count = sql"SELECT COUNT(*) FROM users WHERE email = ${user.email}"
      .map(result => result.int(1))
      .single()
      .apply().get

    if (count == 0) {
      sql"""
        INSERT INTO users VALUES(
          ${user.id},
          ${user.email},
          ${user.name},
          ${user.passwordHash},
          ${user.role});
        """
        .update()
        .apply()
      Some(user)
    } else {
      None
    }
  }
}
