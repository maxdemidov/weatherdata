package weatherdata

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, path, pathPrefix, post}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import weatherdata.actors.CitiesWeatherActor
import weatherdata.model.CityKeyList

import scala.concurrent.{ExecutionContext, Future}

trait WeatherDataHttpService extends WeatherDataJsonSupport {

  import weatherdata.actors.CitiesWeatherActor._

  implicit def executionContext: ExecutionContext
  implicit def system: ActorSystem
  implicit def timeout: Timeout

  lazy val citiesRoute: Route =
    pathPrefix("weatherdata") {
      path("cities") {
        post {
          entity(as[CityKeyList]) { cityKeyList =>
            val weatherCitiesRef =
              system.actorOf(Props[CitiesWeatherActor])
            val citiesWeatherFuture =
              (weatherCitiesRef ? GetCitiesWeatherByKeys(cityKeyList.citiesKeys))
                .mapTo[CitiesWeatherResponse].map {
                  case CitiesWeather(citiesWeather) => citiesWeather
                }
            onComplete(citiesWeatherFuture) {
              weatherCities =>
                complete(weatherCities)
            }
          }
        }
      }
    }
}
