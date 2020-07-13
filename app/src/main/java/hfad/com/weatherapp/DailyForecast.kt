package hfad.com.weatherapp


//Data classes require you to define at least a single property
//a property is a variable of the class, notice we use () with the data class
data class DailyForecast(
    val temp: Float,
    val description: String  //read only property of the data class for temp and description
)