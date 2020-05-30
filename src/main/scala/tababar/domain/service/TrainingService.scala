package tababar.domain.service

import java.util.Date

import tababar.domain.model.{Reservation, Training, TrainingCard, TrainingStatus, User}

trait TrainingService {
  /**
   *
   * @param trainer
   * @param name
   * @param startDate
   * @param endDate
   * @param location
   * @param maxTrainees
   * @param minTrainees
   * @param describe
   * @return
   */
  def create(trainer: User,
             name: String,
             startDate: Date,
             endDate: Date,
             location:String,
             maxTrainees: Int,
             minTrainees: Int,
             describe: String): Either[String, Training]

  /**
   * トレーニングを検索する
   * @param trainingId
   * @return
   */
  def find(trainingId: String): Option[Training]

  /**
   * トレーナーからトレーニングを検索する
   * @param trainer
   * @param status
   * @return
   */
  def searchByTrainer(trainer: User, status: Int = TrainingStatus.OPENED): Seq[Training]


  /**
   * ユーザの受講トレーニングを検索する
   * @param trainee
   * @param status
   * @return
   */
  def searchMyTraining(trainee:User,status:Int = TrainingStatus.OPENED):Seq[TrainingCard]

  /**
   * キーワードからトレーニングを検索する
   * @param keyword
   * @param location
   * @param status
   * @return
   */
  def searchByKeyword(keyword: String, location:Option[String],status: Int = TrainingStatus.OPENED): Seq[TrainingCard]

  def reserve(trainingId:String,traineeId:String):Either[String,Reservation]

  def cancelReservation(reservationId:String,user: User):Either[String,Unit]
}
