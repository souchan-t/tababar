package tababar.domain.repository

case class TransactionContext[T](val session: T)

trait WithTransactionContext[T] {
  implicit def toContext(implicit session: T): TransactionContext[T] = TransactionContext(session)

  implicit def toSession(implicit context: TransactionContext[T]): T = context.session
}
