package hfad.com.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

/*
    repository does two things
    1. loads data
    2. providing that data out to the activity

    we need to define how that data will get out to our activity
 */

class ForecastRepository {

    //LiveData is a templated class meaning it can hold different types of data, we need to tell it what types of data we want it to hold
    //We define a private read only variable that will hold a list of DailyForecast items

    private val _weeklyForecast = MutableLiveData<List<DailyForecast>>() //private Mutable, so our repo can change it
    val weeklyForecast: LiveData<List<DailyForecast>> = _weeklyForecast //public immutable for our activity

    //weeklyForecast is public which means our activity can get access to it.
    // And is LiveData type. This means our activity can get updates to it but the activity cannot publish its own changes to it.
    //This is very important because we want the repository to be the only place that can modify data

    //1. We have a way to update the data now we need a way to update it
    fun loadForecast(zipcode: String){

        //2. Create a list of size 7 and use an intializer for it {}
        val randomValues = List(7){ Random.nextFloat().rem(100) * 100} //range for random nums is 0 - 100, This will be our temp values

            //.map allows us to take in a type and make the output a different type. its a hash table we asign keys to our values in list
        val forecastItems = randomValues.map{temp ->
            DailyForecast(temp, getTempDescription(temp))

        }
        _weeklyForecast.setValue(forecastItems) //send this list to live data. setValue lets us update whatever value is held by live data
        //weeklyForecast is set to _weeklyForecast so it is also updated, so our activity can observe the change
        //3. now we can go to main activity and start observing these values

    }

    private fun getTempDescription(temp: Float) : String {
        return if(temp < 75) "Its too cold" else "Its great"
    }
}


