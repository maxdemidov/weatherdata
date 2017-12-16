package weatherdata.actors

import net.liftweb.json.{DefaultFormats, parse}
import weatherdata.WeatherDataConf
import weatherdata.model.{CityKey, CityWeather}

import scalaj.http.{Http, HttpResponse}

object CityWeatherActor {
  sealed trait CityWeatherRequest
  case class GetCityWeatherByKey(cityKey: CityKey) extends CityWeatherRequest

  sealed trait CityWeatherResponse
  case class CityWeatherResult(cityWeather: CityWeather)
      extends CityWeatherResponse
}
class CityWeatherActor extends ImplicitActor {

  //  type EitherWeather = Either[HttpResponse, CityWeatherList]

  import CityWeatherActor._

  implicit val formats: DefaultFormats.type = DefaultFormats

  val URL = "http://api.openweathermap.org/data/2.5/weather"
  val APP_ID: String = WeatherDataConf.openweathermapAppId

  override def receive: Receive = {

    case GetCityWeatherByKey(cityKey) =>
      val response: HttpResponse[String] =
        Http(s"$URL?id=2172797&appid=$APP_ID").asString

      val json = parse(response.body)

      val base = (json \ "base").extract[String]
      val description = ((json \ "weather")(0) \ "description").extract[String]

      val cityWeather = CityWeather(cityKey, s"$base - $description")

      sender ! CityWeatherResult(cityWeather)
  }
}
