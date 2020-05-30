package tababar.controller.training

import java.util.Date

import com.google.inject.Inject
import org.json4s.DefaultFormats
import org.scalatra._
import org.scalatra.json._
import tababar.util.{IOExecutor, WithLogger}
import tababar.controller.common.{AuthenticateSupport, ErrorJsonFormat, IOFutureSupport, LoggingSupport}
import tababar.domain.model.{Training, TrainingCard, User}
import tababar.domain.service.TrainingService


/**
 * トレーニング登録 JSONフォーマット
 */
private case class CreateTrainingJson(name: String,
                                      startDate: Long,
                                      endDate: Long,
                                      location: String,
                                      maxTrainees: Int,
                                      minTrainees: Int,
                                      describe: String)


/**
 *
 * @param trainingService
 */
class TrainingController @Inject()(val trainingService: TrainingService, val ioExecutor: IOExecutor)
  extends ScalatraServlet
    with LoggingSupport
    with AuthenticateSupport[User]
    with JacksonJsonSupport
    with IOFutureSupport {

  override implicit val jsonFormats: DefaultFormats.type = DefaultFormats

  before() {
    contentType = formats("json")
  }

  /*
   * トレーニング情報
   */
  get("/item/:id/?") {
    val trainingIdOpt = params.get("id")

    trainingIdOpt match {
      case None => BadRequest(ErrorJsonFormat("トレーニングIDが指定されていない"))
      case Some(trainingId) => {
        asyncProc {
          trainingService.find(trainingId) match {
            case None => NotFound(ErrorJsonFormat("トレーニングが見つからない"))
            case Some(training) => Ok(training)
          }
        }
      }
    }
  }


  /*
   * トレーニングの削除
   */
  delete("/item/:id/?") {
    val trainingOpt = params.get("id")
    trainingOpt match {
      case None => BadRequest(ErrorJsonFormat("IDを指定してください。"))
      case Some(id) => {
        if (id.length != 32) {
          BadRequest(ErrorJsonFormat("IDが正しくありません。"))
        } else {

          ???

        }
      }
    }
  }


  /*
   * トレーニングの登録
   */
  post("/?") {
    val inputJson = parsedBody.extract[CreateTrainingJson]
    val user = authenticatedUser

    asyncProc {
      trainingService.create(
        user,
        inputJson.name,
        new Date(inputJson.startDate),
        new Date(inputJson.endDate),
        inputJson.location,
        inputJson.maxTrainees,
        inputJson.minTrainees,
        inputJson.describe)
      match {
        case Left(msg) => BadRequest(ErrorJsonFormat(msg))
        case Right(training) => Ok(training)
      }

    }
  }

  /*
   * トレーニングの検索
   */
  get("/search") {

    val keywordOpt = params.get("keyword")
    val locationOpt = params.get("location")
    val status = params.get("status").getOrElse("0").toInt

    keywordOpt match {
      case None => BadRequest(ErrorJsonFormat("キーワードを指定してください"))
      case Some(keyword) => {
        if (keyword.strip() == ""){
          BadRequest(ErrorJsonFormat("キーワードを指定してください。"))
        }else{
          asyncProc {
            val trainingCard = trainingService.searchByKeyword(keyword,locationOpt,status)
            Ok(trainingCard.map(TrainingCardJson.apply))
          }

        }
      }
    }
  }

}
