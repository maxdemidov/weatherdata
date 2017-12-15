package weatherdata

import scalaj.http.{Http, HttpResponse}
import net.liftweb.json._

object _tmp_WeatherDataMain extends App {

  // TODO - add ScalaTest and ScalaMock

  implicit val formats = DefaultFormats

  val response: HttpResponse[String] =
    Http(
      "http://samples.openweathermap.org/data/2.5/weather?zip=94040,us&appid=b6907d289e10d714a6e88b30761fae22").asString

  val json = parse(response.body)

  val base = (json \ "base").extract[String]
  val description = ((json \ "weather")(0) \ "description").extract[String]

  println(s"base = $base and description = $description")
}
