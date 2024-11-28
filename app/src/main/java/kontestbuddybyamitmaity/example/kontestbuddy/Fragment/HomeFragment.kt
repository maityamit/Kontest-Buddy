package kontestbuddybyamitmaity.example.kontestbuddy.Fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeChefVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Compare.CFcompareActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Compare.CompareActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Compare.LCcompareActivity
import kontestbuddybyamitmaity.example.kontestbuddy.CurrentFriends.CodeForcesFrndAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.CurrentFriends.LeetCodeCurrentFrndAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.MainActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Ranking.LeaderboardActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
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
    lateinit var coding_time_stat_progress_bar:ProgressBar
    lateinit var know_more_button_for_extension:TextView
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
        load_ratings_time_state_from_extesion()

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

        know_more_button_for_extension.setOnClickListener {
            // Open the extension link
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse("https://chromewebstore.google.com/detail/kontest-buddy/chdigpjiicmbbgkdkcfjgjlpejnfahkl")
            startActivity(intent)
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

        ratingsLC_TextView.text = if(LCrating =="null") {"-"} else {LCrating?.substring(1, 5) ?: ""}
        toppercentageLC_TextView.text = if (LCtopPercentage == "null") {"-"} else {(LCtopPercentage?.substring(1, LCtopPercentage.length - 1) + "%") } ?: ""
        livecontestLC_TextView.text = if(LCattendedContestsCount == "null") {"-"} else {LCattendedContestsCount?.substring(1, LCattendedContestsCount.length - 1) ?: ""}
        globalrankingLC_TextView.text = if(LCglobalRanking == "null") {"-"} else {LCglobalRanking?.substring(1, LCglobalRanking.length - 1) ?: ""}

        ratingsCF_TextView.text = if(CFrating == "null") {"-"} else {CFrating?.substring(1, CFrating.length - 1) ?: "" }
        ratingCC_TextView.text = if(CCrating == "null") {"-"} else {CCrating?.substring(1, CCrating.length - 1) ?: "" }
        lcUserName_TextView.text = if(LCuserName == "null") {"-"} else {LCuserName?.substring(1, LCuserName.length - 1) ?: "" }
        cfUserName_TextView.text = if(CFuserName == "null") {"-"} else {CFuserName?.substring(1, CFuserName.length - 1) ?: "" }


        ccUserName_TextView.text = if(CCuserName == "null") {"-"} else {CCuserName?.substring(1, CCuserName.length - 1) ?: "" }
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

//        val allEntries = sharedPreferences.all
//        for ((key, value) in allEntries) {
//            Log.d("ALl_DATA", "Key: $key, Value: $value")
//        }



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
        know_more_button_for_extension = view.findViewById(R.id.know_more_button_for_extension)
        coding_time_stat_progress_bar = view.findViewById(R.id.coding_time_stat_progress_bar)
//        CompareButtonCodeForces = view.findViewById(R.id.CompareButtonCodeForces)
//        LeaderboardButtonLeetCode = view.findViewById(R.id.ADDLeaderboardButtonLeetCode)
//        LeaderboardButtonCodeForces = view.findViewById(R.id.ADDLeaderboardButtonCodeForces)
//        codeforces_leaderboard_Button = view.findViewById(R.id.LeaderboardButtonCodeForces)
//        leetcode_leaderboard_Button = view.findViewById(R.id.LeaderboardButtonLeetCode)
//        resource_layout_button = view.findViewById(R.id.resource_layout_button)
    }


    private fun load_ratings_time_state_from_extesion(){


        // Date -> url-> time
        val nestedMap: MutableMap<String, MutableMap<String, Long>> = mutableMapOf()
        val allowedDomains = listOf(
            "leetcode.com",
            "github.com",
            "codeforces.com",
            "codechef.com",
            "geeksforgeeks.org"
        )

        val auth  = FirebaseAuth.getInstance()
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        val userID = auth.currentUser?.uid.toString()
        val userRef = database.child("users").child(userID)

        // the data is in realtime database within users->userID folder so to retrive write code for that

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Iterate through all random IDs under the user
                    for (randomIdSnapshot in snapshot.children) {
                        val randomIdKey = randomIdSnapshot.key // Get the random ID
                        println("Processing data for Random ID: $randomIdKey")

                        // Access data inside the random ID
                        for (dataSnapshot in randomIdSnapshot.children) {
                            val dataMap = dataSnapshot.value as? Map<String, Any>
                            val url = dataMap?.get("URL") as? String
                            val startTime = dataMap?.get("startTime") as? Long
                            val totalTime = dataMap?.get("totalTime").toString().toLong()

                            Log.e("ExtesionData = ${randomIdKey}", "${url} + ${startTime} + ${totalTime} ")

                            val timestamp = startTime?.toLong()
                            val date = timestamp?.let { Date(it) }
                            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val formattedDate = formatter.format(date).toString()

                            val matchingDomain = allowedDomains.find { domain -> url?.contains(domain)
                                ?: false }

                            if (matchingDomain != null) {

                                val timeSpent = (nestedMap[formattedDate]?.get(matchingDomain) ?: 0).toLong()
                                // Initialize the nested map for the date if it's null
                                val domainMap = nestedMap[formattedDate] ?: mutableMapOf()
                                domainMap[matchingDomain] = (timeSpent + (totalTime ?: 0)).toLong()
                                nestedMap[formattedDate] = domainMap
                            }

                            // Process the data
                        }
                    }

                    process_data_and_analysis(nestedMap)

                } else {
                    println("No data found for user: $userID")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error fetching data: ${error.message}")
            }
        })

        Toast.makeText(context, "Data refreshed !! at ${nestedMap.toString()}", Toast.LENGTH_SHORT).show()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun process_data_and_analysis(nestedMap: MutableMap<String, MutableMap<String, Long>>) {

//        check nestedMap is empty or not

        // check the data is empty or not
        if (nestedMap.isEmpty()) {
            return
        }else{

            // Convert date strings to LocalDate for easier manipulation
            val dateFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dateMap = nestedMap.mapKeys { java.time.LocalDate.parse(it.key, dateFormatter) }

            // Sort dates in ascending order
            val sortedDates = dateMap.keys.sorted()

            // For daily data: Each date and its site->time map
            val dailyData = sortedDates.map { date ->
                mapOf(date.format(dateFormatter) to dateMap[date]!!)
            }

            // For weekly data: Group data into chunks of 7 days
            val weeklyData = mutableListOf<Map<String, Map<String, Long>>>()
            var currentWeekStartIndex = 0
            while (currentWeekStartIndex < sortedDates.size) {
                val currentWeekDates = sortedDates.subList(
                    currentWeekStartIndex,
                    (currentWeekStartIndex + 7).coerceAtMost(sortedDates.size)
                )
                val weeklyMap = mutableMapOf<String, MutableMap<String, Long>>()
                for (date in currentWeekDates) {
                    val formattedDate = date.format(dateFormatter)
                    dateMap[date]?.forEach { (site, time) ->
                        weeklyMap[formattedDate] = weeklyMap[formattedDate]?.toMutableMap() ?: mutableMapOf()
                        weeklyMap[formattedDate]!![site] = (weeklyMap[formattedDate]?.get(site) ?: 0) + time
                    }
                }
                weeklyData.add(weeklyMap)
                currentWeekStartIndex += 7
            }

            // For monthly data: Group data into chunks of 30 days
            val monthlyData = mutableListOf<Map<String, Map<String, Long>>>()
            var currentMonthStartIndex = 0
            while (currentMonthStartIndex < sortedDates.size) {
                val currentMonthDates = sortedDates.subList(
                    currentMonthStartIndex,
                    (currentMonthStartIndex + 30).coerceAtMost(sortedDates.size)
                )
                val monthlyMap = mutableMapOf<String, MutableMap<String, Long>>()
                for (date in currentMonthDates) {
                    val formattedDate = date.format(dateFormatter)
                    dateMap[date]?.forEach { (site, time) ->
                        monthlyMap[formattedDate] = monthlyMap[formattedDate]?.toMutableMap() ?: mutableMapOf()
                        monthlyMap[formattedDate]!![site] = (monthlyMap[formattedDate]?.get(site) ?: 0) + time
                    }
                }
                monthlyData.add(monthlyMap)
                currentMonthStartIndex += 30
            }


            Log.e("DATE_EXTENSION", "${dailyData} + ${weeklyData} + ${monthlyData} ")

        }

    }

}