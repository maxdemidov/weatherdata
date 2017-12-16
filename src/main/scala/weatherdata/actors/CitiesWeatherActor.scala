package weatherdata.actors

import akka.actor.{PoisonPill, Props}
import akka.pattern.ask
import weatherdata.model.{CityKey, CityLocation, CityWeather, CityWeatherData}

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

          val cityWeatherActorRef =
            actorSystem.actorOf(Props[CityWeatherActor])

          (cityWeatherActorRef ? GetCityWeatherByKey(cityKey))
            .mapTo[CityWeatherResponse]
            .map {
              case CityWeatherResult(cityWeatherEither) =>
                cityWeatherEither match {
                  case Right(cityWeather) =>
                    logger.info(
                      s"city id = ${cityWeather.cityKey.id} and data = ${cityWeather.data}")
                  case Left(message) =>
                    logger.info(message)
                }
            }

          val data =
            s"id = ${cityKey.id}, name = ${cityKey.name}, weather"

          CityWeather(cityKey,
                      "UA",
                      CityLocation(71, 26),
                      CityWeatherData(data, "icon", "base", 1, 2, 3, 4))
        })
      sender ! CitiesWeatherResult(citiesWeather)
      self ! PoisonPill
  }
}
