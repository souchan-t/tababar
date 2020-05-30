package tababar.domain.service

import tababar.domain.model.User

trait UserService {

  def add(email: String, password: String, name: String): Either[String, User]

  def find(id: String): Option[User]

  def authenticate(email: String, password: String): Either[String, User]

}


