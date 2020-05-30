package tababar.controller.common


import org.scalatra.{AsyncResult, FutureSupport, ScalatraBase}
import tababar.util.WithIOExecutor

import scala.concurrent.{ExecutionContext, Future}

/**
 * コントローラにIOを別スレッド非同期で処理する機能を追加する
 */
trait IOFutureSupport extends FutureSupport with WithIOExecutor {
  self: ScalatraBase =>

  implicit lazy val executor: ExecutionContext = ioExecutor.executor

  def asyncProc[T](body: => T): AsyncResult =
    new AsyncResult() {
      override val is: Future[_] = Future(body)
    }

}
