package tababar.util

import java.util.Properties

import com.google.inject.Singleton

/**
 * アプリケーションの設定クラス
 */
trait GlobalConfig extends Config

/**
 * リソースファイルconfig.propertiesからアプリケーション設定を読み込む
 */
@Singleton
class ResourceGlobalConfig extends GlobalConfig {
  lazy val properties = new Properties()
  properties.load(scala.io.Source.fromResource("config.properties").bufferedReader())

  override def get(key: String): Option[String] = Option(this.properties.getProperty(key))

}

/**
 * 外部プロパティファイルからアプリケーション設定を読み込む
 *
 * @param filename
 */
class FileGlobalConfig(filename: String) extends GlobalConfig {
  lazy val properties = new Properties()
  properties.load(scala.io.Source.fromFile(filename).bufferedReader())

  override def get(key: String): Option[String] = Option(this.properties.getProperty(key))

}