package kontestbuddybyamitmaity.example.kontestbuddy.Auth

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.MainActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Services.Constants
import kontestbuddybyamitmaity.example.kontestbuddy.Services.SharedPrefServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var leetcode_verify = false
    private var codeforces_verify = false
    private var codechef_verify = false
    private var leetcode_user_name = ""
    private var leetcode_attended_contests_count = ""
    private var leetcode_rating = ""
    private var leetcode_global_ranking = ""
    private var leetcode_total_participants = ""
    private var leetcode_top_percentage = ""

    private var codeforces_user_name = ""
    private var codeforces_rating = ""
    private var codeforces_max_rating = ""
    private var codeforces_rank = ""
    private var codeforces_max_rank = ""

    private var codechef_user_name = ""
    private var codechef_rating = ""


    // Define all the Input Fields
    private lateinit var user_name:EditText
    private lateinit var user_email:EditText
    private lateinit var user_mobile:EditText
    private lateinit var user_password:EditText
    private lateinit var user_leetcode:EditText
    private lateinit var user_codeforces:EditText
    private lateinit var user_codechef:EditText
    private lateinit var user_linkedin:EditText
    private lateinit var user_register_button:Button
    private lateinit var already_sign_in:TextView
    private lateinit var leetcode_verify_button:TextView
    private lateinit var codeforces_verify_button:TextView
    private lateinit var codechef_verify_button:TextView

    private lateinit var superbase: SupabaseClient


    private lateinit var progressDialog: ProgressDialog
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize all the Input Fields
        intitialization()

        // Perform all the Actions
        actions()

    }

    @SuppressLint("CutPasteId", "SetTextI18n")
    private fun actions() {

        already_sign_in_actions()
        register_button_actions()
        leetcode_verify_button_actions()
        codeforces_verify_button_actions()
        codechef_verify_button_actions()

    }

    private fun codechef_verify_button_actions() {

        codechef_verify_button.setOnClickListener {
            if(user_codechef.text.toString().trim().isBlank()){
                user_codechef.error = "Enter Your Codechef UserName"
            }else {
                progressDialog.show()
                val apiTask = APIServices { isValid ->
                    if(isValid?.get("isValid")?.asString.toString()=="true"){
                        codechef_verify_button.text = "Verified"
                        codechef_verify_button.setTypeface(null, Typeface.BOLD)
                        codechef_verify_button.setTextColor(Color.parseColor("#FF115E14"))

                        user_codechef.isEnabled = false
                        codechef_verify_button.isEnabled = false
                        codechef_verify = true

                        codechef_user_name = isValid?.get("userName")?.asString.toString()
                        codechef_rating = isValid?.get("rating")?.asString.toString()

                    }else{
                        Toast.makeText(applicationContext, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss()

                }
                apiTask.execute(user_codechef.text.toString(), "isCCExist")
            }
        }
    }

    private fun codeforces_verify_button_actions() {

        codeforces_verify_button.setOnClickListener {
            if(user_codeforces.text.toString().trim().isBlank()){
                user_codeforces.error = "Enter Your Codeforces UserName"
            }else {
                progressDialog.show()
                val apiTask = APIServices { isValid ->
                    if(isValid?.get("isValid")?.asString.toString()=="true"){
                        codeforces_verify_button.text = "Verified"
                        codeforces_verify_button.setTypeface(null, Typeface.BOLD)
                        codeforces_verify_button.setTextColor(Color.parseColor("#FF115E14"))

                        user_codeforces.isEnabled = false
                        codeforces_verify_button.isEnabled = false

                        codeforces_verify = true
                        codeforces_user_name = isValid?.get("userName")?.asString.toString()
                        codeforces_rating = isValid?.get("rating")?.asString.toString()
                        codeforces_max_rating = isValid?.get("maxRating")?.asString.toString()
                        codeforces_rank = isValid?.get("rank")?.asString.toString()
                        codeforces_max_rank = isValid?.get("maxRank")?.asString.toString()


                    }else{
                        Toast.makeText(applicationContext, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                    }

                    progressDialog.dismiss()

                }
                apiTask.execute(user_codeforces.text.toString(), "isCFExist")
            }
        }

    }

    private fun leetcode_verify_button_actions() {

        leetcode_verify_button.setOnClickListener {
            if(user_leetcode.text.toString().trim().isEmpty()){
                user_leetcode.error = "Enter Your LeetCode UserName"
            }else {
                progressDialog.show()
                val apiTask = APIServices { isValid ->

                    if(isValid?.get("isValid")?.asString.toString()=="true"){

                        leetcode_verify_button.text = "Verified"
                        leetcode_verify_button.setTypeface(null, Typeface.BOLD)
                        leetcode_verify_button.setTextColor(Color.parseColor("#FF115E14"))

                        user_leetcode.isEnabled = false
                        leetcode_verify_button.isEnabled = false

                        leetcode_verify = true

                        leetcode_user_name = isValid?.get("userName")?.asString.toString()
                        leetcode_attended_contests_count = isValid?.get("attendedContestsCount")?.asString.toString()
                        leetcode_rating = isValid?.get("rating")?.asString.toString()
                        leetcode_global_ranking = isValid?.get("globalRanking")?.asString.toString()
                        leetcode_total_participants = isValid?.get("totalParticipants")?.asString.toString()
                        leetcode_top_percentage = isValid?.get("topPercentage")?.asString.toString()

                    }else{

                        Toast.makeText(applicationContext, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                    }

                    progressDialog.dismiss()
                }
                apiTask.execute(user_leetcode.text.toString(), "isLCExist")
            }
        }
    }

    private fun register_button_actions() {


        user_register_button.setOnClickListener{
            if(user_name.text.toString().trim().isEmpty()){
                user_name.error = "Enter Your Name"
            }else if(user_email.text.toString().trim().isEmpty()){
                user_email.error = "Enter Your Email"
            }else if(user_password.text.toString().trim().isEmpty()){
                user_password.error = "Enter Your Password Number"
            }else if(user_leetcode.text.toString().trim().isNotEmpty() && !leetcode_verify){
                user_leetcode.error = "Please verify the LC account"
            }else if(user_codeforces.text.toString().trim().isNotEmpty() && !codeforces_verify){
                user_codeforces.error = "Please verify the CF account"
            }else if(user_codechef.text.toString().trim().isNotEmpty() && !codechef_verify){
                user_codechef.error = "Please verify the CC account"
            }else{

                var linkedin_profile = ""
                if(user_linkedin.text.toString().trim().isNotEmpty()){
                    val result = extractLinkedInUsername(user_linkedin.text.toString())
                    linkedin_profile = result ?: linkedin_profile
                }

                createANewUserOnFirebase(
                    user_name.text.toString(),
                    user_password.text.toString(),
                    user_email.text.toString(),
                    user_mobile.text.toString(),
                    linkedin_profile
                )
            }
        }
    }

    private fun intitialization() {

        auth = Firebase.auth

        superbase = createSupabaseClient(
            supabaseUrl = Constants.SUPERBASE_URL,
            supabaseKey = Constants.SUPERBASE_KEY
        ) {
            install(Postgrest)
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Waittt !! \n Please waiiit")

        user_name = findViewById(R.id.register_user_name)
        user_email = findViewById(R.id.register_user_email)
        user_mobile = findViewById(R.id.register_user_mobile)
        user_password = findViewById(R.id.register_user_password)
        user_leetcode = findViewById(R.id.register_user_leetcode)
        user_codeforces = findViewById(R.id.register_user_codeforces)
        user_codechef = findViewById(R.id.register_user_codechef)
        user_linkedin = findViewById(R.id.register_user_linkedin)
        user_register_button = findViewById(R.id.register_user_button)
        already_sign_in = findViewById(R.id.already_sign_in)
        leetcode_verify_button = findViewById(R.id.leetcode_verify_button)
        codeforces_verify_button = findViewById(R.id.codeforces_verify_button)
        codechef_verify_button = findViewById(R.id.codechef_verify_button)

    }

    private fun already_sign_in_actions() {
        already_sign_in.setOnClickListener {
            val intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createANewUserOnFirebase(userName: String, userPassword: String, userEmail: String, userMobile: String, linkedin_profile: String) {

        progressDialog.show()

        val userData = hashMapOf(
            "user_name" to userName,
            "user_email" to userEmail,
            "user_mobile" to userMobile,
            "user_leetcode" to leetcode_user_name,
            "user_codeforces" to codeforces_user_name,
            "user_codechef" to codechef_user_name,
            "lc_leader" to "",
            "cf_leader" to "",
            "cc_leader" to "",
            "linkedin_profile" to linkedin_profile,
            "is_public" to "false"
        )


        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = FirebaseAuth.getInstance().currentUser
                    if(user!=null){
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = superbase.postgrest.from("users").insert(
                                    userData
                                )

                                withContext(Dispatchers.Main) {
                                    userData["leetcode_user_name"] = leetcode_user_name;
                                    userData["leetcode_attended_contests_count"] = leetcode_attended_contests_count;
                                    userData["leetcode_rating"] = leetcode_rating;
                                    userData["leetcode_global_ranking"] = leetcode_global_ranking;
                                    userData["leetcode_total_participants"] = leetcode_total_participants;
                                    userData["leetcode_top_percentage"] = leetcode_top_percentage;

                                    userData["codeforces_user_name"] = codeforces_user_name;
                                    userData["codeforces_rating"] = codeforces_rating;
                                    userData["codeforces_max_rating"] = codeforces_max_rating;
                                    userData["codeforces_rank"] = codeforces_rank;
                                    userData["codeforces_max_rank"] = codeforces_max_rank;

                                    userData["codechef_user_name"] = codechef_user_name;
                                    userData["codechef_rating"] = codechef_rating;

                                    val sharedPrefServices = SharedPrefServices(applicationContext)
                                    sharedPrefServices.store_data_in_shared_pref(userData)
                                    open_main_dashboard()
                                    println("User added successfully: $response")
                                }

                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    progressDialog.dismiss()
                                    println("Error inserting user: ${e.message}")
                                }
                            }
                        }
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

    fun extractLinkedInUsername(input: String): String? {
        val regex = Regex("""(?:https?://)?(?:www\.)?linkedin\.com/in/([a-zA-Z0-9-_.]+)""")
        val matchResult = regex.find(input)
        return matchResult?.groups?.get(1)?.value
    }

    private fun open_main_dashboard() {
        val intent:Intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}

