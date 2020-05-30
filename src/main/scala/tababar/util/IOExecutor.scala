package tababar.util

import java.util.concurrent.Executors

import com.google.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext

trait WithIOExecutor {
  val ioExecutor: IOExecutor
}

@Singleton
class IOExecutor @Inject()(val globalConfig: GlobalConfig) {

  val executor = ExecutionContext
    .fromExecutorService(Executors.newFixedThreadPool(globalConfig.getOrElse("server.executorThreadNums", 8)))

  def shutdown(): Unit = executor.shutdown()
}
