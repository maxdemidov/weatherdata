package weatherdata.actors

import akka.actor.Actor
import weatherdata.model.{CityKey, CityWeather}

object CitiesWeatherActor {
  sealed trait CitiesWeatherRequest
  case class GetCitiesWeatherByKeys(citiesKeys: List[CityKey])
      extends CitiesWeatherRequest

  sealed trait CitiesWeatherResponse
  case class CitiesWeather(citiesWeather: List[CityWeather])
      extends CitiesWeatherResponse
}
class CitiesWeatherActor extends Actor {

  import CitiesWeatherActor._

  override def receive: Receive = {

    case GetCitiesWeatherByKeys(citiesKeys) =>
      val citiesWeather =
        citiesKeys.map(cityKey => {
          val data = s"id = ${cityKey.id}, name = ${cityKey.name}, weather"
          CityWeather(cityKey, data)
        })
      sender ! CitiesWeather(citiesWeather)
  }
}
