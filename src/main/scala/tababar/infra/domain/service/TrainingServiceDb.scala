package tababar.infra.domain.service

import java.util.Date

import com.google.inject.Inject
import javax.inject.Singleton
import scalikejdbc._
import tababar.domain.model.{Reservation, Training, TrainingCard, TrainingStatus, User}
import tababar.domain.repository.{TrainingRepository, UserRepository, WithTransactionContext}
import tababar.domain.service.TrainingService
import tababar.util.WithLogger


@Singleton
class TrainingServiceDb @Inject()(trainingRepository: TrainingRepository[DBSession], userRepository: UserRepository[DBSession])
  extends TrainingService
    with WithLogger
    with WithTransactionContext[DBSession] {

  override def create(trainer: User,
                      name: String,
                      startDate: Date,
                      endDate: Date,
                      location: String,
                      maxTrainees: Int,
                      minTrainees: Int,
                      describe: String): Either[String, Training] = {

    val training = Training(trainer, name, startDate, endDate, location, maxTrainees, minTrainees, describe)

    DB localTx { implicit session =>
      trainingRepository.save(training)
    } match {
      case Left(msg) => Left(msg)
      case Right(_) => Right(training)
    }
  }

  override def find(trainingId: String): Option[Training] =
    DB readOnly { implicit session =>
      trainingRepository.findById(trainingId)
    }

  override def searchByTrainer(trainer: User, status: Int = TrainingStatus.OPENED): Seq[Training] =
    DB readOnly { implicit session =>
      trainingRepository.findByTrainer(trainer.id, status)
    }

  override def searchMyTraining(trainee: User, status: Int): Seq[TrainingCard] =
    DB readOnly { implicit session =>
      trainingRepository.findByTrainee(trainee.id);
    }

  override def searchByKeyword(keyword: String, location: Option[String], status: Int = TrainingStatus.OPENED): Seq[TrainingCard] =

    DB readOnly { implicit session =>
      trainingRepository.findByKeyword(keyword, location, status)
    }

  override def reserve(trainingId: String, traineeId: String): Either[String, Reservation] =
    DB localTx { implicit session =>
      userRepository.findById(traineeId) match {
        case None => Left("指定したユーザが見つかりません")
        case Some(trainee) => {
          trainingRepository.findById(trainingId) match {
            case None => Left("指定したトレーニングが見つかりません。")
            case Some(training) => {
              if (training.trainerId == trainee.id) Left("自分のトレーニングは予約出来ません。") else trainingRepository.reserve(training, trainee)
            }
          }
        }
      }
    }

  override def cancelReservation(reservationId: String, user: User): Either[String, Unit] =
    DB localTx { implicit session =>
      //TODO 未実装
      ???
    }
}
