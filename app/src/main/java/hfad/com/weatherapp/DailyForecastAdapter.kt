package hfad.com.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
//  : after the class indicated we want the class to inherit from another

//takes a view as parameter and extends RecyclerView.ViewHolder which takes the view in its parameter for the constructor
class DailyForecastViewHolder(view: View) : RecyclerView.ViewHolder(view){

    private val tempText: TextView = view.findViewById(R.id.tvTemp) //get references to our view
    private val descriptionText: TextView = view.findViewById(R.id.tvDescription)

    fun bind(dailyForecast: DailyForecast){ //receives a daily forcase object which we can then extract the data from
        tempText.text = String.format("%.2f", dailyForecast.temp)
        descriptionText.text = dailyForecast.description.toString()
    }
}
/*
    When using list adapter class we need to define two types.
    1.The type of item that will be passed to this list adapter
    2. type of view holder that will be used to bind these items to the layouts
 */
class DailyForecastAdapter(
    private val clickHandler: (DailyForecast) -> Unit
) : ListAdapter<DailyForecast, DailyForecastViewHolder> (DIFF_CONFIG){


//companion object is like static in java. This class is related to the adapter but does not have to be declared with the adapter to be used
    companion object{

    //The = object means we are defining an anonymous inner class. Creating a new instance of an unnamed inner class
    //The colon : means it will extend another class in this case DiffUtil.ItemCallback and pass in DailyForecast as a templated type to say
    //this item call back will work on DailyForecast items

    val DIFF_CONFIG = object : DiffUtil.ItemCallback<DailyForecast>(){
        override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem === newItem //=== compares two items to see if they are exactly the same
        }

        override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem == newItem //returns true if contents of items is the same
        }

    }
    }

    //creates a new view holder, within this we create a new view to represent each item in our list
    //every time recycler view needs to create a new view holder it will call this method, which inflates the layout with our views and
    //returns the view holder with our layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return DailyForecastViewHolder(itemView)
    }

    //get each individual element of the forecast items, and pass that data along to the view holder so the view items can be updated
    //before we can do this we need to create a new layout for the list items
    //When it needs to bind the data to the view it call this method
    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        //the ListAdapter class will store the DailyForecast items we specified as its first parameter. We can get these items position to bind
        holder.bind(getItem(position))

        //we are using a lambda here
        holder.itemView.setOnClickListener{
            clickHandler(getItem(position))
        }

    }
}