package kontestbuddybyamitmaity.example.kontestbuddy.Auth

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.MainActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Services.Constants
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

class LoginActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var login_user_email: EditText
    private lateinit var login_user_pass: EditText
    private lateinit var forgetPassWordTextView: TextView
    private lateinit var sign_in_button: Button
    private lateinit var sign_up_redirect: TextView
    private lateinit var superbase: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // If Firebase Auth is already logged in, redirect to MainActivity
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Initialize all the necessary variables
        initialization()

        // Perform all the actions
        actions()
    }

    private fun actions() {
        sign_up_redirect.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        sign_in_button.setOnClickListener {
            if (login_user_email.text.trim().toString().isBlank()) {
                login_user_email.error = "Enter Your Email"
            } else if (login_user_pass.text.trim().toString().isBlank()) {
                login_user_pass.error = "Enter Your Password"
            } else {
                signinWithEmailAndPass(
                    login_user_email.text.toString(),
                    login_user_pass.text.toString()
                )
            }
        }

        forgetPassWordTextView.setOnClickListener {
            forget_password_actions()
        }
    }

    private fun forget_password_actions() {
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.forget_password, null)
        val emailID = messageBoxView.findViewById<EditText>(R.id.forget_user_email)
        val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)
        val messageBoxInstance = messageBoxBuilder.show()
        val editTextBox = messageBoxInstance.findViewById<EditText>(R.id.forget_user_email)
        messageBoxInstance.findViewById<Button>(R.id.forgetpasswordButton_button)
            .setOnClickListener {
                val emailIDString: String = editTextBox.text.toString()

                if (emailIDString.isBlank()) {
                    Toast.makeText(this, "Please Enter Your Email ID !!", Toast.LENGTH_SHORT).show()
                } else {
                    progressDialog.show()
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailIDString)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                emailID.text = null
                                progressDialog.dismiss()
                                messageBoxInstance.dismiss()
                                Toast.makeText(
                                    this,
                                    "Password Reset Email Sent.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }

    private fun initialization() {
        superbase = createSupabaseClient(
            supabaseUrl = Constants.SUPERBASE_URL,
            supabaseKey = Constants.SUPERBASE_KEY
        ) {
            install(Postgrest)
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please Waiitt ...")

        login_user_email = findViewById(R.id.login_user_email)
        login_user_pass = findViewById(R.id.login_user_pass)
        forgetPassWordTextView = findViewById(R.id.forgetPassWordTextView)
        sign_in_button = findViewById(R.id.sign_in_button)
        sign_up_redirect = findViewById(R.id.sign_up_redirect)
    }

    private fun signinWithEmailAndPass(userEmail: String, userPassword: String) {
        progressDialog.show()
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    retrieveDataFromtheSuperBase(userEmail)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
    }

    private fun retrieveDataFromtheSuperBase(userEmail: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = superbase.postgrest.from("users")
                    .select(
                        "user_email, user_codechef, " +
                            "user_codeforces, user_leetcode, " +
                            "user_name, linkedin_profile, is_public"
                    ) {
                        eq("user_email", userEmail)
                    }.body.jsonArray[0].jsonObject

                withContext(Dispatchers.Main) {
                    val sharedPreferences = getSharedPreferences("userDataStoreLocal", MODE_PRIVATE)
                    val myEdit = sharedPreferences.edit()
                    myEdit.putString("user_name", user["user_name"]?.jsonPrimitive?.contentOrNull)
                    myEdit.putString("user_email", user["user_email"]?.jsonPrimitive?.contentOrNull)
                    myEdit.putString(
                        "linkedin_profile",
                        user["linkedin_profile"]?.jsonPrimitive?.contentOrNull
                    )
                    myEdit.putString(
                        "is_public",
                        user["is_public"]?.jsonPrimitive?.contentOrNull.toString().lowercase()
                    )
                    myEdit.apply()

                    retrieveDataUSingAPI(
                        user["user_leetcode"]?.jsonPrimitive?.contentOrNull,
                        user["user_codeforces"]?.jsonPrimitive?.contentOrNull,
                        user["user_codechef"]?.jsonPrimitive?.contentOrNull
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        "Error fetching users: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun retrieveDataUSingAPI(
        userLeetcode: String?,
        userCodeforces: String?,
        userCodechef: String?
    ) {
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

    private fun callApiTasks(
        userLeetCode: String?,
        userCodeforces: String?,
        userCodechef: String?,
        onAllApiResultsAvailable: () -> Unit
    ) {
        coroutineScope.launch {
            val resultLeetCode = async { APIServices {}.execute(userLeetCode, "isLCExist").get() }
            val resultCodeforces =
                async { APIServices {}.execute(userCodeforces, "isCFExist").get() }
            val resultCodeChef = async { APIServices {}.execute(userCodechef, "isCCExist").get() }
            val allResults = awaitAll(resultLeetCode, resultCodeforces, resultCodeChef)
            Log.e("DataAmit", "$allResults")
            handleAllApiResults(allResults)
            onAllApiResultsAvailable()
        }
    }

    private fun handleAllApiResults(results: List<JsonObject?>) {
        // Handle individual API results here
        val resultLeetCode = results[0]
        val resultCodeforces = results[1]
        val resultCodeChef = results[2]

        val sharedPreferences = getSharedPreferences("userDataStoreLocal", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()

        val currentTimestamp: Long = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yy - hh:mm a", Locale.getDefault())
        myEdit.putString("lastUpdate", dateFormat.format(Date(currentTimestamp)))

        myEdit.putString("leetcode_user_name", resultLeetCode?.get("userName")?.asString.toString())
        myEdit.putString(
            "leetcode_attended_contests_count",
            resultLeetCode?.get("attendedContestsCount")?.asString.toString()
        )
        myEdit.putString("leetcode_rating", resultLeetCode?.get("rating")?.asString.toString())
        myEdit.putString(
            "leetcode_global_ranking",
            resultLeetCode?.get("globalRanking")?.asString.toString()
        )
        myEdit.putString(
            "leetcode_total_participants",
            resultLeetCode?.get("totalParticipants")?.asString.toString()
        )
        myEdit.putString(
            "leetcode_top_percentage",
            resultLeetCode?.get("topPercentage")?.asString.toString()
        )

        myEdit.putString(
            "codeforces_user_name",
            resultCodeforces?.get("userName")?.asString.toString()
        )
        myEdit.putString("codeforces_rating", resultCodeforces?.get("rating")?.asString.toString())
        myEdit.putString(
            "codeforces_max_rating",
            resultCodeforces?.get("maxRating")?.asString.toString()
        )
        myEdit.putString("codeforces_rank", resultCodeforces?.get("rank")?.asString.toString())
        myEdit.putString(
            "codeforces_max_rank",
            resultCodeforces?.get("maxRank")?.asString.toString()
        )

        myEdit.putString("codechef_user_name", resultCodeChef?.get("userName")?.asString.toString())
        myEdit.putString("codechef_rating", resultCodeChef?.get("rating")?.asString.toString())

        myEdit.apply()
    }
}
