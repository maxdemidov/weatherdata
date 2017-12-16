package weatherdata.actors

import akka.actor.{PoisonPill, Props}
import akka.pattern.ask
import weatherdata.model.{CityKey, CityLocation, CityWeather, CityWeatherData}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object CitiesWeatherActor {
  sealed trait CitiesWeatherRequest
  case class GetCitiesWeatherByKeys(citiesKeys: List[CityKey])
      extends CitiesWeatherRequest

  sealed trait CitiesWeatherResponse
  case class CitiesWeatherResult(citiesWeather: List[CityWeather])
      extends CitiesWeatherResponse
  case class CitiesWeatherError(message: String) extends CitiesWeatherResponse
}
class CitiesWeatherActor extends ImplicitActor {

  import CitiesWeatherActor._
  import CityWeatherActor._

  override def receive: Receive = {

    case GetCitiesWeatherByKeys(citiesKeys) =>
      val originalSender = sender
      val citiesWeatherFutures: List[Future[Either[String, CityWeather]]] =
        citiesKeys.map(cityKey => {
          val cityWeatherActorRef =
            actorSystem.actorOf(Props[CityWeatherActor])
          (cityWeatherActorRef ? GetCityWeatherByKey(cityKey))
            .mapTo[CityWeatherResponse]
            .map {
              case CityWeatherResult(cityWeatherEither) => cityWeatherEither
            }
        })
      val resultFuture: Future[(List[String], List[CityWeather])] =
        Future
          .sequence(citiesWeatherFutures)
          .map(
            citiesWeatherEither => {
              val (lefts, rights) =
                citiesWeatherEither.partition(_.isLeft)
              val messages = lefts.map(_.left.get)
              val citiesWeather =
                rights.map(_.right.get)
              (messages, citiesWeather)
            }
          )
      resultFuture.onComplete {
        case Success(results) =>
          val (messages, citiesWeather) = (results._1, results._2)
          for (message <- messages)
            logger.info(message)
          originalSender ! CitiesWeatherResult(citiesWeather)
        case Failure(exception) =>
          originalSender ! CitiesWeatherError(exception.getMessage)
      }
      self ! PoisonPill
  }
}
