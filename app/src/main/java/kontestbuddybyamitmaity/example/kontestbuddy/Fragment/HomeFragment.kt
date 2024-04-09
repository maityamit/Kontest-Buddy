package kontestbuddybyamitmaity.example.kontestbuddy.Fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeChefVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.GFGVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Compare.CFcompareActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Compare.LCcompareActivity
import kontestbuddybyamitmaity.example.kontestbuddy.CurrentFriends.CodeForcesFrndAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.CurrentFriends.LeetCodeCurrentFrndAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Ranking.CFCustomAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.Ranking.CFrankingActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Ranking.LCrankingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeFragment : Fragment() {


    lateinit var userName:TextView
    lateinit var ratingsLC:TextView
    lateinit var toppercentageLC:TextView
    lateinit var livecontestLC:TextView
    lateinit var globalrankingLC:TextView
    lateinit var ratingsCF :TextView
    lateinit var ratingCC :TextView
    lateinit var lcUserName :TextView
    lateinit var cfUserName :TextView
    lateinit var ccUserName :TextView
    lateinit var lastUpdateHomeMainPage:TextView
    lateinit var mainHomePageRatingsRefresh:ImageView
    private lateinit var progressDialog: ProgressDialog
    lateinit var CompareButtonLeetCode:CardView
    lateinit var CompareButtonCodeForces:CardView
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    lateinit var LeaderboardButtonLeetCode:TextView
    lateinit var LeaderboardButtonCodeForces:TextView
    lateinit var leetcode_leaderboard_Button:LinearLayout
    lateinit var codeforces_leaderboard_Button:CardView

    lateinit var gfgUserName:TextView
    lateinit var gfgRatings:TextView
    lateinit var gfgProblemsSolved:TextView
    lateinit var gfgArticlerPublished:TextView

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
        mainHomePageRatingsRefresh.setOnClickListener{
            progressDialog.show()
            retrieveDataFromtheFirebase()
        }

        CompareButtonLeetCode.setOnClickListener {
            context?.let { it1 -> CustomDialog(it1, this) }?.show()
        }

        CompareButtonCodeForces.setOnClickListener{
            context?.let { it1 -> CustomDialog2(it1, this) }?.show()
        }

        LeaderboardButtonLeetCode.setOnClickListener{
            context?.let { it1 -> CustomDialog3(it1, this) }?.show()
        }

        LeaderboardButtonCodeForces.setOnClickListener{
            context?.let { it1 -> CustomDialog4(it1, this) }?.show()
        }


        leetcode_leaderboard_Button.setOnClickListener{
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {

                        val LCleader = document.getString("LCleader")
                        if(LCleader?.isNotBlank()==true){
                            progressDialog.dismiss()
                            val intent:Intent = Intent(context,LCrankingActivity::class.java)
                            intent.putExtra("userNames",LCleader)
                            startActivity(intent)
                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(context,"You have not any friends !!",Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        codeforces_leaderboard_Button.setOnClickListener{
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {

                        val LCleader = document.getString("CFleader")
                        if(LCleader?.isNotBlank()==true){
                            progressDialog.dismiss()
                            val intent:Intent = Intent(context, CFrankingActivity::class.java)
                            intent.putExtra("userNames",LCleader)
                            startActivity(intent)
                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(context,"You have not any friends !!",Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        return view
    }

    class CustomDialog(context: Context, private val listener: HomeFragment) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(context)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.leetcode_compare_two_users_layout)

            val editText1 = findViewById<EditText>(R.id.leetcode_compare_inputUser1)
            val editText2 = findViewById<EditText>(R.id.leetcode_compare_inputUser2)

            val verify1 = findViewById<TextView>(R.id.leetcode_compare_inputUser1_Verify)
            val verify2 = findViewById<TextView>(R.id.leetcode_compare_inputUser2_verify)
            val okButton = findViewById<Button>(R.id.leetcode_compare_Button)

            var leetCodeUserName1:String = ""
            var leetCodeUserName2:String = ""
            var lcUser1verify = false
            var lcUser2verify = false

            verify1.setOnClickListener {
                val user_leetcode = editText1.text.toString()
                if(user_leetcode.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = LeetCodeVerifyApiTask { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){
                            verify1.text = "Verified"
                            verify1.setTextColor(Color.GREEN)
                            verify1.isEnabled = false
                            editText1.isEnabled = false
                            lcUser1verify = true
                            leetCodeUserName1 = editText1.text.toString()

                        }else{
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()
                    }
                    apiTask.execute(user_leetcode)
                }
            }

            verify2.setOnClickListener {
                val user_leetcode = editText2.text.toString()
                if(user_leetcode.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = LeetCodeVerifyApiTask { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){
                            verify2.text = "Verified"
                            verify2.setTextColor(Color.GREEN)
                            verify2.isEnabled = false
                            editText2.isEnabled = false
                            lcUser2verify = true
                            leetCodeUserName2 = editText2.text.toString()

                        }else{
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()
                    }
                    apiTask.execute(user_leetcode)
                }
            }
            okButton.setOnClickListener {
                if(lcUser1verify && lcUser2verify){
                    val intent:Intent = Intent(context,LCcompareActivity::class.java)
                    intent.putExtra("userName1",leetCodeUserName1)
                    intent.putExtra("userName2",leetCodeUserName2)
                    context.startActivity(intent)
                }
            }
        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }

    class CustomDialog2(context: Context, private val listener: HomeFragment) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(context)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.codeforces_compare_two_users_layout)

            val editText1 = findViewById<EditText>(R.id.codeforces_compare_inputUser1)
            val editText2 = findViewById<EditText>(R.id.codeforces_compare_inputUser2)

            val verify1 = findViewById<TextView>(R.id.codeforces_compare_inputUser1_Verify)
            val verify2 = findViewById<TextView>(R.id.codeforces_compare_inputUser2_verify)
            val okButton = findViewById<Button>(R.id.codeforces_compare_Button)

            var codeforcesUserName1:String = ""
            var codeforcesUserName2:String = ""
            var cfUser1verify = false
            var cfUser2verify = false

            verify1.setOnClickListener {
                val user_codeforces = editText1.text.toString()
                if(user_codeforces.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = CodeForcesVerifyApiTask { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){
                            verify1.text = "Verified"
                            verify1.setTextColor(Color.GREEN)
                            verify1.isEnabled = false
                            editText1.isEnabled = false
                            cfUser1verify = true
                            codeforcesUserName1 = editText1.text.toString()

                        }else{
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()
                    }
                    apiTask.execute(user_codeforces)
                }
            }

            verify2.setOnClickListener {
                val user_codeforces = editText2.text.toString()
                if(user_codeforces.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = CodeForcesVerifyApiTask { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){
                            verify2.text = "Verified"
                            verify2.setTextColor(Color.GREEN)
                            verify2.isEnabled = false
                            editText2.isEnabled = false
                            cfUser2verify = true
                            codeforcesUserName2 = editText2.text.toString()

                        }else{
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()
                    }
                    apiTask.execute(user_codeforces)
                }
            }
            okButton.setOnClickListener {
                if(cfUser1verify && cfUser2verify){
                    val intent:Intent = Intent(context, CFcompareActivity::class.java)
                    intent.putExtra("userName1",codeforcesUserName1)
                    intent.putExtra("userName2",codeforcesUserName2)
                    context.startActivity(intent)
                }
            }

//            okButton.setOnClickListener {
//                val inputText = editText.text.toString()
//                listener.onDialogOkButtonClicked(inputText)
//                dismiss()
//            }
        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }

    class CustomDialog3(context: Context, private val listener: HomeFragment) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(context)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.leetcode_leaderboard_dialoguebox)


            val current_user_list_show_recyclerView_leetcode:RecyclerView = findViewById(R.id.current_user_list_show_recyclerView_leetcode)
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        val LCleader = document.getString("LCleader")
                        // Toast.makeText(context,LCleader.toString(),Toast.LENGTH_SHORT).show()

                        if (LCleader != null) {
                            if (LCleader.isBlank()){
                                Toast.makeText(context,"You haven't any friends yet!",Toast.LENGTH_SHORT).show()
                            }
                        }
                        val list:MutableList<String> = mutableListOf()
                        var temp:String = ""
                        if (LCleader != null) {
                            for(ele in LCleader){
                                if(ele==';'){
                                    list.add(temp)
                                    temp = ""
                                }else{
                                    temp+=ele
                                }
                            }
                        }
                        current_user_list_show_recyclerView_leetcode.layoutManager = LinearLayoutManager(context)
                        current_user_list_show_recyclerView_leetcode.adapter = list.let { it1 ->
                            LeetCodeCurrentFrndAdapter(
                                it1
                            )
                        }
                        progressDialog.dismiss()

                    } else {
                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }

            val leetcode_leaderboard_inputUser1:EditText = findViewById(R.id.leetcode_leaderboard_inputUser1)
            val leetcode_leaderboard_inputUser1_Verify:TextView = findViewById(R.id.leetcode_leaderboard_inputUser1_Verify)
            val leetcode_leaderboard_all_frinds_show:TextView = findViewById(R.id.show_leetcode_current_friends)

            leetcode_leaderboard_inputUser1_Verify.setOnClickListener {
                val strUser:String = leetcode_leaderboard_inputUser1.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    progressDialog.show()
                    val apiTask = LeetCodeVerifyApiTask { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){

                            val db = Firebase.firestore
                            val auth: FirebaseAuth = FirebaseAuth.getInstance()

                            val userID = auth.currentUser?.uid.toString()
                            val docRef = db.collection("users").document(userID)
                            docRef.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    if (document != null) {

                                        var LCleader = document.getString("LCleader")
                                        if(LCleader?.isBlank() == true){
                                            LCleader = strUser+";"
                                        }else if(LCleader?.contains(strUser) == false){
                                            LCleader = LCleader+strUser+";"
                                        }

                                        docRef.update("LCleader",LCleader).addOnSuccessListener {
                                            leetcode_leaderboard_inputUser1.text.clear()
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "User Added Done !", Toast.LENGTH_SHORT)
                                                .show()
                                        }.addOnFailureListener{
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                                .show()
                                        }


                                    } else {
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                    }
                    apiTask.execute(strUser)
                }
            }

            val leetcode_leaderboard_inputUser2:EditText = findViewById(R.id.leetcode_leaderboard_inputUser2)
            val leetcode_leaderboard_inputUser2_Verify:TextView = findViewById(R.id.leetcode_leaderboard_inputUser2_verify)
            leetcode_leaderboard_inputUser2_Verify.setOnClickListener {
                var strUser:String = leetcode_leaderboard_inputUser2.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    strUser+=";"
                    progressDialog.show()
                    val db = Firebase.firestore
                    val auth: FirebaseAuth = FirebaseAuth.getInstance()

                    val userID = auth.currentUser?.uid.toString()
                    val docRef = db.collection("users").document(userID)
                    docRef.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null) {

                                val LCleader = document.getString("LCleader")

                                if(LCleader?.isBlank() == false) {
                                    val resultString:String = LCleader.replace(strUser, "")
                                    docRef.update("LCleader",resultString).addOnSuccessListener {
                                        leetcode_leaderboard_inputUser2.text.clear()
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "User Removed Done !", Toast.LENGTH_SHORT)
                                            .show()
                                    }.addOnFailureListener{
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }else{
                                    progressDialog.dismiss()
                                    Toast.makeText(context, "No Such User in your Friends", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

            leetcode_leaderboard_all_frinds_show.setOnClickListener {
                progressDialog.show()
                val db = Firebase.firestore
                val auth: FirebaseAuth = FirebaseAuth.getInstance()

                val userID = auth.currentUser?.uid.toString()
                val docRef = db.collection("users").document(userID)
                docRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            val LCleader = document.getString("LCleader")
                           // Toast.makeText(context,LCleader.toString(),Toast.LENGTH_SHORT).show()
                            val list:MutableList<String> = mutableListOf()
                            var temp:String = ""
                            if (LCleader != null) {
                                for(ele in LCleader){
                                    if(ele==';'){
                                        list.add(temp)
                                        temp = ""
                                    }else{
                                        temp+=ele
                                    }
                                }
                            }
                            current_user_list_show_recyclerView_leetcode.layoutManager = LinearLayoutManager(context)
                            current_user_list_show_recyclerView_leetcode.adapter = list.let { it1 ->
                                LeetCodeCurrentFrndAdapter(
                                    it1
                                )
                            }
                            progressDialog.dismiss()

                        } else {
                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                .show()
                            progressDialog.dismiss()
                        }
                    } else {
                        Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                }
            }

        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }


    class CustomDialog4(context: Context, private val listener: HomeFragment) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(context)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.codeforces_leaderboard_dialoguebox)

            val current_user_list_show_recyclerView_codeforces:RecyclerView = findViewById(R.id.current_user_list_show_recyclerView_codeforces)
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        val LCleader = document.getString("CFleader")
                        // Toast.makeText(context,LCleader.toString(),Toast.LENGTH_SHORT).show()

                        if (LCleader != null) {
                            if (LCleader.isBlank()){
                                Toast.makeText(context,"You haven't any friends yet!",Toast.LENGTH_SHORT).show()
                            }
                        }
                        val list:MutableList<String> = mutableListOf()
                        var temp:String = ""
                        if (LCleader != null) {
                            for(ele in LCleader){
                                if(ele==';'){
                                    list.add(temp)
                                    temp = ""
                                }else{
                                    temp+=ele
                                }
                            }
                        }
                        current_user_list_show_recyclerView_codeforces.layoutManager = LinearLayoutManager(context)
                        current_user_list_show_recyclerView_codeforces.adapter = list.let { it1 ->
                            CodeForcesFrndAdapter(
                                it1
                            )
                        }
                        progressDialog.dismiss()

                    } else {
                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }


            val codeforces_leaderboard_inputUser1:EditText = findViewById(R.id.codeforces_leaderboard_inputUser1)
            val codeforces_leaderboard_inputUser1_Verify:TextView = findViewById(R.id.codeforces_leaderboard_inputUser1_Verify)
            codeforces_leaderboard_inputUser1_Verify.setOnClickListener {
                val strUser:String = codeforces_leaderboard_inputUser1.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    progressDialog.show()
                    val apiTask = CodeForcesVerifyApiTask { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){

                            val db = Firebase.firestore
                            val auth: FirebaseAuth = FirebaseAuth.getInstance()

                            val userID = auth.currentUser?.uid.toString()
                            val docRef = db.collection("users").document(userID)
                            docRef.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    if (document != null) {

                                        var LCleader = document.getString("CFleader")
                                        if(LCleader?.isBlank() == true){
                                            LCleader = strUser+";"
                                        }else if(LCleader?.contains(strUser) == false){
                                            LCleader = LCleader+strUser+";"
                                        }

                                        docRef.update("CFleader",LCleader).addOnSuccessListener {
                                            codeforces_leaderboard_inputUser1.text.clear()
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "User Added Done !", Toast.LENGTH_SHORT)
                                                .show()
                                        }.addOnFailureListener{
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                                .show()
                                        }


                                    } else {
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                    }
                    apiTask.execute(strUser)
                }
            }

            val codeforces_leaderboard_inputUser2:EditText = findViewById(R.id.codeforces_leaderboard_inputUser2)
            val codeforces_leaderboard_inputUser2_Verify:TextView = findViewById(R.id.codeforces_leaderboard_inputUser2_verify)
            codeforces_leaderboard_inputUser2_Verify.setOnClickListener {
                var strUser:String = codeforces_leaderboard_inputUser2.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    strUser+=";"
                    progressDialog.show()
                    val db = Firebase.firestore
                    val auth: FirebaseAuth = FirebaseAuth.getInstance()

                    val userID = auth.currentUser?.uid.toString()
                    val docRef = db.collection("users").document(userID)
                    docRef.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null) {

                                val LCleader = document.getString("CFleader")

                                if(LCleader?.isBlank() == false) {
                                    val resultString:String = LCleader.replace(strUser, "")
                                    docRef.update("CFleader",resultString).addOnSuccessListener {
                                        codeforces_leaderboard_inputUser2.text.clear()
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "User Removed Done !", Toast.LENGTH_SHORT)
                                            .show()
                                    }.addOnFailureListener{
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }else{
                                    progressDialog.dismiss()
                                    Toast.makeText(context, "No Such User in your Friends", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

            val show_codeforces_current_friends:TextView = findViewById(R.id.show_codeforces_current_friends)
            show_codeforces_current_friends.setOnClickListener {
                progressDialog.show()
                val db = Firebase.firestore
                val auth: FirebaseAuth = FirebaseAuth.getInstance()

                val userID = auth.currentUser?.uid.toString()
                val docRef = db.collection("users").document(userID)
                docRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            val LCleader = document.getString("CFleader")
                            // Toast.makeText(context,LCleader.toString(),Toast.LENGTH_SHORT).show()
                            val list:MutableList<String> = mutableListOf()
                            var temp:String = ""
                            if (LCleader != null) {
                                for(ele in LCleader){
                                    if(ele==';'){
                                        list.add(temp)
                                        temp = ""
                                    }else{
                                        temp+=ele
                                    }
                                }
                            }
                            current_user_list_show_recyclerView_codeforces.layoutManager = LinearLayoutManager(context)
                            current_user_list_show_recyclerView_codeforces.adapter = list.let { it1 ->
                                CodeForcesFrndAdapter(
                                    it1
                                )
                            }
                            progressDialog.dismiss()

                        } else {
                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                .show()
                            progressDialog.dismiss()
                        }
                    } else {
                        Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                }
            }


        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }

    private fun allDataSettoTheUI() {
        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        //Retrieve Data From Local Storage and Set Data into The Container
        val userFullName:String? = sharedPreferences?.getString("userName","")
        if (userFullName != null) {
            userName.text = "\uD83D\uDC4B Hello, " + userFullName.split(" ")[0]
        }else{
            userName.text = "\uD83D\uDC4B Hello, "
        }
        ratingsLC.text = sharedPreferences?.getString("ratingsLC","")
        toppercentageLC.text = sharedPreferences?.getString("toppercentageLC","")+"%"
        livecontestLC.text = sharedPreferences?.getString("livecontestLC","")
        globalrankingLC.text = sharedPreferences?.getString("globalrankingLC","")
        ratingsCF.text = sharedPreferences?.getString("ratingsCF","")
        ratingCC.text = sharedPreferences?.getString("ratingCC","")
        lcUserName.text = sharedPreferences?.getString("userLeetcode","")
        cfUserName.text = sharedPreferences?.getString("userCodeforces","")
        ccUserName.text = sharedPreferences?.getString("userCodechef","")
        lastUpdateHomeMainPage.text = sharedPreferences?.getString("lastUpdate","")

        gfgUserName.text = sharedPreferences?.getString("userGFG","")
        gfgRatings.text = sharedPreferences?.getString("gfg_rating","")
        gfgProblemsSolved.text = sharedPreferences?.getString("gfg_problem_solved","")
        gfgArticlerPublished.text = sharedPreferences?.getString("gfg_articlesPublished","")


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
                    val userGFG = document.getString("userGFG")

                    retrieveDataUSingAPI(userLeetcode,userCodeforces,userCodechef,userName,userGFG)

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

    private fun retrieveDataUSingAPI(userLeetcode: String?, userCodeforces: String?, userCodechef: String?, userName: String?,userGFG: String?) {

        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        val myEdit = sharedPreferences?.edit()
        if (myEdit != null) {
            myEdit.putString("userName", userName)
            myEdit.putString("userLeetcode",userLeetcode)
            myEdit.putString("userCodeforces",userCodeforces)
            myEdit.putString("userCodechef",userCodechef)
            myEdit.putString("userGFG",userGFG)
            myEdit.apply()
        }

        callApiTasks(
            userLeetCode = userLeetcode,
            userCodeforces = userCodeforces,
            userCodechef = userCodechef,
            userGFG = userGFG,
            onAllApiResultsAvailable = {
                progressDialog.dismiss()
                allDataSettoTheUI()
            }
        )

    }

    fun callApiTasks(
        userLeetCode: String?,
        userCodeforces: String?,
        userCodechef: String?,
        userGFG: String?,
        onAllApiResultsAvailable: () -> Unit
    ) {
        coroutineScope.launch {

            val resultLeetCode = async { LeetCodeVerifyApiTask{}.execute(userLeetCode).get() }
            val resultCodeforces = async { CodeForcesVerifyApiTask{}.execute(userCodeforces).get() }
            val resultCodeChef = async { CodeChefVerifyApiTask{}.execute(userCodechef).get() }
            val resultGFG = async { GFGVerifyApiTask{}.execute(userGFG).get() }
            val allResults = awaitAll(resultLeetCode, resultCodeforces, resultCodeChef, resultGFG)
            handleAllApiResults(allResults)
            onAllApiResultsAvailable()
        }
    }

    fun handleAllApiResults(results: List<JsonObject?>) {
        // Handle individual API results here
        val resultLeetCode = results[0]
        val resultCodeforces = results[1]
        val resultCodeChef = results[2]
        val resultGFG = results[3]

        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        val myEdit = sharedPreferences?.edit()

        val currentTimestamp: Long = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yy - hh:mm a", Locale.getDefault())
        if (myEdit != null) {
            myEdit.putString("lastUpdate",dateFormat.format(Date(currentTimestamp)))
            myEdit.putString("ratingsLC",resultLeetCode?.get("rating").toString())
            myEdit.putString("toppercentageLC",resultLeetCode?.get("topPercentage").toString())
            myEdit.putString("livecontestLC",resultLeetCode?.get("attendedContestsCount").toString())
            myEdit.putString("globalrankingLC",resultLeetCode?.get("globalRanking").toString())
            myEdit.putString("ratingsCF",resultCodeforces?.get("rating").toString())
            myEdit.putString("ratingCC",resultCodeChef?.get("rating").toString())
            myEdit.putString("gfg_rating",resultGFG?.get("rating").toString())
            myEdit.putString("gfg_problem_solved",resultGFG?.get("problem_solved").toString())
            myEdit.putString("gfg_monthlyCodingScore",resultGFG?.get("monthlyCodingScore").toString())
            myEdit.putString("gfg_articlesPublished",resultGFG?.get("articlesPublished").toString())

            myEdit.apply()
        }


    }

    private fun initialization(view: View) {
        userName = view.findViewById(R.id.userNameinMainPage);
        ratingsLC = view.findViewById(R.id.lcUserRatingsMainPage);
        toppercentageLC = view.findViewById(R.id.lcUsertopMainPage);
        livecontestLC = view.findViewById(R.id.lcUsercontestMainPage);
        globalrankingLC = view.findViewById(R.id.lcUserglobalMainPage);
        ratingsCF  = view.findViewById(R.id.cfUserRatingsMainPage);
        ratingCC  = view.findViewById(R.id.ccUserRatingsMainPage);
        lcUserName  = view.findViewById(R.id.lcUserNameMainPage);
        cfUserName  = view.findViewById(R.id.cfUserNameMainPage);
        ccUserName  = view.findViewById(R.id.ccUserNameMainPage);
        lastUpdateHomeMainPage = view.findViewById(R.id.lastUpdateHomeMainPage)
        mainHomePageRatingsRefresh = view.findViewById(R.id.mainHomePageRatingsRefresh)
        CompareButtonLeetCode = view.findViewById(R.id.CompareButtonLeetCode)
        CompareButtonCodeForces = view.findViewById(R.id.CompareButtonCodeForces)
        LeaderboardButtonLeetCode = view.findViewById(R.id.ADDLeaderboardButtonLeetCode)
        LeaderboardButtonCodeForces = view.findViewById(R.id.ADDLeaderboardButtonCodeForces)
        codeforces_leaderboard_Button = view.findViewById(R.id.LeaderboardButtonCodeForces)
        leetcode_leaderboard_Button = view.findViewById(R.id.LeaderboardButtonLeetCode)
        gfgUserName = view.findViewById(R.id.gfgUserNameMainPage)
        gfgRatings = view.findViewById(R.id.gfgUserRatingsMainPage)
        gfgProblemsSolved = view.findViewById(R.id.gfgProblemSolvedMainPage)
        gfgArticlerPublished = view.findViewById(R.id.gfgArticlesPublishedMainPage)
    }

}