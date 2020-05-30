package tababar.controller.common

import com.google.inject.Inject
import org.scalatra.{FutureSupport, ScalatraFilter}
import tababar.util.{GlobalConfig, IOExecutor, WithLogger}

/**
 * ロギングフィルター
 */
class LoggingFilter @Inject()(val globalConfig: GlobalConfig, val ioExecutor: IOExecutor)
  extends ScalatraFilter
    with WithLogger
    with IOFutureSupport {


  /* ----------------------------------------------------------------------- */
  /*  リクエストのロギング                                                      */
  /* ----------------------------------------------------------------------- */
  before("*") {
    logger.info(s"method:${request.getMethod},ip:${request.getRemoteAddr},${request.getRequestURI}")
  }
  /* ----------------------------------------------------------------------- */
  /*  レスポンスのロギング                                                      */
  /* ----------------------------------------------------------------------- */
  after("*") {
    logger.info(s"status:${response.getStatus}")
  }

  error {
    case e => logger.error(e.getMessage)
  }
}
