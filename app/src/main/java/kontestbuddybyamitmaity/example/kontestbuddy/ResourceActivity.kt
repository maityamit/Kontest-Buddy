package kontestbuddybyamitmaity.example.kontestbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.Ranking.CFCustomAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.Ranking.CFrankingActivity

class ResourceActivity : AppCompatActivity() {

    lateinit var recyclerView:RecyclerView
    private val userList = mutableListOf<CustomData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource)
        recyclerView = findViewById(R.id.dsa_cp_resources)

        userList.add(CustomData("Striver A2Z Sheet","https://takeuforward.org/strivers-a2z-dsa-course/strivers-a2z-dsa-course-sheet-2/"))
        userList.add(CustomData("Striver SDE Sheet","https://takeuforward.org/interviews/strivers-sde-sheet-top-coding-interview-problems"))
        userList.add(CustomData("Striver 79 Sheet","https://takeuforward.org/interview-sheets/strivers-79-last-moment-dsa-sheet-ace-interviews"))
        userList.add(CustomData("Blind 75","https://takeuforward.org/interviews/blind-75-leetcode-problems-detailed-video-solutions"))
        userList.add(CustomData("450 - DSA Love Babbar","https://450dsa.com/"))
        userList.add(CustomData("Fraz DSA Sheet","https://www.naukri.com/code360/problem-lists/mohammad-fraz-dsa-sheet-problems"))
        userList.add(CustomData("Apna College DSA Sheet","https://algolisted.com/coding-sheets/apna-college"))
        userList.add(CustomData("NeetCode 150","https://neetcode.io/practice"))
        userList.add(CustomData("NeetCode 400","https://neetcode.io/practice"))

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ResourceCustomAdapter(this,userList)




    }

    data class CustomData(
        val name: String,
        val link: String
    )
}