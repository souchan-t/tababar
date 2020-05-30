package tababar.infra.domain.service

import com.google.inject.{Inject, Singleton}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import scalikejdbc.{DB, DBSession}
import tababar.domain.model.User
import tababar.domain.repository.{UserRepository, WithTransactionContext}
import tababar.domain.service.UserService

@Singleton
class UserServiceDb @Inject()(val userRepository: UserRepository[DBSession])
  extends UserService
    with WithTransactionContext[DBSession] {

  override def add(email: String, password: String, name: String): Either[String, User] =
    DB localTx { implicit session =>
      userRepository.save(User(email, name, password)) match {
        case None => Left("ユーザが既に存在する")
        case Some(user) => Right(user)
      }
    }


  override def find(id: String): Option[User] =
    DB readOnly { implicit session =>
      userRepository.findById(id)
    }

  override def authenticate(email: String, password: String): Either[String, User] =
    DB readOnly { implicit session =>
      userRepository.findByEmail(email) match {
        case None => Left("ユーザが見つからない。")
        case Some(user) => {
          val encoder = new BCryptPasswordEncoder()
          if (encoder.matches(password, user.passwordHash)) Right(user) else Left("ユーザ又はパスワードが誤っている。")
        }
      }

    }
}
