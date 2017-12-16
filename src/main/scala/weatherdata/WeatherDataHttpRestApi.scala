package weatherdata

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{
  as,
  complete,
  entity,
  onComplete,
  path,
  pathPrefix,
  post
}
import akka.http.scaladsl.server.Route
import scala.util.{Failure, Success}

trait WeatherDataHttpRestApi
    extends WeatherDataJsonSupport
    with WeatherDataService {

  import weatherdata.model.CityKeyList

  lazy val citiesRoute: Route =
    pathPrefix("weatherdata") {
      path("cities") {
        post {
          entity(as[CityKeyList]) { cityKeyList =>
            onComplete(getCitiesWeather(cityKeyList)) {
              case Success(cityWeatherList) =>
                complete(cityWeatherList)
              case Failure(exception) =>
                complete(
                  HttpResponse(StatusCodes.InternalServerError,
                               entity = s"Exception: $exception")
                )
            }
          }
        }
      }
    }
}
