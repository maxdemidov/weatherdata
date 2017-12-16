package weatherdata

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import weatherdata.model._

trait WeatherDataJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val cityKeyFormat: RootJsonFormat[CityKey] =
    jsonFormat2(CityKey)
  implicit val cityKeyListFormat: RootJsonFormat[CityKeyList] =
    jsonFormat1(CityKeyList)

  implicit val cityLocationFormat: RootJsonFormat[CityLocation] =
    jsonFormat2(CityLocation)
  implicit val cityWeatherDataFormat: RootJsonFormat[CityWeatherData] =
    jsonFormat7(CityWeatherData)
  implicit val cityWeatherFormat: RootJsonFormat[CityWeather] =
    jsonFormat4(CityWeather)
  implicit val cityWeatherListFormat: RootJsonFormat[CityWeatherList] =
    jsonFormat1(CityWeatherList)
}
