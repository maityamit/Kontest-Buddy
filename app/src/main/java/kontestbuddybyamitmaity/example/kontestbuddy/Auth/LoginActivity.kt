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
import kontestbuddybyamitmaity.example.kontestbuddy.MainActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R

class LoginActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressDialog= ProgressDialog(this)

        findViewById<TextView>(R.id.sign_up_redirect).setOnClickListener {
            val intent: Intent = Intent(applicationContext,RegisterActivity::class.java)
            startActivity(intent)
        }
        if (FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.sign_in_button).setOnClickListener{
            val user_email = findViewById<EditText>(R.id.login_user_email).text.toString()
            val user_password = findViewById<EditText>(R.id.login_user_pass).text.toString()

            if(user_email.isBlank()){
                Toast.makeText(applicationContext,"Enter Your Email", Toast.LENGTH_SHORT).show()
            }else if(user_password.isBlank()){
                Toast.makeText(applicationContext,"Enter Your Password",Toast.LENGTH_SHORT).show()
            }else{
                progressDialog.show()
                signinWithEmailAndPass(user_email,user_password)
            }
        }
    }
    private fun signinWithEmailAndPass(userEmail: String, userPassword: String) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    progressDialog.dismiss()
                    val intent:Intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    // You can navigate to the next activity or perform other actions here
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
    }
}