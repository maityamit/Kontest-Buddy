package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesLeaderApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodechefLeaderApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeLeaderApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Ranking.CFrankingActivity.CustomData

class CCrankingActivity : AppCompatActivity() {


    private lateinit var progressDialog: ProgressDialog
    private val userList = mutableListOf<CustomData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ccranking)

        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Free services")

        val extras = intent.extras
        val userNames = extras?.getString("userNames")?.dropLast(1)
        callTheAPI(userNames)


    }

    private fun callTheAPI(userNames: String?) {
        progressDialog.show()
        val apiTask = CodechefLeaderApiTask { isValid ->

            val parser = JsonParser()
            val jsonArray = parser.parse(isValid).asJsonArray

            for(element in jsonArray){



                val tempName:String = element.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("username").toString()
                var tempRat:String = element.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("rating").toString()
                tempRat = tempRat.substring(1,tempRat.length-1)
                userList.add(CustomData(tempName,tempRat.toInt()))

            }

            val sortedList = userList.sortedByDescending { it.rating }

            val recyclerView: RecyclerView = findViewById(R.id.cclistRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = CCustomAdapter(sortedList)
            progressDialog.dismiss()

        }
        apiTask.execute(userNames)
    }

    data class CustomData(
        val username: String,
        val rating: Int
    )
}