package tababar.db

import com.google.inject.{Inject, Singleton}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import scalikejdbc._
import tababar.util.{GlobalConfig, WithLogger}

/**
 * データベース設定
 *
 * @param config
 */
@Singleton
class Database @Inject()(val config: GlobalConfig) extends WithLogger with AutoCloseable {

  private val hikariConfig = new HikariConfig()
  config.get("db.user").foreach(user => hikariConfig.setUsername(user))
  config.get("db.password").foreach(password => hikariConfig.setPassword(password))
  config.get("db.driver").foreach(driver => hikariConfig.setDriverClassName(driver))
  config.get("db.url").foreach(url => hikariConfig.setJdbcUrl(url))
  config.get("db.cpMaxSize").foreach(maxsize => hikariConfig.setMaximumPoolSize(maxsize.toInt))
  config.get("db.autoCommit").foreach(autoCommit => hikariConfig.setAutoCommit(autoCommit == "true"))
  config.get("db.connectionTimeout").foreach(timeout => hikariConfig.setConnectionTimeout(timeout.toLong))
  config.get("db.isolationLevel").foreach(level => hikariConfig.setTransactionIsolation(level))

  val datasource = new HikariDataSource(hikariConfig)

  val connectionPool = new DataSourceConnectionPool(datasource)

  ConnectionPool.singleton(connectionPool)


  config.get("db.initSqlSource").foreach(filename => {
    val statement = scala.io.Source.fromResource(filename).getLines().reduce(_ + _)
    DB autoCommit { implicit session =>
      SQL(statement).execute().apply()
    }
  })

  /**
   * データソースを閉じる
   */
  override def close(): Unit = datasource.close()

}
