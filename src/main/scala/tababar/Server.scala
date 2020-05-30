package tababar

import org.eclipse.jetty.server.{HttpConfiguration, HttpConnectionFactory, ServerConnector}
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.eclipse.jetty.webapp.WebAppContext
import tababar.util.{Config, WithLogger}

/**
 * アプリケーションサーバ（Jetty）の起動・停止
 *
 * @param config
 */
class Server(val config: Config) extends WithLogger {

  private val server = new org.eclipse.jetty.server.Server(
    new QueuedThreadPool(config.getOrElse("server.threadSize", 4)))

  /**
   * Jettyサーバの起動
   */
  def start(): Unit = {

    val httpConfig = new HttpConfiguration()
    val httpConnectionFactory = new HttpConnectionFactory(httpConfig)
    val serverConnector = new ServerConnector(server, httpConnectionFactory)

    // ポート設定
    serverConnector.setPort(config.getOrElse("server.port", 8000))
    logger.info(s"set port to ${serverConnector.getPort}")

    server.setConnectors(Array(serverConnector))
    server.setHandler(createWebAppContext())

    logger.info("Jetty Server starting...")
    server.start()
    logger.info("Jetty Server started")

    // JVM終了時にサーバを停止する
    val that = this
    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
      override def run(): Unit = that.stop()
    }))
  }

  /**
   * Jettyサーバ停止
   */
  def stop(): Unit = {
    logger.info("Jetty Server stopping...")
    server.stop()
    logger.info("Jetty Server stopped")
  }

  private def createWebAppContext(): WebAppContext = {
    val context = new WebAppContext()

    // プロダクションモードの設定
    val isProduction = config.getOrElse("server.production", false)
    context.setInitParameter(org.scalatra.EnvironmentKey, if (isProduction) "production" else "development")


    // コンテキストパスの設定
    context.setContextPath("/")

    // リソースベースの設定
    context.setResourceBase(config.getOrElse("server.resourceBase", "src/main/webapp"))

    context.addServlet(classOf[DefaultServlet], "/")

    context
  }
}
