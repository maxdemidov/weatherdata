package weatherdata

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import scala.concurrent.duration._

object WeatherDataBoot extends App with WeatherDataConf with WeatherDataHttpService {

  implicit val system = ActorSystem("weather-data-actor-system")
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val timeout = Timeout(10 seconds)

  Http().bindAndHandle(citiesRoute, host, port) map {
    binding =>
      println(s"Server bound to http:/${binding.localAddress}/")
  } recover {
    case exception =>
      println(s"Server could not bind to http://$host:$port",
        exception.getMessage)
  }
}
