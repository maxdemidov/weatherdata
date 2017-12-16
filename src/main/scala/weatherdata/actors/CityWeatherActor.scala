package weatherdata.actors

import akka.actor.PoisonPill
import net.liftweb.json.{DefaultFormats, parse}
import weatherdata.WeatherDataConf
import weatherdata.model.{CityKey, CityLocation, CityWeather, CityWeatherData}

import scalaj.http.{Http, HttpResponse}

object CityWeatherActor {
  sealed trait CityWeatherRequest
  case class GetCityWeatherByKey(cityKey: CityKey) extends CityWeatherRequest

  sealed trait CityWeatherResponse
  case class CityWeatherResult(cityWeatherEither: Either[String, CityWeather])
      extends CityWeatherResponse
}
class CityWeatherActor extends ImplicitActor {

  import CityWeatherActor._

  implicit val formats: DefaultFormats.type = DefaultFormats

  val URL = "http://api.openweathermap.org/data/2.5/weather"
  val APP_ID: String = WeatherDataConf.openweathermapAppId

  // TODO - add ScalaTest and ScalaMock (tests async rest call and actor performing and check actor destroying)
  // TODO - add Logging by actor
  // TODO - add Test event for check all actors in system
  // TODO - add pool for actor with size 3
  // TODO - add date filed (dt)

  override def receive: Receive = {

    case GetCityWeatherByKey(cityKey) =>
      val cityId = cityKey.id
      val cityName = cityKey.name

      val response: HttpResponse[String] =
        Http(s"$URL?id=$cityId&appid=$APP_ID").asString

      val json = parse(response.body)

      (json \ "name").extract[String] match {

        case thisName if thisName == cityName =>
          val cityWeather = CityWeather(
            cityKey,
            location = CityLocation(
              lon = (json \ "coord" \ "lon").extract[Float],
              lat = (json \ "coord" \ "lat").extract[Float]
            ),
            data = CityWeatherData(
              description =
                ((json \ "weather")(0) \ "description").extract[String],
              icon = ((json \ "weather")(0) \ "icon").extract[String],
              base = (json \ "base").extract[String],
              windSpeed = (json \ "wind" \ "speed").extract[Float],
              windDegrees = (json \ "wind" \ "deg").extract[Float],
              temperature = (json \ "main" \ "temp").extract[Float],
              humidity = (json \ "main" \ "humidity").extract[Byte]
            ),
            countryCode = (json \ "sys" \ "country").extract[String]
          )
          sender ! CityWeatherResult(Right(cityWeather))

        case otherName =>
          sender ! CityWeatherResult(
            Left(s"City name in cityKey is [$cityName] but found [$otherName]"))
      }
      self ! PoisonPill
  }
}
