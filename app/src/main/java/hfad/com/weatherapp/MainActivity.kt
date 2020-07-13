package hfad.com.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {

    //4. We need to create a new repository for our activity so it can observe the changes to our LiveData weeklyForecast. It is public and immutable
    //which means our activity can observe it but not change it.
    private val forecastRepository = ForecastRepository() //we can now reference the repo in our activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val can change var is final
        val zipcodeEditText: EditText = findViewById(R.id.et_zip_code)
        val enterButton: Button = findViewById(R.id.btnEnter)

        //notice we are using brackets instead of () here. this is known as a lambda in kotlin. I think it shortens code instead of having to write parameters it does this for us
        enterButton.setOnClickListener{
            val zipcode: String = zipcodeEditText.text.toString() //notice how we only use text, not getText()
            if(zipcode.length != 5){
                Toast.makeText(this, R.string.zipcode_entry_value_error, Toast.LENGTH_SHORT).show()

            }else{
                forecastRepository.loadForecast(zipcode) //generates random number then assigns a key to it then sets the _weeklyForecast live data
                //member which is set to the public weeklyForecast which our activity observes for changes

            }
        }

        val forecastList: RecyclerView= findViewById(R.id.main_recycler_view)
        forecastList.layoutManager = LinearLayoutManager(this)
        val dailyForecastAdapter = DailyForecastAdapter(){
            val message = getString(R.string.forecast_clicked_format, it.temp, it.description)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        forecastList.adapter = dailyForecastAdapter

        //5. we need to add an observer which will know when updates are made
        //each observer requires a lifecycle owner, which is a something that knows something about the lifecycle.
        //in this case it is the activity so we can pass this.(context)

        //this is a lamda we used -> to rename the receiver of the lambda. lambda is an anonymous function that is not bound to an identifier.
        //we can declare it inline without assigning it to a class
        val weeklyForecastObserver = Observer<List<DailyForecast>>{forecastItems ->
            //update list adapter
            dailyForecastAdapter.submitList(forecastItems) //pass the forecastItems into our adapter

        }
        //we .observe a lifecycle owner which is main activity and a weeklyForcaseObserver. this observer will be updated any time
        //our live data changes in our repository. Because we also passed in a lifecycle observer weeklyForecastObserver, all of these
        //changes will be bound to the bound to the lifecycle of the activity.
        forecastRepository.weeklyForecast.observe(this, weeklyForecastObserver) //6. update observer with weeklyForecastObserver

    }
}