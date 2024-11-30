package kontestbuddybyamitmaity.example.kontestbuddy.Agents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.Fragment.HomeFragment
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kotlin.reflect.typeOf

class DailystatshowActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private val userList = mutableListOf<CustomData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dailystatshow)

        recyclerView = findViewById(R.id.daily_stats_recycler_view)

        val dayWiseList = intent.getSerializableExtra("day_wise_list") as? ArrayList<Map<String,Map<String,Long>>>

        if (dayWiseList != null) {
            for(element in dayWiseList){
                for ((key, value) in element) {
                    userList.add(CustomData(key, value as Map<String, Long>))
                }
            }
        }


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DailyStatsCustomAdapter(this,userList)



    }

    data class CustomData(
        val date: String,
        val link_to_time_map: Map<String, *>
    )
}