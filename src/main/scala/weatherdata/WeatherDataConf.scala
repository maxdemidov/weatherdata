package weatherdata

import com.typesafe.config.{Config, ConfigFactory}

object WeatherDataConf {

  val config: Config = ConfigFactory.load("weatherdata.conf")

  lazy val host: String = config.getString("http.host")
  lazy val port: Int = config.getInt("http.port")

  lazy val requestTimeout: Int =
    config.getInt("akka.requestTimeout")
  lazy val loggingActorName: String =
    config.getString("akka.loggingActorName")

  lazy val openweathermapAppId: String =
    config.getString("openweathermap.applicationId")
}
