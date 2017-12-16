package weatherdata.actors

import akka.actor.{Actor, ActorSelection}
import akka.event.Logging
import akka.util.Timeout

import scala.concurrent.duration._
import weatherdata.WeatherDataConf

trait ImplicitActor extends Actor {

  implicit val actorSystem = context.system
  implicit val timeout = Timeout(WeatherDataConf.requestTimeout seconds)
  implicit val executionContext = actorSystem.dispatcher

  val logger = Logging(actorSystem, this)
}
