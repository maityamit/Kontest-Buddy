package kontestbuddybyamitmaity.example.kontestbuddy.Auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeChefVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.DummyAPI
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.MainActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Free services")

        startTestAPI()



        findViewById<TextView>(R.id.sign_up_redirect).setOnClickListener {
            val intent: Intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            val user_email = findViewById<EditText>(R.id.login_user_email).text.toString()
            val user_password = findViewById<EditText>(R.id.login_user_pass).text.toString()

            if (user_email.isBlank()) {
                Toast.makeText(applicationContext, "Enter Your Email", Toast.LENGTH_SHORT).show()
            } else if (user_password.isBlank()) {
                Toast.makeText(applicationContext, "Enter Your Password", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.show()
                signinWithEmailAndPass(user_email, user_password)
            }
        }
    }

    private fun signinWithEmailAndPass(userEmail: String, userPassword: String) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    retrieveDataFromtheFirebase()
                    // You can navigate to the next activity or perform other actions here
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
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

                    retrieveDataUSingAPI(userLeetcode,userCodeforces,userCodechef,userName,userEmail)

                } else {
                    Toast.makeText(applicationContext, "No Such document", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, task.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
            }


        }
    }

    private fun retrieveDataUSingAPI(userLeetcode: String?, userCodeforces: String?, userCodechef: String?, userName: String?, userEmail: String?) {

        val sharedPreferences = getSharedPreferences("userDataStoreLocal", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("userName", userName)
        myEdit.putString("userEmail", userEmail)
        myEdit.putString("userLeetcode",userLeetcode)
        myEdit.putString("userCodeforces",userCodeforces)
        myEdit.putString("userCodechef",userCodechef)
        myEdit.apply()

        callApiTasks(
            userLeetCode = userLeetcode,
            userCodeforces = userCodeforces,
            userCodechef = userCodechef,
            onAllApiResultsAvailable = {
                progressDialog.dismiss()
                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        )

    }

    fun callApiTasks(
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
            handleAllApiResults(allResults)
            onAllApiResultsAvailable()
        }
    }

    fun handleAllApiResults(results: List<JsonObject?>) {
        // Handle individual API results here
        val resultLeetCode = results[0]
        val resultCodeforces = results[1]
        val resultCodeChef = results[2]

        val sharedPreferences = getSharedPreferences("userDataStoreLocal", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()

        val currentTimestamp: Long = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yy - hh:mm a", Locale.getDefault())
        myEdit.putString("lastUpdate",dateFormat.format(Date(currentTimestamp)))
        myEdit.putString("ratingsLC",resultLeetCode?.get("rating").toString())
        myEdit.putString("toppercentageLC",resultLeetCode?.get("topPercentage").toString())
        myEdit.putString("livecontestLC",resultLeetCode?.get("attendedContestsCount").toString())
        myEdit.putString("globalrankingLC",resultLeetCode?.get("globalRanking").toString())
        myEdit.putString("ratingsCF",resultCodeforces?.get("rating").toString())
        myEdit.putString("ratingCC",resultCodeChef?.get("rating").toString())

        myEdit.apply()

    }

    private fun startTestAPI() {
        val apiTask = DummyAPI { isValid ->
            if(isValid?.get("message").toString().isNotBlank()){
                Log.e("API_TEST_START","API Started")
            }else{
                Toast.makeText(applicationContext, "API Facing Some Issue",Toast.LENGTH_SHORT).show()
            }
        }
        apiTask.execute("am")
    }


}