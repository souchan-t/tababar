package tababar.domain.model

import tababar.util.IDGenerator

case class Reservation(id:String,trainingId: String,traineeId:String) {

}
object Reservation {
  def apply(trainingId:String,traineeId:String):Reservation = new Reservation(IDGenerator.generate(),trainingId,traineeId)
}