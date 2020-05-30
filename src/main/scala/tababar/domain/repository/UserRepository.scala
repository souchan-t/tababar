package tababar.domain.repository

import tababar.domain.model.User

trait UserRepository[T] extends WithTransactionContext[T] {

  def findByEmail(email: String)(implicit trxContext: TransactionContext[T]): Option[User]

  def findById(id: String)(implicit trxContext: TransactionContext[T]): Option[User]

  def save(user: User)(implicit trxContext: TransactionContext[T]): Option[User]

}

