package weatherdata.actors

import akka.actor.Props
import akka.pattern.ask
import weatherdata.model.{CityKey, CityWeather}

object CitiesWeatherActor {
  sealed trait CitiesWeatherRequest
  case class GetCitiesWeatherByKeys(citiesKeys: List[CityKey])
      extends CitiesWeatherRequest

  sealed trait CitiesWeatherResponse
  case class CitiesWeatherResult(citiesWeather: List[CityWeather])
      extends CitiesWeatherResponse
}
class CitiesWeatherActor extends ImplicitActor {

  import CitiesWeatherActor._
  import CityWeatherActor._

  override def receive: Receive = {

    case GetCitiesWeatherByKeys(citiesKeys) =>
      val citiesWeather =
        citiesKeys.map(cityKey => {

          val data = s"id = ${cityKey.id}, name = ${cityKey.name}, weather"

          val cityWeatherActorRef =
            actorSystem.actorOf(Props[CityWeatherActor])

          (cityWeatherActorRef ? GetCityWeatherByKey(cityKey))
            .mapTo[CityWeatherResponse]
            .map {
              case CityWeatherResult(cityWeather) =>
                logger.info(
                  s"city id = ${cityWeather.cityKey.id} and data = ${cityWeather.data}")
            }

          CityWeather(cityKey, data)
        })
      sender ! CitiesWeatherResult(citiesWeather)
  }
}
