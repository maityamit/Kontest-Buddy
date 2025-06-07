package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.R

class CommonRankingActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private val userList = mutableListOf<RankingModel>()
    private lateinit var common_ranking_title: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_ranking)
        initialization()

        val extras = intent.extras
        val userNames = extras?.getString("userNames")?.dropLast(1)
        val type = extras?.getString("type").toString()

        if (type.lowercase() == "leetcode") {
            callTheLeetCodeAPI(userNames)
        } else if (type.lowercase() == "codeforces") {
            callTheCodeForcesAPI(userNames)
        } else if (type.lowercase() == "codechef") {
            callTheCodeChefAPI(userNames)
        }
    }

    private fun initialization() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please wait")
        common_ranking_title = findViewById(R.id.common_ranking_title)
        recyclerView = findViewById(R.id.common_ranking_recycler_view)
    }

    private fun callTheLeetCodeAPI(userNames: String?) {
        common_ranking_title.text = "LeetCode Ranking"
        progressDialog.show()
        val apiTask = APIServices { isValid ->
            val users = isValid?.get("result")?.asJsonArray
            if (users != null) {
                for (user in users) {
                    val tempName: String =
                        user.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get(
                            "username"
                        ).toString()
                    val tempRat: String =
                        user.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get(
                            "rating"
                        ).toString().split(".")[0]
                    val tempRank: String =
                        user.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get(
                            "globalRanking"
                        ).toString()
                    userList.add(
                        RankingModel(
                            tempName.toString(),
                            tempRat.toInt().toString(),
                            tempRank.toFloat().toString(),
                            "leetcode"
                        )
                    )
                }
            }
            val sortedList = userList.sortedByDescending { it.rating }
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = CommonRankingAdapter(sortedList)
            progressDialog.dismiss()
        }
        apiTask.execute(userNames, "lcLeader")
    }

    private fun callTheCodeForcesAPI(userNames: String?) {
        common_ranking_title.text = "CodeForces Ranking"
        progressDialog.show()
        val apiTask = APIServices { isValid ->
            val users = isValid?.get("result")?.asJsonArray
            if (users != null) {
                for (user in users) {
                    val tempName: String = user.asJsonObject?.get("username").toString()
                    val tempRat: String = user.asJsonObject.get("rating").toString()
                    val tempRank: String = user.asJsonObject.get("rank").toString()
                    userList.add(
                        RankingModel(
                            tempName,
                            tempRat.toInt().toString(),
                            tempRank.toString(),
                            "codeforces"
                        )
                    )
                }
            }
            val sortedList = userList.sortedByDescending { it.rating }
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = CommonRankingAdapter(sortedList)
            progressDialog.dismiss()
        }
        apiTask.execute(userNames, "cfLeader")
    }

    private fun callTheCodeChefAPI(userNames: String?) {
        common_ranking_title.text = "CodeChef Ranking"
        progressDialog.show()
        val apiTask = APIServices { isValid ->
            val users = isValid?.get("result")?.asJsonArray
            if (users != null) {
                for (user in users) {
                    val tempName: String =
                        user.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get(
                            "username"
                        ).toString()
                    var tempRat: String =
                        user.asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get(
                            "rating"
                        ).toString()
                    tempRat = tempRat.substring(1, tempRat.length - 1)
                    userList.add(
                        RankingModel(
                            tempName.toString(),
                            tempRat.toInt().toString(),
                            "",
                            "codechef"
                        )
                    )
                }
            }
            val sortedList = userList.sortedByDescending { it.rating }
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = CommonRankingAdapter(sortedList)
            progressDialog.dismiss()
        }
        apiTask.execute(userNames, "ccLeader")
    }

    data class RankingModel(
        val name: String,
        val rating: String,
        val rank: String,
        val type: String
    )
}
