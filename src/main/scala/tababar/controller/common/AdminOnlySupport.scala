package tababar.controller.common

import org.scalatra.{ScalatraBase, Unauthorized}
import tababar.domain.model.Role
import tababar.util.WithLogger

/**
 * コントローラにAdmin認可機能を追加する
 *
 * @tparam T ユーザモデル
 */
trait AdminOnlySupport[T <: Role] extends WithLogger {
  self: ScalatraBase with AuthenticateSupport[T] =>
  before() {
    if (!authenticatedUser.isAdmin) {
      logger.info(s"未認可")
      halt(Unauthorized("認可エラー。管理者のみ実行できます。"))
    }
  }
}
