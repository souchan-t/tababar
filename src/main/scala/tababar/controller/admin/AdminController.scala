package tababar.controller.admin

import org.scalatra._
import tababar.controller.common.{AdminOnlySupport, AuthenticateSupport}
import tababar.domain.model.User
import tababar.util.WithLogger

class AdminController extends ScalatraServlet
  with WithLogger
  with AuthenticateSupport[User]
  with AdminOnlySupport[User] {

  post("/") {
    Ok("you are admin")
  }
}
