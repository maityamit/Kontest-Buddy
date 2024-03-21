package kontestbuddybyamitmaity.example.kontestbuddy.Auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kontestbuddybyamitmaity.example.kontestbuddy.MainActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var leetcode_verify = false
    private var codeforces_verify = false
    private var codechef_verify = false
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        progressDialog= ProgressDialog(this)


        findViewById<TextView>(R.id.already_sign_in).setOnClickListener {
            val intent: Intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.leetcode_verify_button).setOnClickListener {
            val user_leetcode = findViewById<EditText>(R.id.register_user_leetcode).text.toString()

        }

        findViewById<TextView>(R.id.codeforces_verify_button).setOnClickListener {
            val user_codeforces = findViewById<EditText>(R.id.register_user_codeforces).text.toString()

        }

        findViewById<TextView>(R.id.codechef_verify_button).setOnClickListener {
            val user_codechef = findViewById<EditText>(R.id.register_user_codechef).text.toString()

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
            "userCodechef" to userCodechef
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
                        val intent:Intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(
                            baseContext,
                            "Creating User failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Creating User failed.", Toast.LENGTH_SHORT,).show()
                }
                progressDialog.dismiss()
            }
    }
}