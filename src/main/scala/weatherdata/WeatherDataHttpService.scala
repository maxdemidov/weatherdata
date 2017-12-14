package weatherdata

import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, path, pathPrefix, post}
import akka.http.scaladsl.server.Route

import scala.concurrent.{ExecutionContext, Future}

trait WeatherDataHttpService extends WeatherDataJsonSupport {

  implicit def executionContext: ExecutionContext

  lazy val citiesRoute: Route =
    pathPrefix("weatherdata") {
      path("cities") {
        post {
          entity(as[CityKeyList]) { cityKeyList =>
            val weatherForCities: Future[CityWeatherList] = Future {
              CityWeatherList(
                cityKeyList.cities.map( cityKey =>
                  CityWeather(cityKey, s"id = ${cityKey.id}, name = ${cityKey.name}, weather")
                )
              )
            }
            onComplete(weatherForCities) {
              weatherCities =>
                complete(weatherCities)
            }
          }
        }
      }
    }
}
