package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParser
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.R

class LCrankingActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private val userList = mutableListOf<CustomData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lcranking)

        initialization()
        val extras = intent.extras
        val userNames = extras?.getString("userNames")?.dropLast(1)
        callTheAPI(userNames)
    }


    private fun callTheAPI(userNames: String?) {
        progressDialog.show()
        val apiTask = APIServices { isValid ->

            val users = isValid?.get("result")?.asJsonArray

            if (users != null) {
                for(user in users){
                    val tempName:String = user.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("username").toString()
                val tempRat:String = user.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("rating").toString().split(".")[0]
                val tempRank:String = user.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("globalRanking").toString()
                userList.add(CustomData(tempName,tempRat.toInt(),tempRank.toFloat()))
                }
            }

            val sortedList = userList.sortedByDescending { it.rating }
            val recyclerView: RecyclerView = findViewById(R.id.lclistRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = LCCustomAdapter(sortedList)
            progressDialog.dismiss()

        }
        apiTask.execute(userNames, "lcLeader")

    }

    private fun initialization() {
        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please wait")
    }

    data class CustomData(
        val name: String,
        val rating: Int,
        val rank: Float
    )
}