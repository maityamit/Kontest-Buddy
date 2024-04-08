package kontestbuddybyamitmaity.example.kontestbuddy.Auth

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.GFGVerifyApiTask
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
    private var gfg_verify = false
    private var ratingsLC = ""
    private var toppercentageLC = ""
    private var livecontestLC = ""
    private var globalrankingLC = ""
    private var ratingsCF = ""
    private var ratingCC = ""
    private var gfg_rating = ""
    private var gfg_problem_solved = ""
    private var gfg_monthlyCodingScore = ""
    private var gfg_articlesPublished = ""
    private lateinit var progressDialog: ProgressDialog
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Free services")


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
                val apiTask = LeetCodeVerifyApiTask { isValid ->
                    if(isValid?.get("isValid").toString()=="\"true\""){
                        val txt = findViewById<TextView>(R.id.leetcode_verify_button)
                        txt.text = "Verified"
                        txt.setTextColor(Color.GREEN)
                        findViewById<EditText>(R.id.register_user_leetcode).isEnabled = false
                        findViewById<TextView>(R.id.leetcode_verify_button).isEnabled = false
                        leetcode_verify = true
                        ratingsLC = (isValid?.get("rating")).toString()
                        toppercentageLC = isValid?.get("topPercentage").toString()
                        livecontestLC = isValid?.get("attendedContestsCount").toString()
                        globalrankingLC = isValid?.get("globalRanking").toString()
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
                        var txt = findViewById<TextView>(R.id.codeforces_verify_button)
                        txt.text = "Verified"
                        txt.setTextColor(Color.GREEN)
                        findViewById<EditText>(R.id.register_user_codeforces).isEnabled = false
                        findViewById<TextView>(R.id.codeforces_verify_button).isEnabled = false
                        codeforces_verify = true
                        ratingsCF = isValid?.get("rating").toString()

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
                        txt.setTextColor(Color.GREEN)
                        findViewById<EditText>(R.id.register_user_codechef).isEnabled = false
                        findViewById<TextView>(R.id.codechef_verify_button).isEnabled = false
                        codechef_verify = true
                        ratingCC = isValid?.get("rating").toString()

                    }else{
                        Toast.makeText(applicationContext, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss()

                }
                apiTask.execute(user_codechef)
            }
        }

        findViewById<TextView>(R.id.gfg_verify_button).setOnClickListener {

            val user_gfg = findViewById<EditText>(R.id.register_user_gfg).text.toString()
            if(user_gfg.isBlank()){
                Toast.makeText(applicationContext,"Enter the UserName",Toast.LENGTH_SHORT).show()
            }else {
                progressDialog.show()
                val apiTask = GFGVerifyApiTask { isValid ->

                    if(isValid?.get("isValid").toString()=="\"true\""){
                        val txt = findViewById<TextView>(R.id.gfg_verify_button)
                        txt.text = "Verified"
                        txt.setTextColor(Color.GREEN)
                        findViewById<EditText>(R.id.register_user_gfg).isEnabled = false
                        findViewById<TextView>(R.id.gfg_verify_button).isEnabled = false
                        gfg_verify = true

                        gfg_rating = isValid?.get("rating").toString()
                        gfg_problem_solved = isValid?.get("problem_solved").toString()
                        gfg_monthlyCodingScore = isValid?.get("monthlyCodingScore").toString()
                        gfg_articlesPublished = isValid?.get("articlesPublished").toString()

                    }else{
                        Toast.makeText(applicationContext, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss()

                }
                apiTask.execute(user_gfg)
            }
        }


        findViewById<Button>(R.id.register_user_button).setOnClickListener{

            val user_name = findViewById<EditText>(R.id.register_user_name).text.toString()
            val user_email = findViewById<EditText>(R.id.register_user_email).text.toString()
            val user_password = findViewById<EditText>(R.id.register_user_password).text.toString()
            val user_leetcode = findViewById<EditText>(R.id.register_user_leetcode).text.toString()
            val user_codeforces = findViewById<EditText>(R.id.register_user_codeforces).text.toString()
            val user_codechef= findViewById<EditText>(R.id.register_user_codechef).text.toString()

            if(user_name.isBlank()){
                Toast.makeText(applicationContext,"Enter Your Name",Toast.LENGTH_SHORT).show()
            }else if(user_email.isBlank()){
                Toast.makeText(applicationContext,"Enter Your Email",Toast.LENGTH_SHORT).show()
            }else if(user_password.isBlank()){
                Toast.makeText(applicationContext,"Enter Your Password",Toast.LENGTH_SHORT).show()
            }else if(user_leetcode.isNotBlank() && !leetcode_verify){
                Toast.makeText(applicationContext,"Please verify the LC account",Toast.LENGTH_SHORT).show()
            }else if(user_codeforces.isNotBlank() && !codeforces_verify){
                Toast.makeText(applicationContext,"Please verify the CF account",Toast.LENGTH_SHORT).show()
            }else if(user_codechef.isNotBlank() && !codechef_verify){
                Toast.makeText(applicationContext,"Please verify the CC account",Toast.LENGTH_SHORT).show()
            }else{
                progressDialog.show()
                var lc = "";
                var cf = "";
                var cc = "";
                if(user_leetcode.isNotBlank()) lc = user_leetcode;
                if(user_codeforces.isNotBlank()) cf = user_codeforces;
                if(user_codechef.isNotBlank()) cc = user_codechef;
                createANewUserOnFirebase(user_name,user_password,user_email,lc,cf,cc)
            }
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
        myEdit.putString("ratingsLC",ratingsLC)
        myEdit.putString("toppercentageLC",toppercentageLC)
        myEdit.putString("livecontestLC",livecontestLC)
        myEdit.putString("globalrankingLC",globalrankingLC)
        myEdit.putString("ratingsCF",ratingsCF)
        myEdit.putString("ratingCC",ratingCC)
        myEdit.putString("gfg_rating",gfg_rating)
        myEdit.putString("gfg_problem_solved",gfg_problem_solved)
        myEdit.putString("gfg_monthlyCodingScore",gfg_monthlyCodingScore)
        myEdit.putString("gfg_articlesPublished",gfg_articlesPublished)

        myEdit.apply()

        val intent:Intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}