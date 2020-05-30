package tababar.infra.domain.repository

import com.google.inject.Singleton
import scalikejdbc._
import tababar.domain.model.{Reservation, Training, TrainingCard, TrainingStatus, User}
import tababar.domain.repository.{TrainingRepository, TransactionContext}

@Singleton
class DBTrainingRepository extends TrainingRepository[DBSession] {
  private def resultSet2Training(result: WrappedResultSet): Training =
    Training(
      result.string("trainings.id"),
      result.int("trainings.status"),
      result.string("trainings.trainerId"),
      result.string("trainings.name"),
      result.date("trainings.startDate"),
      result.date("trainings.endDate"),
      result.string("trainings.location"),
      result.int("trainings.trainees"),
      result.int("trainings.maxTrainees"),
      result.int("trainings.minTrainees"),
      result.string("trainings.describe"))


  private def resultSet2TrainingCard(result: WrappedResultSet): TrainingCard =
    TrainingCard(resultSet2Training(result), result.string("users.name"))

  private def resultSet2Reservation(result: WrappedResultSet): Reservation =
    Reservation(
      result.string("id"),
      result.string("trainingId"),
      result.string("traineeId")
    )

  override def findById(id: String)(implicit ctx: TransactionContext[DBSession]): Option[Training] =
    sql"""
         SELECT * FROM trainings WHERE id = ${id};
       """
      .map(resultSet2Training)
      .single()
      .apply()

  override def findByTrainer(trainerId: String, status: Int)(implicit ctx: TransactionContext[DBSession]): Seq[Training] =
    sql"""
         SELECT * FROM trainings WHERE trainerId = ${trainerId} AND status = ${status};
       """
      .map(resultSet2Training)
      .toList()
      .apply()


  override def findByTrainee(traineeId: String, status: Int)(implicit ctx: TransactionContext[DBSession]): Seq[TrainingCard] =
    sql"""
         SELECT reservations.*,trainings.*,users.*
         FROM reservations
         INNER JOIN trainings ON reservations.trainingId = trainings.id
         INNER JOIN users ON reservations.traineeId = users.id
         WHERE reservations.traineeId = ${traineeId} AND trainings.status = ${status}
       """
      .map(resultSet2TrainingCard)
      .toList()
      .apply()

  override def findByKeyword(keyword: String, location: Option[String], status: Int = TrainingStatus.OPENED)(implicit ctx: TransactionContext[DBSession]): Seq[TrainingCard] = {
    val likeKeyword = s"%${keyword}%"


    val query = location match {
      case None =>
        sql"""
         SELECT trainings.*,users.name
         FROM trainings
         INNER JOIN users ON trainings.trainerId = users.id
         WHERE trainings.status = ${status} AND trainings.name LIKE ${likeKeyword};
       """
      case Some(loc) =>
        sql"""
         SELECT trainings.*,users.name
         FROM trainings
         INNER JOIN users ON trainings.trainerId = users.id
         WHERE trainings.status = ${status} AND trainings.location = ${loc} AND trainings.name LIKE ${likeKeyword};
       """
    }

    query
      .map(resultSet2TrainingCard)
      .toList()
      .apply()

  }

  override def save(training: Training)(implicit ctx: TransactionContext[DBSession]): Either[String, Unit] =
    this.findById(training.id)(ctx) match {
      case Some(_) => Left("既に存在するIDです")
      case None => {
        sql"""
         INSERT INTO trainings VALUES(
         ${training.id},
         ${training.status},
         ${training.trainerId},
         ${training.name},
         ${training.startDate},
         ${training.endDate},
         ${training.location},
         ${training.trainees},
         ${training.maxTrainees},
         ${training.minTrainees},
         ${training.describe})
       """
          .execute()
          .apply()
        Right()
      }
    }

  override def delete(id: String)(implicit ctx: TransactionContext[DBSession]): Either[String, Unit] =

    this.findById(id)(ctx) match {
      case None => Left("削除対象が見つかりません")
      case Some(training) => {
        sql"""
         DELETE FROM trainings WHERE id = ${training.id};
       """
          .execute()
          .apply()
        Right()
      }
    }

  private def updateTrainee(training: Training, traineeCount: Int)(implicit ctx: TransactionContext[DBSession]): Either[String, Unit] = {
    val updateCount = sql"UPDATE trainings SET trainees = ${traineeCount} WHERE id = ${training.id};"
      .update()
      .apply()

    if (updateCount > 0) Right() else Left("トレーニングを更新出来ませんでした。")
  }


  override def reserve(training: Training, trainee: User)(implicit ctx: TransactionContext[DBSession]): Either[String, Reservation] = {

    //トレーニングの存在チェック
    this.findById(training.id)(ctx) match {
      case None => Left("トレーニングが見つかりません")

      case Some(t) => {

        //空席のチェック
        if (t.trainees >= t.maxTrainees) {

          Left("トレーニングは満席です")

        } else {
          // 既に予約済かのチェック
          val hasAlready = sql"SELECT * FROM reservations WHERE trainingId = ${t.id} AND traineeId = ${trainee.id}"
            .map(resultSet2Reservation)
            .single()
            .apply()
            .isDefined

          // 既に予約済かのチェック
          if (hasAlready) {
            Left("トレニーングは既に予約されています。")
          } else {

            // トレーニングの受講者数をカウントアップ
            this.updateTrainee(t, t.trainees + 1)(ctx) match {
              // カウントアップ失敗
              case Left(msg) => Left(msg)

              // カウントアップ成功
              case Right(_) => {
                // 予約の作成
                val reservation = Reservation(training.id, trainee.id)
                val insertCount =
                  sql"""INSERT INTO reservations VALUES
                       (${reservation.id},${reservation.trainingId},${reservation.traineeId});"""
                    .update()
                    .apply()
                Right(reservation)
              }
            }
          }
        }
      }
    }
  }
}
