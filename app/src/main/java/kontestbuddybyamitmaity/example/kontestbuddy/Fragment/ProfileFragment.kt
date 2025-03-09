package kontestbuddybyamitmaity.example.kontestbuddy.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kontestbuddybyamitmaity.example.kontestbuddy.Auth.LoginActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.R


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    lateinit var progressDialog: ProgressDialog



    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_profile, container, false)

        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please wait...")


        auth = FirebaseAuth.getInstance()
        retrieveData(view)

        view.findViewById<RelativeLayout>(R.id.profile_logout_button).setOnClickListener{

            // Create an Alert Dialog that you want to confirm the logout
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout?")
            builder.setPositiveButton("Yes") { _, _ ->
                auth.signOut()
                val intent: Intent = Intent(context,LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.show()
        }

        view.findViewById<ImageView>(R.id.profile_frag_user_edit).setOnClickListener{
            context?.let { it1 -> CustomDialog(it1, this) }?.show()
        }

        val switchButton = view.findViewById<Switch>(R.id.is_profile_public_switch_button)
        val is_public_profile_textview = view.findViewById<TextView>(R.id.is_public_profile_textview)

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                progressDialog.show()
                val userData = hashMapOf(
                    "is_public" to "true"
                )
                val db = Firebase.firestore
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid.toString()
                val userInfo = db.collection("users")

                userInfo.document(userId).update(userData as Map<String, Any>).addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(context,"Preference Updated...",Toast.LENGTH_SHORT).show()
                    is_public_profile_textview.text = "Public Profile: (on)"
                    progressDialog.dismiss()

                    val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val myEdit = sharedPreferences?.edit()
                    if(myEdit != null) {
                        myEdit.putString("is_public",userData["is_public"].toString())
                        myEdit.apply()
                    }

                }.addOnFailureListener{
                    progressDialog.dismiss()
                    Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT).show()
                }
            } else {
                progressDialog.show()
                val userData = hashMapOf(
                    "is_public" to "false"
                )
                val db = Firebase.firestore
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid.toString()
                val userInfo = db.collection("users")

                userInfo.document(userId).update(userData as Map<String, Any>).addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(context,"Preference Updated...",Toast.LENGTH_SHORT).show()
                    is_public_profile_textview.text = "Public Profile: (off)"
                    progressDialog.dismiss()

                    val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val myEdit = sharedPreferences?.edit()
                    if(myEdit != null) {
                        myEdit.putString("is_public",userData["is_public"].toString())
                        myEdit.apply()
                    }
                }.addOnFailureListener{
                    progressDialog.dismiss()
                    Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }


    class CustomDialog(context: Context, private val listener: ProfileFragment) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(context)
        private var leetcode_verify = false
        private var codeforces_verify = false
        private var codechef_verify = false
        @SuppressLint("CutPasteId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.profile_update_layout)

            val sharedPreferences = context.getSharedPreferences("userDataStoreLocal",
                AppCompatActivity.MODE_PRIVATE
            )
            val LCuserName = sharedPreferences?.getString("LCuserName","")
            val CFuserName = sharedPreferences?.getString("CFuserName","")
            val CCuserName = sharedPreferences?.getString("CCuserName","")

            findViewById<EditText>(R.id._register_user_leetcode).setText(if(LCuserName == "null" || LCuserName=="") {""} else {LCuserName?.substring(1, LCuserName.length - 1) ?: "" })
            findViewById<EditText>(R.id._register_user_codeforces).setText(if(CFuserName == "null" || CFuserName=="") {""} else {CFuserName?.substring(1, CFuserName.length - 1) ?: "" })
            findViewById<EditText>(R.id._register_user_codechef).setText(if(CCuserName == "null" || CCuserName=="") {""} else {CCuserName?.substring(1, CCuserName.length - 1) ?: "" })

            findViewById<TextView>(R.id._leetcode_verify_button).setOnClickListener {
                val user_leetcode = findViewById<EditText>(R.id._register_user_leetcode).text.toString()
                if(user_leetcode.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){
                            val txt = findViewById<TextView>(R.id._leetcode_verify_button)
                            txt.text = "Verified"
                            txt.setTextColor(Color.GREEN)
                            findViewById<EditText>(R.id._register_user_leetcode).isEnabled = false
                            findViewById<TextView>(R.id._leetcode_verify_button).isEnabled = false
                            leetcode_verify = true
                        }else{
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()
                    }
                    apiTask.execute(user_leetcode)
                }
            }

            findViewById<TextView>(R.id._codeforces_verify_button).setOnClickListener {

                val user_codeforces = findViewById<EditText>(R.id._register_user_codeforces).text.toString()
                if(user_codeforces.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->

                        if(isValid?.get("isValid").toString()=="\"true\""){
                            val txt = findViewById<TextView>(R.id._codeforces_verify_button)
                            txt.text = "Verified"
                            txt.setTextColor(Color.GREEN)
                            findViewById<EditText>(R.id._register_user_codeforces).isEnabled = false
                            findViewById<TextView>(R.id._codeforces_verify_button).isEnabled = false
                            codeforces_verify = true

                        }else{
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }

                        progressDialog.dismiss()

                    }
                    apiTask.execute(user_codeforces)
                }
            }

            findViewById<TextView>(R.id._codechef_verify_button).setOnClickListener {

                val user_codechef = findViewById<EditText>(R.id._register_user_codechef).text.toString()
                if(user_codechef.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){
                            val txt = findViewById<TextView>(R.id._codechef_verify_button)
                            txt.text = "Verified"
                            txt.setTextColor(Color.GREEN)
                            findViewById<EditText>(R.id._register_user_codechef).isEnabled = false
                            findViewById<TextView>(R.id._codechef_verify_button).isEnabled = false
                            codechef_verify = true

                        }else{
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismiss()

                    }
                    apiTask.execute(user_codechef)
                }
            }



            findViewById<Button>(R.id._register_user_button).setOnClickListener{

                val user_leetcode = findViewById<EditText>(R.id._register_user_leetcode).text.toString()
                val user_codeforces = findViewById<EditText>(R.id._register_user_codeforces).text.toString()
                val user_codechef= findViewById<EditText>(R.id._register_user_codechef).text.toString()
                var user_linkedin = findViewById<EditText>(R.id._register_user_linkedin_account).text.toString()

                if(user_leetcode.isNotBlank() && !leetcode_verify){
                    Toast.makeText(context,"Please verify the LC account",Toast.LENGTH_SHORT).show()
                }else if(user_codeforces.isNotBlank() && !codeforces_verify){
                    Toast.makeText(context,"Please verify the CF account",Toast.LENGTH_SHORT).show()
                }else if(user_codechef.isNotBlank() && !codechef_verify){
                    Toast.makeText(context,"Please verify the CC account",Toast.LENGTH_SHORT).show()
                }else{

                    if(user_linkedin.isNotBlank()){
                        val result = extractLinkedInUsername(user_linkedin)
                        user_linkedin = result ?: user_linkedin
                    }

                    progressDialog.show()
                    var lc = "";
                    var cf = "";
                    var cc = "";
                    if(user_leetcode.isNotBlank()) lc = user_leetcode;
                    if(user_codeforces.isNotBlank()) cf = user_codeforces;
                    if(user_codechef.isNotBlank()) cc = user_codechef;
                    val userData = hashMapOf(
                        "userLeetcode" to lc,
                        "userCodeforces" to cf,
                        "userCodechef" to cc,
                        "linkedin_profile" to user_linkedin
                    )
                    val db = Firebase.firestore
                    val user = FirebaseAuth.getInstance().currentUser
                    val userId = user?.uid.toString()
                    val userInfo = db.collection("users")

                    userInfo.document(userId).update(userData as Map<String, Any>).addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(context,"Data Updated, Please Sign in Once!",Toast.LENGTH_SHORT).show()
                        val auth:FirebaseAuth = FirebaseAuth.getInstance()
                        auth.signOut()
                        val intent:Intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        listener.activity?.finish()

                    }.addOnFailureListener{
                        progressDialog.dismiss()
                        Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }

        private fun extractLinkedInUsername(userLinkedin: String): String? {
            val regex = Regex("""(?:https?://)?(?:www\.)?linkedin\.com/in/([a-zA-Z0-9-_.]+)""")
            val matchResult = regex.find(userLinkedin)
            return matchResult?.groups?.get(1)?.value
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Now you can access the activity property safely
    }


    private fun retrieveData(view: View) {
        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )

        val LCuserName = sharedPreferences?.getString("LCuserName","")
        val CFuserName = sharedPreferences?.getString("CFuserName","")
        val CCuserName = sharedPreferences?.getString("CCuserName","")
        val linkedin_profile = sharedPreferences?.getString("linkedin_profile","")
        val is_public_from_store = sharedPreferences?.getString("is_public","false")

        view.findViewById<TextView>(R.id.profile_frag_userName).text = sharedPreferences?.getString("userName","")
        view.findViewById<TextView>(R.id.profile_frag_userEmail).text = sharedPreferences?.getString("userEmail","")
        view.findViewById<TextView>(R.id.profile_frag_userLC).text = if(LCuserName == "null" || LCuserName=="") {"-"} else {LCuserName?.substring(1, LCuserName.length - 1) ?: "" }
        view.findViewById<TextView>(R.id.profile_frag_userCF).text = if(CFuserName == "null" || CFuserName=="") {"-"} else {CFuserName?.substring(1, CFuserName.length - 1) ?: "" }
        view.findViewById<TextView>(R.id.profile_frag_userCC).text = if(CCuserName == "null" || CCuserName=="") {"-"} else {CCuserName?.substring(1, CCuserName.length - 1) ?: "" }
        view.findViewById<TextView>(R.id.profile_frag_userLinkedin).text = if(linkedin_profile== "null" || linkedin_profile=="") {"-"} else linkedin_profile

        if(is_public_from_store=="false") {
            view.findViewById<TextView>(R.id.is_public_profile_textview).text =
                "Public Profile: (off)"
            view.findViewById<Switch>(R.id.is_profile_public_switch_button).isChecked = false
        }else{
            view.findViewById<TextView>(R.id.is_public_profile_textview).text =
                "Public Profile: (on)"
            view.findViewById<Switch>(R.id.is_profile_public_switch_button).isChecked = true
        }

    }

}