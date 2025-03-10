package kontestbuddybyamitmaity.example.kontestbuddy.Compare

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.R

class CompareActivity : AppCompatActivity() {
    private lateinit var enter_codeforces_compare_button:CardView
    private lateinit var enter_leetcode_compare_button:CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare)

        initalizations()

        enter_codeforces_compare_button.setOnClickListener {
            CustomDialog2(this).show()
        }
        enter_leetcode_compare_button.setOnClickListener {
            CustomDialog(this).show()
        }
    }

    private fun initalizations() {
        enter_codeforces_compare_button = findViewById(R.id.enter_codeforces_compare_button)
        enter_leetcode_compare_button = findViewById(R.id.enter_leetcode_compare_button)
    }


    class CustomDialog(context: Context) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(this.context)
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
                    Toast.makeText(context,"Enter the UserName", Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if(isValid?.get("isValid")?.asString.toString()=="true"){
                            verify1.text = "Verified"
                            verify1.setTextColor(Color.GREEN)
                            verify1.isEnabled = false
                            editText1.isEnabled = false
                            lcUser1verify = true
                            leetCodeUserName1 = editText1.text.toString()

                        }else{
                            Toast.makeText(context, "This UserName is not valid", Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()
                    }
                    apiTask.execute(user_leetcode, "isLCExist")
                }
            }

            verify2.setOnClickListener {
                val user_leetcode = editText2.text.toString()
                if(user_leetcode.isBlank()){
                    Toast.makeText(context,"Enter the UserName", Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if(isValid?.get("isValid")?.asString.toString()=="true"){
                            verify2.text = "Verified"
                            verify2.setTextColor(Color.GREEN)
                            verify2.isEnabled = false
                            editText2.isEnabled = false
                            lcUser2verify = true
                            leetCodeUserName2 = editText2.text.toString()

                        }else{
                            Toast.makeText(context, "This UserName is not valid", Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()
                    }
                    apiTask.execute(user_leetcode, "isLCExist")
                }
            }
            okButton.setOnClickListener {
                if(lcUser1verify && lcUser2verify){
                    val intent: Intent = Intent(context,LCcompareActivity::class.java)
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

    class CustomDialog2(context: Context) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(this.context)
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
                    Toast.makeText(context,"Enter the UserName", Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){
                            verify1.text = "Verified"
                            verify1.setTextColor(Color.GREEN)
                            verify1.isEnabled = false
                            editText1.isEnabled = false
                            cfUser1verify = true
                            codeforcesUserName1 = editText1.text.toString()

                        }else{
                            Toast.makeText(context, "This UserName is not valid", Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()
                    }
                    apiTask.execute(user_codeforces, "isCFExist")
                }
            }

            verify2.setOnClickListener {
                val user_codeforces = editText2.text.toString()
                if(user_codeforces.isBlank()){
                    Toast.makeText(context,"Enter the UserName", Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){
                            verify2.text = "Verified"
                            verify2.setTextColor(Color.GREEN)
                            verify2.isEnabled = false
                            editText2.isEnabled = false
                            cfUser2verify = true
                            codeforcesUserName2 = editText2.text.toString()

                        }else{
                            Toast.makeText(context, "This UserName is not valid", Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()
                    }
                    apiTask.execute(user_codeforces, "isCFExist")
                }
            }
            okButton.setOnClickListener {
                if(cfUser1verify && cfUser2verify){
                    val intent: Intent = Intent(context, CFcompareActivity::class.java)
                    intent.putExtra("userName1",codeforcesUserName1)
                    intent.putExtra("userName2",codeforcesUserName2)
                    context.startActivity(intent)
                }
            }

        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }
}