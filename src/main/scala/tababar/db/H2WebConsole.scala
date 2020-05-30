package tababar.db

import org.h2.tools.Console
import tababar.util.WithLogger

/**
 * H2WebConsoleの起動
 */
class H2WebConsole extends WithLogger {
  private val console = new Console()
  private val runThread = new Thread(() => console.runTool())

  /**
   * 起動
   */
  def start(): Unit = {

    runThread.start()
    logger.info("H2 WebConsole start")
  }

  /**
   * 停止
   */
  def stop(): Unit = {
    console.shutdown()
    logger.info("H2 WebConsole shutdown")
  }
}
