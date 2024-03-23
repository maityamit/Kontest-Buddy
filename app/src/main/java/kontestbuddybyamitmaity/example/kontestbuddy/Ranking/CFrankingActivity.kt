package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesLeaderApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeLeaderApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.R

class CFrankingActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private val userList = mutableListOf<CustomData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cfranking)

        progressDialog= ProgressDialog(this)
        val extras = intent.extras
        val userNames = extras?.getString("userNames")?.dropLast(1)
        callTheAPI(userNames)

    }

    private fun callTheAPI(userNames: String?) {
        progressDialog.show()
        val apiTask = CodeForcesLeaderApiTask { isValid ->

            val jsonArray: JsonArray? = isValid?.let { JsonParser().parse(it).asJsonObject }?.get("result")?.asJsonArray

            if (jsonArray != null) {
                for(element in jsonArray){
                    val tempName:String = element.asJsonObject?.get("Name").toString()
                    val tempRat:String = element.asJsonObject.get("rating").toString()
                    val tempRank:String = element.asJsonObject.get("rank").toString()
                    userList.add(CustomData(tempName,tempRat.toInt(),tempRank))
                }
            }

            val sortedList = userList.sortedByDescending { it.rating }

            val recyclerView: RecyclerView = findViewById(R.id.cflistRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = CFCustomAdapter(sortedList)

            progressDialog.dismiss()

        }
        apiTask.execute(userNames)
    }

    data class CustomData(
        val name: String,
        val rating: Int,
        val rank: String
    )
}