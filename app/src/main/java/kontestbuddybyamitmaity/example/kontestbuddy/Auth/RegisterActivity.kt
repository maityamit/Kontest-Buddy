package kontestbuddybyamitmaity.example.kontestbuddy.Auth

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeChefVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.MainActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private var leetcode_verify = false
    private var codeforces_verify = false
    private var codechef_verify = false

    private var ratingsDetails: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

    private lateinit var progressDialog: ProgressDialog
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please waiiit")

        ratingsDetails["leetcode"]?.set("userName", "")
        ratingsDetails["leetcode"]?.set("attendedContestsCount", "")
        ratingsDetails["leetcode"]?.set("rating", "")
        ratingsDetails["leetcode"]?.set("globalRanking", "")
        ratingsDetails["leetcode"]?.set("totalParticipants", "")
        ratingsDetails["leetcode"]?.set("topPercentage", "")

        ratingsDetails["codeforces"]?.set("userName", "")
        ratingsDetails["codeforces"]?.set("rating", "")
        ratingsDetails["codeforces"]?.set("maxRating", "")
        ratingsDetails["codeforces"]?.set("rank", "")
        ratingsDetails["codeforces"]?.set("maxRank", "")

        ratingsDetails["codechef"]?.set("userName", "")
        ratingsDetails["codechef"]?.set("rating", "")


        findViewById<TextView>(R.id.already_sign_in).setOnClickListener {
            val intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.leetcode_verify_button).setOnClickListener {
            val user_leetcode = findViewById<EditText>(R.id.register_user_leetcode).text.toString()
            if(user_leetcode.isBlank()){
                Toast.makeText(applicationContext,"Enter the UserName",Toast.LENGTH_SHORT).show()
            }else {
                progressDialog.show()
                Log.e("API_RESPONSE","ENTRYY")
                val apiTask = LeetCodeVerifyApiTask { isValid ->
                    if(isValid?.get("isValid").toString()=="\"true\""){
                        val txt = findViewById<TextView>(R.id.leetcode_verify_button)
                        txt.text = "Verified"
                        txt.setTypeface(null, Typeface.BOLD)
                        txt.setTextColor(Color.parseColor("#FF115E14"))
                        findViewById<EditText>(R.id.register_user_leetcode).isEnabled = false
                        findViewById<TextView>(R.id.leetcode_verify_button).isEnabled = false
                        leetcode_verify = true
                        ratingsDetails["leetcode"]?.set("userName", isValid?.get("userName").toString())
                        ratingsDetails["leetcode"]?.set("attendedContestsCount", isValid?.get("attendedContestsCount").toString())
                        ratingsDetails["leetcode"]?.set("rating", isValid?.get("rating").toString())
                        ratingsDetails["leetcode"]?.set("globalRanking", isValid?.get("globalRanking").toString())
                        ratingsDetails["leetcode"]?.set("totalParticipants", isValid?.get("totalParticipants").toString())
                        ratingsDetails["leetcode"]?.set("topPercentage", isValid?.get("topPercentage").toString())
                    }else{
                        Toast.makeText(applicationContext, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                    }

                    progressDialog.dismiss()

                }
                apiTask.execute(user_leetcode)
            }
        }

        findViewById<TextView>(R.id.codeforces_verify_button).setOnClickListener {

            val user_codeforces = findViewById<EditText>(R.id.register_user_codeforces).text.toString()
            if(user_codeforces.isBlank()){
                Toast.makeText(applicationContext,"Enter the UserName",Toast.LENGTH_SHORT).show()
            }else {
                progressDialog.show()
                val apiTask = CodeForcesVerifyApiTask { isValid ->

                    if(isValid?.get("isValid").toString()=="\"true\""){
                        val txt = findViewById<TextView>(R.id.codeforces_verify_button)
                        txt.text = "Verified"
                        txt.setTypeface(null, Typeface.BOLD)
                        txt.setTextColor(Color.parseColor("#FF115E14"))
                        findViewById<EditText>(R.id.register_user_codeforces).isEnabled = false
                        findViewById<TextView>(R.id.codeforces_verify_button).isEnabled = false
                        codeforces_verify = true
                        ratingsDetails["codeforces"]?.set("userName", isValid?.get("userName").toString())
                        ratingsDetails["codeforces"]?.set("rating", isValid?.get("rating").toString())
                        ratingsDetails["codeforces"]?.set("maxRating", isValid?.get("maxRating").toString())
                        ratingsDetails["codeforces"]?.set("rank", isValid?.get("rank").toString())
                        ratingsDetails["codeforces"]?.set("maxRank", isValid?.get("maxRank").toString())


                    }else{
                        Toast.makeText(applicationContext, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                    }

                    progressDialog.dismiss()

                }
                apiTask.execute(user_codeforces)
            }
        }

        findViewById<TextView>(R.id.codechef_verify_button).setOnClickListener {

            val user_codechef = findViewById<EditText>(R.id.register_user_codechef).text.toString()
            if(user_codechef.isBlank()){
                Toast.makeText(applicationContext,"Enter the UserName",Toast.LENGTH_SHORT).show()
            }else {
                progressDialog.show()
                val apiTask = CodeChefVerifyApiTask { isValid ->

                    if(isValid?.get("isValid").toString()=="\"true\""){
                        val txt = findViewById<TextView>(R.id.codechef_verify_button)
                        txt.text = "Verified"
                        txt.setTypeface(null, Typeface.BOLD)
                        txt.setTextColor(Color.parseColor("#FF115E14"))
                        findViewById<EditText>(R.id.register_user_codechef).isEnabled = false
                        findViewById<TextView>(R.id.codechef_verify_button).isEnabled = false
                        codechef_verify = true

                        ratingsDetails["codechef"]?.set("userName", isValid?.get("userName").toString())
                        ratingsDetails["codechef"]?.set("rating", isValid?.get("rating").toString())

                    }else{
                        Toast.makeText(applicationContext, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss()

                }
                apiTask.execute(user_codechef)
            }
        }

        findViewById<Button>(R.id.register_user_button).setOnClickListener{

            Log.e("RATING_DETAILS", ratingsDetails.toString())
            Log.e("RATING_DETAILS", ratingsDetails["leetcode"].toString())
            Log.e("RATING_DETAILS", ratingsDetails["codeforces"].toString())
            Toast.makeText(applicationContext, ratingsDetails.toString(),Toast.LENGTH_SHORT).show()

//            val user_name = findViewById<EditText>(R.id.register_user_name).text.toString()
//            val user_email = findViewById<EditText>(R.id.register_user_email).text.toString()
//            val user_password = findViewById<EditText>(R.id.register_user_password).text.toString()
//            val user_leetcode = findViewById<EditText>(R.id.register_user_leetcode).text.toString()
//            val user_codeforces = findViewById<EditText>(R.id.register_user_codeforces).text.toString()
//            val user_codechef= findViewById<EditText>(R.id.register_user_codechef).text.toString()
//
//            if(user_name.isBlank()){
//                Toast.makeText(applicationContext,"Enter Your Name",Toast.LENGTH_SHORT).show()
//            }else if(user_email.isBlank()){
//                Toast.makeText(applicationContext,"Enter Your Email",Toast.LENGTH_SHORT).show()
//            }else if(user_password.isBlank()){
//                Toast.makeText(applicationContext,"Enter Your Password",Toast.LENGTH_SHORT).show()
//            }else if(user_leetcode.isNotBlank() && !leetcode_verify){
//                Toast.makeText(applicationContext,"Please verify the LC account",Toast.LENGTH_SHORT).show()
//            }else if(user_codeforces.isNotBlank() && !codeforces_verify){
//                Toast.makeText(applicationContext,"Please verify the CF account",Toast.LENGTH_SHORT).show()
//            }else if(user_codechef.isNotBlank() && !codechef_verify){
//                Toast.makeText(applicationContext,"Please verify the CC account",Toast.LENGTH_SHORT).show()
//            }else{
//                progressDialog.show()
//                var lc = "";
//                var cf = "";
//                var cc = "";
//                if(user_leetcode.isNotBlank()) lc = user_leetcode;
//                if(user_codeforces.isNotBlank()) cf = user_codeforces;
//                if(user_codechef.isNotBlank()) cc = user_codechef;
//                createANewUserOnFirebase(user_name,user_password,user_email,lc,cf,cc)
//            }
        }


    }

    private fun createANewUserOnFirebase(userName: String, userPassword: String, userEmail: String, userLeetcode: String, userCodeforces: String, userCodechef: String) {
        val userData = hashMapOf(
            "userName" to userName,
            "userEmail" to userEmail,
            "userLeetcode" to userLeetcode,
            "userCodeforces" to userCodeforces,
            "userCodechef" to userCodechef,
            "LCleader" to "",
            "CFleader" to ""
        )


        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = FirebaseAuth.getInstance().currentUser
                    if(user!=null){
                        val userId = user?.uid.toString()
                        val userInfo = db.collection("users")
                        userInfo.document(userId).set(userData)
                        storeDataIntoSharePref(userData)
                    }else {
                        Toast.makeText(
                            baseContext,
                            "Creating User failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Creating User failed.", Toast.LENGTH_SHORT).show()
                }
                progressDialog.dismiss()
            }
    }

    private fun storeDataIntoSharePref(userData: HashMap<String, String>) {
        val sharedPreferences = getSharedPreferences("userDataStoreLocal", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        for(ele in userData){
            myEdit.putString(ele.key, ele.value)
        }
        val currentTimestamp: Long = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yy - hh:mm a", Locale.getDefault())
        myEdit.putString("lastUpdate",dateFormat.format(Date(currentTimestamp)))
//        myEdit.putString("ratingsLC",ratingsLC)
//        myEdit.putString("toppercentageLC",toppercentageLC)
//        myEdit.putString("livecontestLC",livecontestLC)
//        myEdit.putString("globalrankingLC",globalrankingLC)
//        myEdit.putString("ratingsCF",ratingsCF)
//        myEdit.putString("ratingCC",ratingCC)

        myEdit.apply()

        val intent:Intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}