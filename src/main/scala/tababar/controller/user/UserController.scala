package tababar.controller.user

import com.google.inject.Inject
import org.json4s.DefaultFormats
import org.scalatra._
import org.scalatra.json._
import tababar.controller.common.{AuthenticateSupport, ErrorJsonFormat, IOFutureSupport}
import tababar.domain.model.User
import tababar.domain.service.{TrainingService, UserService}
import tababar.util.{GlobalConfig, IOExecutor, WithLogger}

/**
 *
 * @param userService
 * @param trainingService
 * @param globalConfig
 * @param ioExecutor
 */
class UserController @Inject()(val userService: UserService,
                               val trainingService: TrainingService,
                               val globalConfig: GlobalConfig,
                               val ioExecutor: IOExecutor)
  extends ScalatraServlet
    with WithLogger
    with AuthenticateSupport[User]
    with JacksonJsonSupport
    with IOFutureSupport {

  val jsonFormats: DefaultFormats.type = DefaultFormats

  before() {
    contentType = formats("json")
  }

  /* ----------------------------------------------------------------------- */
  /*  ユーザー情報取得                                                         */
  /* ----------------------------------------------------------------------- */
  private case class UserInfoJsonFormat(email: String, name: String)

  get("/:userid/?") {

    val unsafeUserIdOpt = params.get("userid")

    unsafeUserIdOpt match {

      case None => BadRequest(ErrorJsonFormat("IDを指定してください"))

      case Some(unsafeUserId) => {
        //32桁数チェック
        if (unsafeUserId.length != 32) {

          BadRequest(ErrorJsonFormat("不正なユーザIDです。"))

        } else {

          val safeUserId = unsafeUserId
          asyncProc{
            userService.find(safeUserId) match {

              case None       => NotFound(ErrorJsonFormat("ユーザが存在しません。"))

              case Some(user) => Ok(UserInfoJsonFormat(user.email, user.name))
            }
          }
        }
      }
    }

  }


  /* ----------------------------------------------------------------------- */
  /*  開講状況照会                                                             */
  /* ----------------------------------------------------------------------- */
  get("/:userid/kaikou") {
    params.get("userid") match {
      case None => BadRequest(ErrorJsonFormat("IDが指定されていません"))
      case Some(userid) => {
        asyncProc{
          userService.find(userid) match {
            case None => BadRequest(ErrorJsonFormat("トレーナーが見つかりません"))
            case Some(trainer) => trainingService.searchByTrainer(trainer)
          }
        }

      }
    }


  }

}
