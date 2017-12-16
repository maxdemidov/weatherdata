package weatherdata.model

case class CityKey(name: String, id: Long)

case class CityKeyList(citiesKeys: List[CityKey])

case class CityLocation(lon: Float, lat: Float)

case class CityWeatherData(description: String,
                           icon: String,
                           base: String,
                           windSpeed: Float,
                           windDegrees: Float,
                           temperature: Float,
                           humidity: Byte)

case class CityWeather(cityKey: CityKey,
                       countryCode: String,
                       location: CityLocation,
                       data: CityWeatherData)

case class CityWeatherList(citiesWeather: List[CityWeather])
