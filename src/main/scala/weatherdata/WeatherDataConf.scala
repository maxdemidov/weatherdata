package weatherdata

import com.typesafe.config.{Config, ConfigFactory}

trait WeatherDataConf {

  val config: Config = ConfigFactory.load("weatherdata.conf")

  lazy val host: String = config.getString("http.host")
  lazy val port: Int = config.getInt("http.port")
}
