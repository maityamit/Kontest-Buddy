package kontestbuddybyamitmaity.example.kontestbuddy.Fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.Compare.CompareActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Friends.FriendsActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Ranking.LeaderboardActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Services.Constants
import kontestbuddybyamitmaity.example.kontestbuddy.questions_stats.QuestionsStatsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
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
    private lateinit var superbase: SupabaseClient


    val coroutineScope = CoroutineScope(Dispatchers.Main)




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view:View  = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialization
        initialization(view)
        allDataSettoTheUI()
        actions()

        return view
    }

    private fun actions() {

        mainHomePageRatingsRefresh_ImageView.setOnClickListener{
            retrieveDataFromtheSuperBase()
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
            find_koding_buddy_actions()
        }
    }

    private fun find_koding_buddy_actions() {
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


    @SuppressLint("SetTextI18n")
    private fun allDataSettoTheUI() {
        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )

        var userName = sharedPreferences?.getString("user_name", null)
        var LCuserName = sharedPreferences?.getString("leetcode_user_name",null)
        var LCattendedContestsCount = sharedPreferences?.getString("leetcode_attended_contests_count",null)
        var LCrating = sharedPreferences?.getString("leetcode_rating",null)
        var LCglobalRanking = sharedPreferences?.getString("leetcode_global_ranking",null)
        var LCtopPercentage = sharedPreferences?.getString("leetcode_top_percentage",null)
        var CFuserName = sharedPreferences?.getString("codeforces_user_name",null)
        var CFrating = sharedPreferences?.getString("codeforces_rating",null)
        var CCuserName = sharedPreferences?.getString("codechef_user_name",null)
        var CCrating = sharedPreferences?.getString("codechef_rating",null)
        var lastUpdate = sharedPreferences?.getString("lastUpdate",null)

        if(userName!=null && userName=="") userName = null
        if(LCuserName!=null && LCuserName=="") LCuserName = null
        if(LCattendedContestsCount!=null && LCattendedContestsCount=="") LCattendedContestsCount = null
        if(LCrating!=null && LCrating=="") LCrating = null
        if(LCglobalRanking!=null && LCglobalRanking=="") LCglobalRanking = null
        if(LCtopPercentage!=null && LCtopPercentage=="") LCtopPercentage = null
        if(CFuserName!=null && CFuserName=="") CFuserName = null
        if(CFrating!=null && CFrating=="") CFrating = null
        if(CCuserName!=null && CCuserName=="") CCuserName = null
        if(CCrating!=null && CCrating=="") CCrating = null
        if(lastUpdate!=null && lastUpdate=="") lastUpdate = null


        userName_TextView.text = "\uD83D\uDC4B Hello, " + if(userName==null)  "" else userName.split(" ")[0]

        lcUserName_TextView.text = LCuserName?: "-"
        ratingsLC_TextView.text = if(LCrating!=null) LCrating.split(".")[0] else "-"
        toppercentageLC_TextView.text = if(LCtopPercentage!=null) "$LCtopPercentage%" else "-"
        livecontestLC_TextView.text = LCattendedContestsCount?:"-"
        globalrankingLC_TextView.text = LCglobalRanking?.substring(1, LCglobalRanking.length - 1) ?: "-"
        ratingsCF_TextView.text = CFrating?: "-"
        ratingCC_TextView.text = CCrating?: "-"
        cfUserName_TextView.text = CFuserName?: "-"
        ccUserName_TextView.text = CCuserName?: "-"
        lastUpdateHomeMainPage_TextView.text = lastUpdate

    }

    private fun retrieveDataFromtheSuperBase() {

        progressDialog.show()

        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        var user_email = sharedPreferences?.getString("user_email","")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = superbase.postgrest.from("users")
                    .select("user_codechef, user_codeforces, user_leetcode"){
                        eq("user_email", user_email?:"")
                    }.body.jsonArray[0].jsonObject

                withContext(Dispatchers.Main) {
                    retrieveDataUSingAPI(
                        user["user_leetcode"]?.jsonPrimitive?.contentOrNull,
                        user["user_codeforces"]?.jsonPrimitive?.contentOrNull,
                        user["user_codechef"]?.jsonPrimitive?.contentOrNull
                    )
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Error fetching users: ${e.message}", Toast.LENGTH_SHORT).show()
                    println("Error fetching users: ${e.message}")
                }
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

            val resultLeetCode = async { APIServices{}.execute(userLeetCode, "isLCExist").get() }
            val resultCodeforces = async { APIServices{}.execute(userCodeforces, "isCFExist").get() }
            val resultCodeChef = async { APIServices{}.execute(userCodechef, "isCCExist").get() }
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


            myEdit.putString("leetcode_user_name", resultLeetCode?.get("userName")?.asString.toString())
            myEdit.putString("leetcode_attended_contests_count",resultLeetCode?.get("attendedContestsCount")?.asString.toString())
            myEdit.putString("leetcode_rating",resultLeetCode?.get("rating")?.asString.toString())
            myEdit.putString("leetcode_global_ranking",resultLeetCode?.get("globalRanking")?.asString.toString())
            myEdit.putString("leetcode_total_participants",resultLeetCode?.get("totalParticipants")?.asString.toString())
            myEdit.putString("leetcode_top_percentage",resultLeetCode?.get("topPercentage")?.asString.toString())

            myEdit.putString("codeforces_user_name",resultCodeforces?.get("userName")?.asString.toString())
            myEdit.putString("codeforces_rating",resultCodeforces?.get("rating")?.asString.toString())
            myEdit.putString("codeforces_max_rating",resultCodeforces?.get("maxRating")?.asString.toString())
            myEdit.putString("codeforces_rank",resultCodeforces?.get("rank")?.asString.toString())
            myEdit.putString("codeforces_max_rank",resultCodeforces?.get("maxRank")?.asString.toString())

            myEdit.putString("codechef_user_name",resultCodeChef?.get("userName")?.asString.toString())
            myEdit.putString("codechef_rating",resultCodeChef?.get("rating")?.asString.toString())

            myEdit.apply()
        }
        Toast.makeText(context, "Data refreshed !! at ${dateFormat.format(Date(currentTimestamp))}", Toast.LENGTH_SHORT).show()

    }

    private fun initialization(view: View) {


        superbase = createSupabaseClient(
            supabaseUrl = Constants.SUPERBASE_URL,
            supabaseKey = Constants.SUPERBASE_KEY
        ) {
            install(Postgrest)
        }

        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please wait")

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