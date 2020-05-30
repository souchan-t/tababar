package tababar.domain.model

import java.util.Date
import tababar.util.IDGenerator

case class Training(
                     id: String,
                     status: Int,
                     trainerId: String,
                     name: String,
                     startDate: Date,
                     endDate: Date,
                     location:String,
                     trainees: Int,
                     maxTrainees: Int,
                     minTrainees: Int,
                     describe: String
                   ) {

}

object Training {
  def apply(trainer: User, name: String, startDate: Date, endDate: Date,location:String, maxTrainees: Int, minTrainees: Int, describe: String = ""): Training = {
    val id = IDGenerator.generate()
    val status = TrainingStatus.OPENED
    val trainerId = trainer.id

    new Training(id, status, trainerId, name, startDate, endDate, location,0, maxTrainees, minTrainees, describe)
  }
}
