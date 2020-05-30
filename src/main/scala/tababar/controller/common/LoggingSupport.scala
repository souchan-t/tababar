package tababar.controller.common

import org.scalatra.ScalatraBase
import tababar.util.WithLogger

/**
 * コントローラにリクエスト・レスポンスログをつける。
 */
trait LoggingSupport extends WithLogger{ self:ScalatraBase =>

  before(){
    logger.info(s"method:${request.getMethod},ip:${request.getRemoteAddr},${request.getRequestURI}")
  }
  after() {
    logger.info(s"status:${response.getStatus}")
  }

  error{
    case e => logger.error(e.getMessage)
  }
}
