package weatherdata

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import weatherdata.model.{CityKey, CityKeyList, CityWeather, CityWeatherList}

trait WeatherDataJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val cityKeyFormat: RootJsonFormat[CityKey] =
    jsonFormat2(CityKey)
  implicit val cityKeyListFormat: RootJsonFormat[CityKeyList] =
    jsonFormat1(CityKeyList)

  implicit val cityWeatherFormat: RootJsonFormat[CityWeather] =
    jsonFormat2(CityWeather)
  implicit val cityWeatherListFormat: RootJsonFormat[CityWeatherList] =
    jsonFormat1(CityWeatherList)
}
