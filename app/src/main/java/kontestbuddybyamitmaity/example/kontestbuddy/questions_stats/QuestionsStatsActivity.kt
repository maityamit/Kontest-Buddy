package kontestbuddybyamitmaity.example.kontestbuddy.questions_stats

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParser
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.MainActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R

class QuestionsStatsActivity : AppCompatActivity() {

    lateinit var qns_stat_type_text:TextView
    private lateinit var progressDialog: ProgressDialog
    private val userList = mutableListOf<CustomData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_stats)

        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Free services")

        qns_stat_type_text = findViewById(R.id.qns_stat_type_text)

        val extras = intent.extras
        val statType = extras?.getString("type")

        if (statType != null) {
            qns_stat_type_text.text = statType+" Stats"
        }

        val sharedPreferences = this.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        var LCuserName = sharedPreferences?.getString("LCuserName",null)


        if(LCuserName != null && statType!= null){
            LCuserName = LCuserName.substring(1,LCuserName.length-1)
            callTheAPI(LCuserName, statType)
        }else{
            Toast.makeText(this,"Please Set LeetCode User Name First",Toast.LENGTH_SHORT).show()
            val intent:Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun callTheAPI(LCuserName: String, statType: String) {
        progressDialog.show()
        val apiTask = APIServices { isValid ->

//            val parser = JsonParser()
//            val jsonObj = parser.parse(isValid).asJsonObject
//            val daily_json_obj = jsonObj.get("dailySolved").asJsonObject
//            val monthly_json_obj = jsonObj.get("monthlySolved").asJsonObject
//            val yearly_json_obj = jsonObj.get("yearlySolved").asJsonObject
//
//            if (statType == "Daily") {
//                for (element in daily_json_obj.entrySet()) {
//                    userList.add(CustomData(element.key, element.value.asInt))
//                }
//            }else if (statType == "Monthly") {
//                for (element in monthly_json_obj.entrySet()) {
//                    userList.add(CustomData(element.key, element.value.asInt))
//                }
//            }else if (statType == "Yearly") {
//                for (element in yearly_json_obj.entrySet()) {
//                    userList.add(CustomData(element.key, element.value.asInt))
//                }
//            }

            userList.reverse()
            val recyclerView: RecyclerView = findViewById(R.id.questions_stats_recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = QuestionStatAdapter(userList)
            progressDialog.dismiss()

        }
        apiTask.execute(LCuserName)
    }

    data class CustomData(
        val date: String,
        val no_of_qns: Int
    )

}