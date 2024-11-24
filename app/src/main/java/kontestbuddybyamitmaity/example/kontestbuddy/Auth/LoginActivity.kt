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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeChefVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.MainActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class LoginActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        intialization()

        findViewById<TextView>(R.id.sign_up_redirect).setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }


        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }



        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            val userEmail = findViewById<EditText>(R.id.login_user_email).text.toString()
            val userPassword = findViewById<EditText>(R.id.login_user_pass).text.toString()

            if (userEmail.isBlank()) {
                Toast.makeText(applicationContext, "Enter Your Email", Toast.LENGTH_SHORT).show()
            } else if (userPassword.isBlank()) {
                Toast.makeText(applicationContext, "Enter Your Password", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.show()
                signinWithEmailAndPass(userEmail, userPassword)
            }
        }

        findViewById<TextView>(R.id.forgetPassWordTextView).setOnClickListener {


            val messageBoxView = LayoutInflater.from(this).inflate(R.layout.forget_password, null)
            val emailID = messageBoxView.findViewById<EditText>(R.id.forget_user_email)
            val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)
            val  messageBoxInstance = messageBoxBuilder.show()
            val editTextBox = messageBoxInstance.findViewById<EditText>(R.id.forget_user_email)
            messageBoxInstance.findViewById<Button>(R.id.forgetpasswordButton_button).setOnClickListener {
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
    }

    private fun intialization() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please Waiitt ...")
    }


    private fun signinWithEmailAndPass(userEmail: String, userPassword: String) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    retrieveDataFromtheFirebase()
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

                    val sharedPreferences = getSharedPreferences("userDataStoreLocal", MODE_PRIVATE)
                    val myEdit = sharedPreferences.edit()

                    myEdit.putString("userName",userName)
                    myEdit.putString("userEmail",userEmail)
                    myEdit.apply()

                    retrieveDataUSingAPI(userLeetcode,userCodeforces,userCodechef)

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

    private fun retrieveDataUSingAPI(userLeetcode: String?, userCodeforces: String?, userCodechef: String?) {



        Log.e("RetriveUserNAME_FROM_FIREBASE", "${userLeetcode} + ${userCodeforces} + ${userCodechef} ")

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

        val sharedPreferences = getSharedPreferences("userDataStoreLocal", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()

        val currentTimestamp: Long = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yy - hh:mm a", Locale.getDefault())
        myEdit.putString("lastUpdate",dateFormat.format(Date(currentTimestamp)))


        myEdit.putString("LCuserName",resultLeetCode?.get("userName").toString())
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



//        val allEntries = sharedPreferences.all
//        for ((key, value) in allEntries) {
//            Log.d("ALl_DATA", "Key: $key, Value: $value")
//        }


        myEdit.apply()

    }



}