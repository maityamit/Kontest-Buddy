package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.CurrentFriends.LeetCodeCurrentFrndAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Services.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class LeaderboardActivity : AppCompatActivity() {

    lateinit var leetcode_leaderboard_edit_friends_button: CardView
    lateinit var leetcode_show_leaderboard_button: CardView
    lateinit var codeforces_leaderboard_edit_friends_button: CardView
    lateinit var codeforces_show_leaderboard_button: CardView
    lateinit var codechef_leaderboard_edit_friends_button: CardView
    lateinit var codechef_show_leaderboard_button: CardView
    private lateinit var superbase: SupabaseClient
    private var user_email_string: String = ""

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        initalizations()

        val sharedPreferences = this.getSharedPreferences(
            "userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        var user_email = sharedPreferences?.getString("user_email", "")
        user_email_string = user_email.toString()

        leetcode_leaderboard_edit_friends_button.setOnClickListener {
            CustomDialog3(this).show()
        }

        codeforces_leaderboard_edit_friends_button.setOnClickListener {
            CustomDialog4(this).show()
        }

        codechef_leaderboard_edit_friends_button.setOnClickListener {
            CustomDialog5(this).show()
        }

        leetcode_show_leaderboard_button.setOnClickListener {
            progressDialog.show()

            CoroutineScope(Dispatchers.IO).launch {
                val leader_board_names = leader_board_data_fetch(user_email_string, "lc_leader")

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LeaderboardActivity, leader_board_names, Toast.LENGTH_SHORT)
                        .show()

                    if (leader_board_names != "") {
                        progressDialog.dismiss()
                        val intent: Intent =
                            Intent(this@LeaderboardActivity, CommonRankingActivity::class.java)
                        intent.putExtra("userNames", leader_board_names)
                        intent.putExtra("type", "leetcode")
                        startActivity(intent)
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@LeaderboardActivity,
                            "You have not any friends !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        codeforces_show_leaderboard_button.setOnClickListener {
            progressDialog.show()
            CoroutineScope(Dispatchers.IO).launch {
                val leader_board_names = leader_board_data_fetch(user_email_string, "cf_leader")
                withContext(Dispatchers.Main) {
                    if (leader_board_names != "") {
                        progressDialog.dismiss()
                        val intent: Intent =
                            Intent(this@LeaderboardActivity, CommonRankingActivity::class.java)
                        intent.putExtra("userNames", leader_board_names)
                        intent.putExtra("type", "codeforces")
                        startActivity(intent)
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@LeaderboardActivity,
                            "You have not any friends !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        codechef_show_leaderboard_button.setOnClickListener {
            progressDialog.show()
            CoroutineScope(Dispatchers.IO).launch {
                var leader_board_names = leader_board_data_fetch(user_email_string, "cc_leader")
                withContext(Dispatchers.Main) {
                    if (leader_board_names != "") {
                        progressDialog.dismiss()
                        val intent: Intent =
                            Intent(this@LeaderboardActivity, CommonRankingActivity::class.java)
                        intent.putExtra("userNames", leader_board_names)
                        intent.putExtra("type", "codechef")
                        startActivity(intent)
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@LeaderboardActivity,
                            "You have not any friends !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun initalizations() {
        superbase = createSupabaseClient(
            supabaseUrl = Constants.SUPERBASE_URL,
            supabaseKey = Constants.SUPERBASE_KEY
        ) {
            install(Postgrest)
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Free services")

        leetcode_leaderboard_edit_friends_button = findViewById(
            R.id.leetcode_leaderboard_edit_friends_button
        )
        leetcode_show_leaderboard_button = findViewById(R.id.leetcode_show_leaderboard_button)
        codeforces_leaderboard_edit_friends_button = findViewById(
            R.id.codeforces_leaderboard_edit_friends_button
        )
        codeforces_show_leaderboard_button = findViewById(R.id.codeforces_show_leaderboard_button)
        codechef_leaderboard_edit_friends_button = findViewById(
            R.id.codechef_leaderboard_edit_friends_button
        )
        codechef_show_leaderboard_button = findViewById(R.id.codechef_show_leaderboard_button)
    }

    class CustomDialog3(context: Context) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(this.context)
        private val leaderboardActivity = context as LeaderboardActivity

        @SuppressLint("NotifyDataSetChanged")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.leetcode_leaderboard_dialoguebox)

            val current_user_list_show_recyclerView_leetcode: RecyclerView = findViewById(
                R.id.current_user_list_show_recyclerView_leetcode
            )
            progressDialog.show()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var LCleader = leaderboardActivity.leader_board_data_fetch(
                        leaderboardActivity.user_email_string,
                        "lc_leader"
                    )
                    withContext(Dispatchers.Main) {
                        if (LCleader.isBlank()) {
                            Toast.makeText(
                                context,
                                "You haven't any friends yet!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val list: MutableList<String> = mutableListOf()
                            var temp: String = ""
                            if (LCleader != "") {
                                for (ele in LCleader) {
                                    if (ele == ';') {
                                        list.add(temp)
                                        temp = ""
                                    } else {
                                        temp += ele
                                    }
                                }
                            }
                            val adapter = LeetCodeCurrentFrndAdapter(list)
                            current_user_list_show_recyclerView_leetcode.layoutManager =
                                LinearLayoutManager(
                                    context
                                )
                            current_user_list_show_recyclerView_leetcode.adapter = adapter
                            adapter.notifyDataSetChanged()
                            progressDialog.dismiss()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressDialog.dismiss()
                        println("Error updating user: ${e.message}")
                    }
                }
            }

            val leetcode_leaderboard_inputUser1: EditText = findViewById(
                R.id.leetcode_leaderboard_inputUser1
            )
            val leetcode_leaderboard_inputUser1_Verify: TextView = findViewById(
                R.id.leetcode_leaderboard_inputUser1_Verify
            )
            val leetcode_leaderboard_all_frinds_show: TextView = findViewById(
                R.id.show_leetcode_current_friends
            )

            leetcode_leaderboard_inputUser1_Verify.setOnClickListener {
                val strUser: String = leetcode_leaderboard_inputUser1.text.toString()
                if (strUser.isBlank()) {
                    Toast.makeText(context, "Enter the UserName", Toast.LENGTH_SHORT).show()
                } else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if (isValid?.get("isValid")?.asString.toString() == "true") {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    var LCleader = leaderboardActivity.leader_board_data_fetch(
                                        leaderboardActivity.user_email_string,
                                        "lc_leader"
                                    )
                                    withContext(Dispatchers.Main) {
                                        if (LCleader.isBlank() == true) {
                                            LCleader = strUser + ";"
                                        } else if (LCleader.contains(strUser) == false) {
                                            LCleader = LCleader + strUser + ";"
                                        }
                                    }
                                    leaderboardActivity.leader_board_update_data(
                                        leaderboardActivity.user_email_string,
                                        "lc_leader",
                                        LCleader
                                    )

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "User Added,",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        leetcode_leaderboard_inputUser1.text = null
                                        progressDialog.dismiss()
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        progressDialog.dismiss()
                                        println("Error updating user: ${e.message}")
                                    }
                                }
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                context,
                                "This UserName is not valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    apiTask.execute(strUser, "isLCExist")
                }
            }

            val leetcode_leaderboard_inputUser2: EditText = findViewById(
                R.id.leetcode_leaderboard_inputUser2
            )
            val leetcode_leaderboard_inputUser2_Verify: TextView = findViewById(
                R.id.leetcode_leaderboard_inputUser2_verify
            )
            leetcode_leaderboard_inputUser2_Verify.setOnClickListener {
                var strUser: String = leetcode_leaderboard_inputUser2.text.toString()
                if (strUser.isBlank()) {
                    Toast.makeText(context, "Enter the UserName", Toast.LENGTH_SHORT).show()
                } else {
                    strUser += ";"
                    progressDialog.show()
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var LCleader = leaderboardActivity.leader_board_data_fetch(
                                leaderboardActivity.user_email_string,
                                "lc_leader"
                            )
                            withContext(Dispatchers.Main) {
                                if (LCleader.isNotEmpty()) {
                                    LCleader = LCleader.replace(strUser, "")
                                }
                            }
                            leaderboardActivity.leader_board_update_data(
                                leaderboardActivity.user_email_string,
                                "lc_leader",
                                LCleader
                            )
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "User Removed,",
                                    Toast.LENGTH_SHORT
                                ).show()
                                leetcode_leaderboard_inputUser2.text = null
                                progressDialog.dismiss()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                progressDialog.dismiss()
                                println("Error updating user: ${e.message}")
                            }
                        }
                    }
                }
            }

            leetcode_leaderboard_all_frinds_show.setOnClickListener {
                progressDialog.show()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        var LCleader = leaderboardActivity.leader_board_data_fetch(
                            leaderboardActivity.user_email_string,
                            "lc_leader"
                        )

                        withContext(Dispatchers.Main) {
                            if (LCleader.isBlank()) {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    context,
                                    "You haven't any friends yet!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val list: MutableList<String> = mutableListOf()
                                var temp: String = ""
                                if (LCleader != "") {
                                    for (ele in LCleader) {
                                        if (ele == ';') {
                                            list.add(temp)
                                            temp = ""
                                        } else {
                                            temp += ele
                                        }
                                    }
                                }
                                val adapter = LeetCodeCurrentFrndAdapter(list)
                                current_user_list_show_recyclerView_leetcode.layoutManager =
                                    LinearLayoutManager(
                                        context
                                    )
                                current_user_list_show_recyclerView_leetcode.adapter = adapter
                                adapter.notifyDataSetChanged()
                                progressDialog.dismiss()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            progressDialog.dismiss()
                            println("Error updating user: ${e.message}")
                        }
                    }
                }
            }
        }

        override fun dismiss() {
            super.dismiss()
            progressDialog.dismiss()
        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }

    class CustomDialog4(context: Context) : Dialog(context) {

        private val leaderboardActivity = context as LeaderboardActivity
        var progressDialog: ProgressDialog = ProgressDialog(this.context)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.codeforces_leaderboard_dialoguebox)

            val current_user_list_show_recyclerView_codeforces: RecyclerView = findViewById(
                R.id.current_user_list_show_recyclerView_codeforces
            )
            progressDialog.show()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var LCleader = leaderboardActivity.leader_board_data_fetch(
                        leaderboardActivity.user_email_string,
                        "cf_leader"
                    )
                    withContext(Dispatchers.Main) {
                        if (LCleader.isBlank()) {
                            Toast.makeText(
                                context,
                                "You haven't any friends yet!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val list: MutableList<String> = mutableListOf()
                            var temp: String = ""
                            if (LCleader != "") {
                                for (ele in LCleader) {
                                    if (ele == ';') {
                                        list.add(temp)
                                        temp = ""
                                    } else {
                                        temp += ele
                                    }
                                }
                            }
                            val adapter = LeetCodeCurrentFrndAdapter(list)
                            current_user_list_show_recyclerView_codeforces.layoutManager =
                                LinearLayoutManager(
                                    context
                                )
                            current_user_list_show_recyclerView_codeforces.adapter = adapter
                            adapter.notifyDataSetChanged()
                            progressDialog.dismiss()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressDialog.dismiss()
                        println("Error updating user: ${e.message}")
                    }
                }
            }

            val codeforces_leaderboard_inputUser1: EditText = findViewById(
                R.id.codeforces_leaderboard_inputUser1
            )
            val codeforces_leaderboard_inputUser1_Verify: TextView = findViewById(
                R.id.codeforces_leaderboard_inputUser1_Verify
            )
            codeforces_leaderboard_inputUser1_Verify.setOnClickListener {
                val strUser: String = codeforces_leaderboard_inputUser1.text.toString()
                if (strUser.isBlank()) {
                    Toast.makeText(context, "Enter the UserName", Toast.LENGTH_SHORT).show()
                } else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if (isValid?.get("isValid")?.asString.toString() == "true") {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    var LCleader = leaderboardActivity.leader_board_data_fetch(
                                        leaderboardActivity.user_email_string,
                                        "cf_leader"
                                    )
                                    withContext(Dispatchers.Main) {
                                        if (LCleader.isBlank() == true) {
                                            LCleader = strUser + ";"
                                        } else if (LCleader.contains(strUser) == false) {
                                            LCleader = LCleader + strUser + ";"
                                        }
                                    }
                                    leaderboardActivity.leader_board_update_data(
                                        leaderboardActivity.user_email_string,
                                        "cf_leader",
                                        LCleader
                                    )

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "User Added,",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        codeforces_leaderboard_inputUser1.text = null
                                        progressDialog.dismiss()
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        progressDialog.dismiss()
                                        println("Error updating user: ${e.message}")
                                    }
                                }
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                context,
                                "This UserName is not valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    apiTask.execute(strUser, "isCFExist")
                }
            }

            val codeforces_leaderboard_inputUser2: EditText = findViewById(
                R.id.codeforces_leaderboard_inputUser2
            )
            val codeforces_leaderboard_inputUser2_Verify: TextView = findViewById(
                R.id.codeforces_leaderboard_inputUser2_verify
            )
            codeforces_leaderboard_inputUser2_Verify.setOnClickListener {
                var strUser: String = codeforces_leaderboard_inputUser2.text.toString()
                if (strUser.isBlank()) {
                    Toast.makeText(context, "Enter the UserName", Toast.LENGTH_SHORT).show()
                } else {
                    strUser += ";"
                    progressDialog.show()
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var LCleader = leaderboardActivity.leader_board_data_fetch(
                                leaderboardActivity.user_email_string,
                                "cf_leader"
                            )
                            withContext(Dispatchers.Main) {
                                if (LCleader.isNotEmpty()) {
                                    LCleader = LCleader.replace(strUser, "")
                                }
                            }
                            leaderboardActivity.leader_board_update_data(
                                leaderboardActivity.user_email_string,
                                "cf_leader",
                                LCleader
                            )
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "User Removed,",
                                    Toast.LENGTH_SHORT
                                ).show()
                                codeforces_leaderboard_inputUser2.text = null
                                progressDialog.dismiss()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                progressDialog.dismiss()
                                println("Error updating user: ${e.message}")
                            }
                        }
                    }
                }
            }

            val show_codeforces_current_friends: TextView = findViewById(
                R.id.show_codeforces_current_friends
            )
            show_codeforces_current_friends.setOnClickListener {
                progressDialog.show()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        var LCleader = leaderboardActivity.leader_board_data_fetch(
                            leaderboardActivity.user_email_string,
                            "cf_leader"
                        )

                        withContext(Dispatchers.Main) {
                            if (LCleader.isBlank()) {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    context,
                                    "You haven't any friends yet!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val list: MutableList<String> = mutableListOf()
                                var temp: String = ""
                                if (LCleader != "") {
                                    for (ele in LCleader) {
                                        if (ele == ';') {
                                            list.add(temp)
                                            temp = ""
                                        } else {
                                            temp += ele
                                        }
                                    }
                                }
                                val adapter = LeetCodeCurrentFrndAdapter(list)
                                current_user_list_show_recyclerView_codeforces.layoutManager =
                                    LinearLayoutManager(
                                        context
                                    )
                                current_user_list_show_recyclerView_codeforces.adapter = adapter
                                adapter.notifyDataSetChanged()
                                progressDialog.dismiss()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            progressDialog.dismiss()
                            println("Error updating user: ${e.message}")
                        }
                    }
                }
            }
        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }

    class CustomDialog5(context: Context) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(this.context)
        private val leaderboardActivity = context as LeaderboardActivity

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.codechef_leaderboard_dialoguebox)

            val current_user_list_show_recyclerView_codeforces: RecyclerView = findViewById(
                R.id.current_user_list_show_recyclerView_codechef
            )
            progressDialog.show()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var LCleader = leaderboardActivity.leader_board_data_fetch(
                        leaderboardActivity.user_email_string,
                        "cc_leader"
                    )
                    withContext(Dispatchers.Main) {
                        if (LCleader.isBlank()) {
                            Toast.makeText(
                                context,
                                "You haven't any friends yet!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val list: MutableList<String> = mutableListOf()
                            var temp: String = ""
                            if (LCleader != "") {
                                for (ele in LCleader) {
                                    if (ele == ';') {
                                        list.add(temp)
                                        temp = ""
                                    } else {
                                        temp += ele
                                    }
                                }
                            }
                            val adapter = LeetCodeCurrentFrndAdapter(list)
                            current_user_list_show_recyclerView_codeforces.layoutManager =
                                LinearLayoutManager(
                                    context
                                )
                            current_user_list_show_recyclerView_codeforces.adapter = adapter
                            adapter.notifyDataSetChanged()
                            progressDialog.dismiss()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressDialog.dismiss()
                        println("Error updating user: ${e.message}")
                    }
                }
            }

            val codeforces_leaderboard_inputUser1: EditText = findViewById(
                R.id.codechef_leaderboard_inputUser1
            )
            val codeforces_leaderboard_inputUser1_Verify: TextView = findViewById(
                R.id.codechef_leaderboard_inputUser1_Verify
            )
            codeforces_leaderboard_inputUser1_Verify.setOnClickListener {
                val strUser: String = codeforces_leaderboard_inputUser1.text.toString()
                if (strUser.isBlank()) {
                    Toast.makeText(context, "Enter the UserName", Toast.LENGTH_SHORT).show()
                } else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if (isValid?.get("isValid")?.asString.toString() == "true") {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    var LCleader = leaderboardActivity.leader_board_data_fetch(
                                        leaderboardActivity.user_email_string,
                                        "cc_leader"
                                    )
                                    withContext(Dispatchers.Main) {
                                        if (LCleader.isBlank() == true) {
                                            LCleader = strUser + ";"
                                        } else if (LCleader.contains(strUser) == false) {
                                            LCleader = LCleader + strUser + ";"
                                        }
                                    }
                                    leaderboardActivity.leader_board_update_data(
                                        leaderboardActivity.user_email_string,
                                        "cc_leader",
                                        LCleader
                                    )

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "User Added,",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        codeforces_leaderboard_inputUser1.text = null
                                        progressDialog.dismiss()
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        progressDialog.dismiss()
                                        println("Error updating user: ${e.message}")
                                    }
                                }
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                context,
                                "This UserName is not valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    apiTask.execute(strUser, "isCCExist")
                }
            }

            val codeforces_leaderboard_inputUser2: EditText = findViewById(
                R.id.codechef_leaderboard_inputUser2
            )
            val codeforces_leaderboard_inputUser2_Verify: TextView = findViewById(
                R.id.codechef_leaderboard_inputUser2_verify
            )
            codeforces_leaderboard_inputUser2_Verify.setOnClickListener {
                var strUser: String = codeforces_leaderboard_inputUser2.text.toString()
                if (strUser.isBlank()) {
                    Toast.makeText(context, "Enter the UserName", Toast.LENGTH_SHORT).show()
                } else {
                    strUser += ";"
                    progressDialog.show()
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var LCleader = leaderboardActivity.leader_board_data_fetch(
                                leaderboardActivity.user_email_string,
                                "cc_leader"
                            )
                            withContext(Dispatchers.Main) {
                                if (LCleader.isNotEmpty()) {
                                    LCleader = LCleader.replace(strUser, "")
                                }
                            }
                            leaderboardActivity.leader_board_update_data(
                                leaderboardActivity.user_email_string,
                                "cc_leader",
                                LCleader
                            )
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "User Removed,",
                                    Toast.LENGTH_SHORT
                                ).show()
                                codeforces_leaderboard_inputUser2.text = null
                                progressDialog.dismiss()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                progressDialog.dismiss()
                                println("Error updating user: ${e.message}")
                            }
                        }
                    }
                }
            }

            val show_codeforces_current_friends: TextView = findViewById(
                R.id.show_codechef_current_friends
            )
            show_codeforces_current_friends.setOnClickListener {
                progressDialog.show()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        var LCleader = leaderboardActivity.leader_board_data_fetch(
                            leaderboardActivity.user_email_string,
                            "cc_leader"
                        )

                        withContext(Dispatchers.Main) {
                            if (LCleader.isBlank()) {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    context,
                                    "You haven't any friends yet!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val list: MutableList<String> = mutableListOf()
                                var temp: String = ""
                                if (LCleader != "") {
                                    for (ele in LCleader) {
                                        if (ele == ';') {
                                            list.add(temp)
                                            temp = ""
                                        } else {
                                            temp += ele
                                        }
                                    }
                                }
                                val adapter = LeetCodeCurrentFrndAdapter(list)
                                current_user_list_show_recyclerView_codeforces.layoutManager =
                                    LinearLayoutManager(
                                        context
                                    )
                                current_user_list_show_recyclerView_codeforces.adapter = adapter
                                adapter.notifyDataSetChanged()
                                progressDialog.dismiss()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            progressDialog.dismiss()
                            println("Error updating user: ${e.message}")
                        }
                    }
                }
            }
        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }

    private suspend fun leader_board_data_fetch(
        user_email_string: String,
        paramter: String
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                val user = superbase.postgrest.from("users")
                    .select("user_email, $paramter") {
                        eq("user_email", user_email_string)
                    }.body.jsonArray[0].jsonObject.get(paramter)

                var LCleader = user?.jsonPrimitive?.contentOrNull
                LCleader.toString()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LeaderboardActivity,
                        "Error fetching users: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ""
            }
        }
    }

    private suspend fun leader_board_update_data(
        user_email_string: String,
        paramter: String,
        value: String
    ) {
        return withContext(Dispatchers.IO) {
            try {
                superbase.postgrest.from("users")
                    .update({
                        set(paramter, value)
                    }) {
                        eq(
                            "user_email",
                            user_email_string
                        )
                    }.body
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LeaderboardActivity,
                        "Error fetching users: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
