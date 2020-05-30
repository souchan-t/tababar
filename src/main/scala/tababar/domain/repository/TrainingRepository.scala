package tababar.domain.repository

import tababar.domain.model.{Reservation, Training, TrainingCard, TrainingStatus, User}

trait TrainingRepository[T] extends WithTransactionContext[T] {

  def findById(id: String)(implicit ctx: TransactionContext[T]): Option[Training]

  def findByTrainer(trainerId: String, status: Int = TrainingStatus.OPENED)(implicit ctx: TransactionContext[T]): Seq[Training]

  def findByTrainee(traineeId:String,status:Int = TrainingStatus.OPENED)(implicit ctx: TransactionContext[T]):Seq[TrainingCard]

  def findByKeyword(keyword: String, location:Option[String],status: Int = TrainingStatus.OPENED)(implicit ctx: TransactionContext[T]): Seq[TrainingCard]

  def save(training: Training)(implicit ctx: TransactionContext[T]): Either[String, Unit]

  def delete(id: String)(implicit ctx: TransactionContext[T]): Either[String, Unit]

  def reserve(training:Training,trainee:User)(implicit ctx:TransactionContext[T]):Either[String,Reservation]

}
