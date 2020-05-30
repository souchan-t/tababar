package tababar.controller.training

import com.google.inject.Inject
import org.json4s.DefaultFormats
import org.scalatra.{BadRequest, Ok, ScalatraServlet}
import org.scalatra.json.JacksonJsonSupport
import tababar.controller.common.{AuthenticateSupport, ErrorJsonFormat, IOFutureSupport, LoggingSupport}
import tababar.domain.model.{TrainingCard, User}
import tababar.domain.service.TrainingService
import tababar.util.IOExecutor

/**
 * 予約時のJSON入力フォーマット
 * @param trainingId
 */
private case class ReservationInputJson(trainingId:String)

/**
 * 検索時のトレーニングカードのJSON出力フォーマット
 */
private case class TrainingCardJson(
                                     id: String,
                                     status: Int,
                                     trainerId: String,
                                     name: String,
                                     startDate: Long,
                                     endDate: Long,
                                     location: String,
                                     trainees: Int,
                                     maxTrainees: Int,
                                     minTrainees: Int,
                                     describe: String,
                                     trainerName: String)
private object TrainingCardJson {
  def apply(card: TrainingCard): TrainingCardJson =
    new TrainingCardJson(
      card.training.id, card.training.status, card.training.trainerId, card.training.name,
      card.training.startDate.getTime, card.training.endDate.getTime, card.training.location, card.training.trainees,
      card.training.maxTrainees, card.training.minTrainees, card.training.describe, card.trainerName
    )
}
/**
 *
 * @param trainingService
 * @param ioExecutor
 */
class ReservationController @Inject() (val trainingService: TrainingService,val ioExecutor:IOExecutor)
  extends ScalatraServlet
    with LoggingSupport
    with AuthenticateSupport[User]
    with JacksonJsonSupport
    with IOFutureSupport{

  override implicit val jsonFormats: DefaultFormats.type = DefaultFormats

  before(){
    contentType = formats("json")
  }

  get("/item/:id/?"){
    val idOpt = params.get("id")
    idOpt match {
      case None => BadRequest(ErrorJsonFormat("IDが指定されていません。"))
      case Some(id) => {
        // TODO 未実装
        ???
      }
    }
  }

  /* ----------------------------------------------------------------------- */
  /*  受講状況照会                                                             */
  /* ----------------------------------------------------------------------- */
  get("/?") {
    val user = authenticatedUser;
    asyncProc{
      val outJson = trainingService.searchMyTraining(user).map(TrainingCardJson.apply)
      Ok(outJson)
    }
  }

  /*
   * トレーニング予約
   */

  post("/?"){
    val inputJson = parsedBody.extract[ReservationInputJson]
    val user = authenticatedUser

    asyncProc{
      trainingService.reserve(inputJson.trainingId,user.id) match {
        case Right(reservation) => Ok(reservation)
        case Left(msg)          => BadRequest(ErrorJsonFormat(msg))
      }
    }
  }

  /*
   * トレーニングキャンセル
   */
  delete("/item/:id/?"){
    params.get("id") match {
      case None => BadRequest(ErrorJsonFormat("IDを指定してください。"))
      case Some(id) => {
        val user = authenticatedUser
        asyncProc{
          trainingService.cancelReservation(id,user)
        }
      }
    }
  }

}
