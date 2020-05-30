package tababar.controller.login

import com.google.inject.Inject
import org.scalatra._
import org.scalatra.forms._
import org.scalatra.i18n.I18nSupport
import tababar.controller.common.LoggingSupport
import tababar.domain.service.UserService
import tababar.util.WithLogger

/**
 * ユーザー登録を行う。
 *
 * @param userService
 */
class SignUpController @Inject()(val userService: UserService) extends ScalatraServlet
  with LoggingSupport
  with I18nSupport
  with FormSupport {

  /* ----------------------------------------------------------------------- */
  /*  サインアップ                                                             */
  /* ----------------------------------------------------------------------- */

  /** ログインフォーム */
  private case class SignUpForm(email: String, password: String, name: String)

  /** ログインフォームとリクエストのマッピング */
  private val signUpForm = mapping(
    "email" -> text(required, maxlength(256)),
    "password" -> text(required, maxlength(256)),
    "name" -> text(required, maxlength(256))
  )(SignUpForm.apply)

  post("/?") {

    // フォームの検証
    validate(signUpForm)(
      { errors => BadRequest(errors)
      },
      { form => {
        // ユーザの登録
        val registResult = userService.add(form.email, form.password, form.name)
        registResult match {
          case Left(msg) => BadRequest(msg)
          case Right(user) => Ok(user)
        }
      }
      }
    )
  }

}
