import tababar.Server
import tababar.db.{Database, H2WebConsole}
import tababar.util.ResourceGlobalConfig

object Main {
  def main(args: Array[String]): Unit = {

    val config = new ResourceGlobalConfig()

    val server = new Server(config)

    //H2WebConsoleの設定がある場合、コンソール作成
    val h2WebConsole = if (config.getOrElse("H2WebConsole", false)) {
      Some(new H2WebConsole())
    } else {
      None
    }

    //H2WebConsoleの起動
    h2WebConsole.foreach(_.start())

    //アプリケーションサーバ起動
    server.start()

    scala.io.StdIn.readLine()

    //サーバの停止
    server.stop()

    //H2WebConsole停止
    h2WebConsole.foreach(_.stop())

  }
}
