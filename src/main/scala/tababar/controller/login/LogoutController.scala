package tababar.controller.login

import org.scalatra._
import tababar.controller.common.LoggingSupport
import tababar.util.WithLogger

/**
 * セッションを破棄しログアウトを行う。
 */
class LogoutController extends ScalatraServlet with LoggingSupport{

  /* ----------------------------------------------------------------------- */
  /*  ログアウト                                                              */
  /* ----------------------------------------------------------------------- */
  get("/?") {
    logger.info("logout")
    session.invalidate()
    redirect("/")
  }
}
