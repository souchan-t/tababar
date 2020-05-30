package tababar.controller.login

import com.google.inject.Inject
import org.scalatra._
import org.scalatra.forms.{FormSupport, label, mapping, maxlength, required, text}
import org.scalatra.i18n.I18nSupport
import tababar.controller.common.{AuthenticateSupport, LoggingSupport}
import tababar.domain.service.UserService

/**
 * ログイン処理と認証を行う
 *
 * @param userService
 */
class LoginController @Inject()(userService: UserService) extends ScalatraServlet
  with LoggingSupport
  with FormSupport
  with I18nSupport {


  /* ----------------------------------------------------------------------- */
  /*  ログインページ                                                           */
  /* ----------------------------------------------------------------------- */
  get("/?") {
    views.html.login()
  }

  /* ----------------------------------------------------------------------- */
  /*  認証                                                                   */
  /* ----------------------------------------------------------------------- */

  /** ログインフォーム */
  private case class LoginForm(email: String, password: String)

  /** フォームとリクエストのマッピング */
  private val loginForm = mapping(
    "email" -> label("email", text(required, maxlength(256))),
    "password" -> label("password", text(required, maxlength(256)))
  )(LoginForm.apply)

  post("/login/?") {

    // フォームの検証
    validate(loginForm)(
      {
        errors: Seq[(String, String)] => BadRequest(errors)
      },
      {
        form: LoginForm => {
          // 認証の実施
          val authResult = userService.authenticate(form.email, form.password)

          authResult match {
            //認証失敗時
            case Left(value) => Unauthorized(value)

            //認証成功時
            case Right(user) => {
              session.invalidate()
              session.setAttribute(AuthenticateSupport.sessionKey, user)
              session.setMaxInactiveInterval(60 * 60 * 12)

              //ユーザページへリダイレクト
              redirect(s"/public/application.html")
            }
          }
        }
      }
    )
  }


}
