package tababar.controller.common

import org.scalatra._
import tababar.util.WithLogger

/**
 * コントローラに認証機能を追加する。
 *
 * @tparam T ユーザモデル
 */
trait AuthenticateSupport[T] extends WithLogger {
  self: ScalatraBase =>

  /** 認証失敗時のリダイレクト先 */
  val redirectPath = "/"

  private val userAttributeKey = "tababar.controller.common.user"

  before() {

    val userOpt = session.get(AuthenticateSupport.sessionKey)
    userOpt match {
      case None => {
        logger.info("セッションがない。未認証ユーザ")
        halt(Unauthorized())
      }
      case Some(user) => {
        logger.info("セッションあり。認証済ユーザ")
        request.setAttribute(userAttributeKey, user)
      }
    }
  }

  /**
   * 認証済のユーザ
   *
   * @return ユーザモデルのインスタンス
   */
  def authenticatedUser: T = request.getAttribute(userAttributeKey).asInstanceOf[T]

}

object AuthenticateSupport {
  /** ユーザモデルを格納するセッションキー */
  val sessionKey = "tabbar.controller.common.sessionKey"
}