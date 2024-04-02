package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParser
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeCompareApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeLeaderApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.R

class LCrankingActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private val userList = mutableListOf<CustomData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lcranking)
        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Free services")

        initialization()
        val extras = intent.extras
        val userNames = extras?.getString("userNames")?.dropLast(1)
        callTheAPI(userNames)
    }


    private fun callTheAPI(userNames: String?) {
        progressDialog.show()
        val apiTask = LeetCodeLeaderApiTask { isValid ->

            val parser = JsonParser()
            val jsonArray = parser.parse(isValid).asJsonArray

            for(element in jsonArray){
                val tempName:String = element.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("Name").toString()
                val tempRat:String = element.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("rating").toString().split(".")[0]
                val tempRank:String = element.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("rank").toString()
                userList.add(CustomData(tempName,tempRat.toInt(),tempRank.toFloat()))
            }

            val sortedList = userList.sortedByDescending { it.rating }

            val recyclerView: RecyclerView = findViewById(R.id.lclistRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = LCCustomAdapter(sortedList)

            progressDialog.dismiss()

        }
        apiTask.execute(userNames)
    }

    private fun initialization() {

    }

    data class CustomData(
        val name: String,
        val rating: Int,
        val rank: Float
    )
}