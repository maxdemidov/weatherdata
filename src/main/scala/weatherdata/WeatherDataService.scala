package weatherdata

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.{ExecutionContext, Future}

trait WeatherDataService {

  import weatherdata.model.{CityKeyList, CityWeatherList}
  import weatherdata.actors.CitiesWeatherActor
  import weatherdata.actors.CitiesWeatherActor._

  implicit def executionContext: ExecutionContext
  implicit def system: ActorSystem
  implicit def timeout: Timeout

  def getCitiesWeather(cityKeyList: CityKeyList): Future[CityWeatherList] = {

    val citiesWeatherActorRef =
      system.actorOf(Props[CitiesWeatherActor])

    (citiesWeatherActorRef ? GetCitiesWeatherByKeys(cityKeyList.citiesKeys))
      .mapTo[CitiesWeatherResponse]
      .map {
        case CitiesWeatherResult(citiesWeather) =>
          CityWeatherList(citiesWeather)
        case _ =>
          throw new IllegalStateException(
            "Unhandled response for WeatherDataService.getCitiesWeather")
      }
  }
}
