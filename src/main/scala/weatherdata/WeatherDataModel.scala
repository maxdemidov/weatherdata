package weatherdata


case class CityKey(name: String,
                   id: Long)

case class CityKeyList(cities: List[CityKey])


case class CityWeather(cityKey: CityKey,
                       data: String)

case class CityWeatherList(citiesWeather: List[CityWeather])