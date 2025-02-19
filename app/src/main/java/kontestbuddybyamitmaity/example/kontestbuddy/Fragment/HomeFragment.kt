package kontestbuddybyamitmaity.example.kontestbuddy.Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeChefVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Compare.CompareActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Friends.FriendsActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Ranking.LeaderboardActivity
import kontestbuddybyamitmaity.example.kontestbuddy.questions_stats.QuestionsStatsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class HomeFragment : Fragment() {


    lateinit var userName_TextView:TextView
    lateinit var ratingsLC_TextView:TextView
    lateinit var toppercentageLC_TextView:TextView
    lateinit var livecontestLC_TextView:TextView
    lateinit var globalrankingLC_TextView:TextView
    lateinit var ratingsCF_TextView:TextView
    lateinit var ratingCC_TextView:TextView
    lateinit var lcUserName_TextView:TextView
    lateinit var cfUserName_TextView:TextView
    lateinit var ccUserName_TextView:TextView
    lateinit var lastUpdateHomeMainPage_TextView:TextView
    lateinit var mainHomePageRatingsRefresh_ImageView:ImageView
    private lateinit var progressDialog: ProgressDialog
    lateinit var LeaderBoardButton_Homepage:LinearLayout
    lateinit var compareButton_Homepage:LinearLayout
    lateinit var find_koding_buddy_button:CardView
    lateinit var daily_coding_stats_via_extension:CardView
    lateinit var monthly_coding_stats_via_extension:CardView
    lateinit var yearly_coding_stats_via_extension:CardView

    val day_wise_list: MutableList<Any> = mutableListOf()
    val week_wise_list: MutableList<Any> = mutableListOf()
    val month_wise_list: MutableList<Any> = mutableListOf()

//    lateinit var CompareButtonLeetCode:CardView
//    lateinit var CompareButtonCodeForces:CardView
    val coroutineScope = CoroutineScope(Dispatchers.Main)
//    lateinit var LeaderboardButtonLeetCode:TextView
//    lateinit var LeaderboardButtonCodeForces:TextView
//    lateinit var leetcode_leaderboard_Button:LinearLayout
//    lateinit var codeforces_leaderboard_Button:CardView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view:View  = inflater.inflate(R.layout.fragment_home, container, false)
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Free services")

        // Initialization
        initialization(view)
        allDataSettoTheUI()

        // Update the Refresh Button
        // Retrieve the details from firebase -> Call the API to Fetch Latest Data -> set into Container -> Store Local Storage
        mainHomePageRatingsRefresh_ImageView.setOnClickListener{
            progressDialog.show()
            retrieveDataFromtheFirebase()
        }

        LeaderBoardButton_Homepage.setOnClickListener {
            val intent:Intent = Intent(context,LeaderboardActivity::class.java)
            startActivity(intent)
        }
        compareButton_Homepage.setOnClickListener{
            val intent:Intent = Intent(context,CompareActivity::class.java)
            startActivity(intent)
        }

        daily_coding_stats_via_extension.setOnClickListener {
            val intent:Intent = Intent(context, QuestionsStatsActivity::class.java)
            intent.putExtra("type","Daily")
            startActivity(intent)
        }

        monthly_coding_stats_via_extension.setOnClickListener {
            val intent:Intent = Intent(context, QuestionsStatsActivity::class.java)
            intent.putExtra("type","Monthly")
            startActivity(intent)
        }

        yearly_coding_stats_via_extension.setOnClickListener {
            val intent:Intent = Intent(context, QuestionsStatsActivity::class.java)
            intent.putExtra("type","Yearly")
            startActivity(intent)
        }


        find_koding_buddy_button.setOnClickListener {
            val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
                AppCompatActivity.MODE_PRIVATE
            )
            val _is_public = sharedPreferences?.getString("is_public","false")
            if(_is_public=="true"){
                val intent:Intent = Intent(context, FriendsActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(context, "Please make your profile public to use this feature, for this go to Profile Page", Toast.LENGTH_LONG).show()
            }
        }


        return view
    }


    private fun allDataSettoTheUI() {
        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )



        val userName = sharedPreferences?.getString("userName","")
        val userEmail = sharedPreferences?.getString("userEmail","")
        val LCuserName = sharedPreferences?.getString("LCuserName","")
        val LCattendedContestsCount = sharedPreferences?.getString("LCattendedContestsCount","")
        val LCrating = sharedPreferences?.getString("LCrating","")
        val LCglobalRanking = sharedPreferences?.getString("LCglobalRanking","")
        val LCtotalParticipants = sharedPreferences?.getString("LCtotalParticipants","")
        val LCtopPercentage = sharedPreferences?.getString("LCtopPercentage","")
        val CFuserName = sharedPreferences?.getString("CFuserName","")
        val CFrating = sharedPreferences?.getString("CFrating","")
        val CFmaxRating = sharedPreferences?.getString("CFmaxRating","")
        val CFrank = sharedPreferences?.getString("CFrank","")
        val CFmaxRank = sharedPreferences?.getString("CFmaxRank","")
        val CCuserName = sharedPreferences?.getString("CCuserName","")
        val CCrating = sharedPreferences?.getString("CCrating","")
        val lastUpdate = sharedPreferences?.getString("lastUpdate","")



        //Retrieve Data From Local Storage and Set Data into The Container
        if (userName != null) {
            userName_TextView.text = "\uD83D\uDC4B Hello, " + userName.split(" ")[0]
        }else{
            userName_TextView.text = "\uD83D\uDC4B Hello, "
        }

        ratingsLC_TextView.text = if(LCrating =="null" || LCrating=="") {"-"} else {LCrating?.substring(1, 5) ?: ""}
        toppercentageLC_TextView.text = if (LCtopPercentage == "null" || LCtopPercentage=="") {"-"} else {(LCtopPercentage?.substring(1, LCtopPercentage.length - 1) + "%") } ?: ""
        livecontestLC_TextView.text = if(LCattendedContestsCount == "null" || LCattendedContestsCount=="") {"-"} else {LCattendedContestsCount?.substring(1, LCattendedContestsCount.length - 1) ?: ""}
        globalrankingLC_TextView.text = if(LCglobalRanking == "null" || LCglobalRanking=="") {"-"} else {LCglobalRanking?.substring(1, LCglobalRanking.length - 1) ?: ""}

        ratingsCF_TextView.text = if(CFrating == "null" || CFrating=="") {"-"} else {CFrating?.substring(1, CFrating.length - 1) ?: "" }
        ratingCC_TextView.text = if(CCrating == "null" || CCrating=="") {"-"} else {CCrating?.substring(1, CCrating.length - 1) ?: "" }
        lcUserName_TextView.text = if(LCuserName == "null" || LCuserName=="") {"-"} else {LCuserName?.substring(1, LCuserName.length - 1) ?: "" }
        cfUserName_TextView.text = if(CFuserName == "null" || CFuserName=="") {"-"} else {CFuserName?.substring(1, CFuserName.length - 1) ?: "" }


        ccUserName_TextView.text = if(CCuserName == "null" || CCuserName=="") {"-"} else {CCuserName?.substring(1, CCuserName.length - 1) ?: "" }
        lastUpdateHomeMainPage_TextView.text = lastUpdate


    }

    private fun retrieveDataFromtheFirebase() {

        val db = Firebase.firestore
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val userID = auth.currentUser?.uid.toString()
        val docRef = db.collection("users").document(userID)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    val userCodechef = document.getString("userCodechef")
                    val userCodeforces = document.getString("userCodeforces")
                    val userLeetcode = document.getString("userLeetcode")
                    val userName = document.getString("userName")
                    val userEmail = document.getString("userEmail")

                    retrieveDataUSingAPI(userLeetcode,userCodeforces,userCodechef)

                } else {
                    Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun retrieveDataUSingAPI(userLeetcode: String?, userCodeforces: String?, userCodechef: String?) {

        Log.e("RetriveUserNAME_FROM_FIREBASE", "${userLeetcode} + ${userCodeforces} + ${userCodechef} ")

        callApiTasks(
            userLeetCode = userLeetcode,
            userCodeforces = userCodeforces,
            userCodechef = userCodechef,
            onAllApiResultsAvailable = {
                progressDialog.dismiss()
                allDataSettoTheUI()
            }
        )

    }

    private fun callApiTasks(
        userLeetCode: String?,
        userCodeforces: String?,
        userCodechef: String?,
        onAllApiResultsAvailable: () -> Unit
    ) {
        coroutineScope.launch {

            val resultLeetCode = async { LeetCodeVerifyApiTask{}.execute(userLeetCode).get() }
            val resultCodeforces = async { CodeForcesVerifyApiTask{}.execute(userCodeforces).get() }
            val resultCodeChef = async { CodeChefVerifyApiTask{}.execute(userCodechef).get() }
            val allResults = awaitAll(resultLeetCode, resultCodeforces, resultCodeChef)
            Log.e("DataAmit", "${allResults}")
            handleAllApiResults(allResults)
            onAllApiResultsAvailable()
        }
    }

    private fun handleAllApiResults(results: List<JsonObject?>) {
        // Handle individual API results here
        val resultLeetCode = results[0]
        val resultCodeforces = results[1]
        val resultCodeChef = results[2]

        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        val myEdit = sharedPreferences?.edit()

        val currentTimestamp: Long = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yy - hh:mm a", Locale.getDefault())

        if(myEdit!=null){
            myEdit.putString("lastUpdate",dateFormat.format(Date(currentTimestamp)))


            myEdit.putString("LCuserName", resultLeetCode?.get("userName").toString())
            myEdit.putString("LCattendedContestsCount",resultLeetCode?.get("attendedContestsCount").toString())
            myEdit.putString("LCrating",resultLeetCode?.get("rating").toString())
            myEdit.putString("LCglobalRanking",resultLeetCode?.get("globalRanking").toString())
            myEdit.putString("LCtotalParticipants",resultLeetCode?.get("totalParticipants").toString())
            myEdit.putString("LCtopPercentage",resultLeetCode?.get("topPercentage").toString())

            myEdit.putString("CFuserName",resultCodeforces?.get("userName").toString())
            myEdit.putString("CFrating",resultCodeforces?.get("rating").toString())
            myEdit.putString("CFmaxRating",resultCodeforces?.get("maxRating").toString())
            myEdit.putString("CFrank",resultCodeforces?.get("rank").toString())
            myEdit.putString("CFmaxRank",resultCodeforces?.get("maxRank").toString())

            myEdit.putString("CCuserName",resultCodeChef?.get("userName").toString())
            myEdit.putString("CCrating",resultCodeChef?.get("rating").toString())

            myEdit.apply()
        }

        Toast.makeText(context, "Data refreshed !! at ${dateFormat.format(Date(currentTimestamp))}", Toast.LENGTH_SHORT).show()



    }

    private fun initialization(view: View) {
        userName_TextView = view.findViewById(R.id.userNameinMainPage);
        ratingsLC_TextView = view.findViewById(R.id.lcUserRatingsMainPage);
        toppercentageLC_TextView = view.findViewById(R.id.lcUsertopMainPage);
        livecontestLC_TextView = view.findViewById(R.id.lcUsercontestMainPage);
        globalrankingLC_TextView = view.findViewById(R.id.lcUserglobalMainPage);
        ratingsCF_TextView  = view.findViewById(R.id.cfUserRatingsMainPage);
        ratingCC_TextView  = view.findViewById(R.id.ccUserRatingsMainPage);
        lcUserName_TextView  = view.findViewById(R.id.lcUserNameMainPage);
        cfUserName_TextView  = view.findViewById(R.id.cfUserNameMainPage);
        ccUserName_TextView  = view.findViewById(R.id.ccUserNameMainPage);
        lastUpdateHomeMainPage_TextView = view.findViewById(R.id.lastUpdateHomeMainPage)
        mainHomePageRatingsRefresh_ImageView = view.findViewById(R.id.mainHomePageRatingsRefresh)
        LeaderBoardButton_Homepage = view.findViewById(R.id.leaderBoardButton_Homepage)
        compareButton_Homepage = view.findViewById(R.id.compareButton_Homepage)
        find_koding_buddy_button = view.findViewById<CardView>(R.id.find_koding_buddy_button)
        daily_coding_stats_via_extension = view.findViewById<CardView>(R.id.daily_coding_stats_via_extension)
        monthly_coding_stats_via_extension = view.findViewById<CardView>(R.id.monthly_coding_stats_via_extension)
        yearly_coding_stats_via_extension = view.findViewById<CardView>(R.id.yearly_coding_stats_via_extension)
    }


}