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
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kontestbuddybyamitmaity.example.kontestbuddy.Auth.LoginActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Services.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    lateinit var progressDialog: ProgressDialog

    private lateinit var profile_frag_userName: TextView
    private lateinit var profile_frag_userEmail: TextView
    private lateinit var profile_frag_userLC: TextView
    private lateinit var profile_frag_userCF: TextView
    private lateinit var profile_frag_userCC: TextView
    private lateinit var profile_frag_userLinkedin: TextView
    private lateinit var profile_logout_button: RelativeLayout
    private lateinit var profile_frag_user_edit: ImageView
    private lateinit var is_profile_public_switch_button: Switch
    private lateinit var is_public_profile_textview: TextView
    private lateinit var superbase: SupabaseClient


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_profile, container, false)

        initializations(view)
        retrieveData(view)

        actions()

        return view
    }

    private fun actions() {
        profile_logout_button.setOnClickListener{
            logout_actions()
        }

        profile_frag_user_edit.setOnClickListener{
            context?.let { it1 -> CustomDialog(it1, this) }?.show()
        }

        is_profile_public_switch_button.setOnCheckedChangeListener { _, isChecked ->
            set_is_public_profile(isChecked.toString())
        }
    }

    private fun set_is_public_profile(value: String) {

        progressDialog.show()

        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        val myEdit = sharedPreferences?.edit()
        var user_email = sharedPreferences?.getString("user_email","")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                superbase.postgrest.from("users").update({
                    set("is_public", value)
                }) {
                    eq("user_email", user_email.toString())
                }
                if(myEdit != null) {
                    myEdit.putString("is_public",value)
                    myEdit.apply()
                }
                Toast.makeText(context,"Preference Updated...",Toast.LENGTH_SHORT).show()
                is_public_profile_textview.text = if (value=="true") "Public Profile: (on)" else "Public Profile: (off)"
                progressDialog.dismiss()
            } catch (e: Exception) {
                progressDialog.dismiss()
                println("Error updating user: ${e.message}")
            }
        }

    }

    private fun logout_actions() {
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

    private fun initializations(view: View) {

        superbase = createSupabaseClient(
            supabaseUrl = Constants.SUPERBASE_URL,
            supabaseKey = Constants.SUPERBASE_KEY
        ) {
            install(Postgrest)
        }

        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please wait...")
        auth = FirebaseAuth.getInstance()

        profile_frag_userName = view.findViewById(R.id.profile_frag_userName)
        profile_frag_userEmail = view.findViewById(R.id.profile_frag_userEmail)
        profile_frag_userLC = view.findViewById(R.id.profile_frag_userLC)
        profile_frag_userCF = view.findViewById(R.id.profile_frag_userCF)
        profile_frag_userCC = view.findViewById(R.id.profile_frag_userCC)
        profile_frag_userLinkedin = view.findViewById(R.id.profile_frag_userLinkedin)
        profile_logout_button = view.findViewById(R.id.profile_logout_button)
        profile_frag_user_edit = view.findViewById(R.id.profile_frag_user_edit)
        is_profile_public_switch_button = view.findViewById(R.id.is_profile_public_switch_button)
        is_public_profile_textview = view.findViewById(R.id.is_public_profile_textview)

    }

    class CustomDialog(
        context: Context,
        private val listener: ProfileFragment
    ) : Dialog(context) {

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
            var LCuserName = sharedPreferences?.getString("leetcode_user_name",null)
            var CFuserName = sharedPreferences?.getString("codeforces_user_name",null)
            var CCuserName = sharedPreferences?.getString("codechef_user_name",null)
            var linkedin_profile = sharedPreferences?.getString("linkedin_profile",null)

            if(LCuserName!=null && LCuserName=="") LCuserName = null
            if(CFuserName!=null && CFuserName=="") CFuserName = null
            if(CCuserName!=null && CCuserName=="") CCuserName = null
            if(linkedin_profile!=null && linkedin_profile=="") linkedin_profile = null

            findViewById<EditText>(R.id._register_user_leetcode).setText(LCuserName?: "-")
            findViewById<EditText>(R.id._register_user_codeforces).setText(CFuserName?: "-")
            findViewById<EditText>(R.id._register_user_codechef).setText(CCuserName?: "-")
            findViewById<EditText>(R.id._register_user_linkedin_account).setText(linkedin_profile?: "-")

            findViewById<TextView>(R.id._leetcode_verify_button).setOnClickListener {
                val user_leetcode = findViewById<EditText>(R.id._register_user_leetcode)
                if(user_leetcode.text.toString().isBlank()){
                    user_leetcode.error = "Enter the LeetCode UserName"
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if(isValid?.get("isValid")?.asString.toString()=="true"){
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
                    apiTask.execute(user_leetcode.text.toString(), "isLCExist")
                }
            }

            findViewById<TextView>(R.id._codeforces_verify_button).setOnClickListener {

                val user_codeforces = findViewById<EditText>(R.id._register_user_codeforces)
                if(user_codeforces.text.toString().isBlank()){
                    user_codeforces.error = "Enter the Codeforces UserName"
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if(isValid?.get("isValid")?.asString.toString()=="true"){
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
                    apiTask.execute(user_codeforces.text.toString(), "isCFExist")
                }
            }

            findViewById<TextView>(R.id._codechef_verify_button).setOnClickListener {

                val user_codechef = findViewById<EditText>(R.id._register_user_codechef)
                if(user_codechef.text.toString().isBlank()){
                    user_codechef.error = "Enter the CodeChef UserName"
                }else {
                    progressDialog.show()
                    val apiTask = APIServices { isValid ->
                        if(isValid?.get("isValid")?.asString.toString()=="true"){
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
                    apiTask.execute(user_codechef.text.toString(), "isCCExist")
                }
            }



            findViewById<Button>(R.id._register_user_button).setOnClickListener{

                val user_leetcode = findViewById<EditText>(R.id._register_user_leetcode)
                val user_codeforces = findViewById<EditText>(R.id._register_user_codeforces)
                val user_codechef= findViewById<EditText>(R.id._register_user_codechef)
                var user_linkedin = findViewById<EditText>(R.id._register_user_linkedin_account).text.toString()

                if(user_leetcode.text.toString().isNotBlank() && !leetcode_verify){
                    user_leetcode.error = "Please verify the LC account"
                }else if(user_codeforces.text.toString().isNotBlank() && !codeforces_verify){
                    user_codeforces.error = "Please verify the CF account"
                }else if(user_codechef.text.toString().isNotBlank() && !codechef_verify){
                    user_codechef.error = "Please verify the CC account"
                }else{
                    if(user_linkedin.isNotBlank()){
                        val result = extractLinkedInUsername(user_linkedin)
                        user_linkedin = result ?: user_linkedin
                    }
                    progressDialog.show()
                    var lc = "";
                    var cf = "";
                    var cc = "";
                    if(user_leetcode.text.toString().isNotBlank()) lc = user_leetcode.text.toString();
                    if(user_codeforces.text.toString().isNotBlank()) cf = user_codeforces.text.toString();
                    if(user_codechef.text.toString().isNotBlank()) cc = user_codechef.text.toString();


                    val sharedPreferences = context.getSharedPreferences("userDataStoreLocal",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    var user_email = sharedPreferences?.getString("user_email","")

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var ele = listener.superbase.postgrest.from("users").update({
                                set("user_leetcode", lc)
                                set("user_codeforces", cf)
                                set("user_codechef", cc)
                                set("linkedin_profile", user_linkedin)
                            }) {
                                eq("user_email", user_email.toString())
                            }.body

                            withContext(Dispatchers.Main) {
                                Toast.makeText(context,"Data Updated, Please Sign in Once!",Toast.LENGTH_SHORT).show()
                                val auth:FirebaseAuth = FirebaseAuth.getInstance()
                                auth.signOut()
                                val intent:Intent = Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                                listener.activity?.finish()
                                progressDialog.dismiss()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                progressDialog.dismiss()
                                println("Error updating user: ${e.message}")
                            }
                        }
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

        var LCuserName = sharedPreferences?.getString("leetcode_user_name",null)
        var CFuserName = sharedPreferences?.getString("codeforces_user_name",null)
        var CCuserName = sharedPreferences?.getString("codechef_user_name",null)
        var linkedin_profile = sharedPreferences?.getString("linkedin_profile",null)
        val is_public_from_store = sharedPreferences?.getString("is_public","false")

        if(LCuserName!=null && LCuserName=="") LCuserName = null
        if(CFuserName!=null && CFuserName=="") CFuserName = null
        if(CCuserName!=null && CCuserName=="") CCuserName = null
        if(linkedin_profile!=null && linkedin_profile=="") linkedin_profile = null

        profile_frag_userName.text = sharedPreferences?.getString("user_name","-")
        profile_frag_userEmail.text = sharedPreferences?.getString("user_email","-")

        profile_frag_userLC.text = LCuserName?: "-"
        profile_frag_userCF.text = CFuserName?: "-"
        profile_frag_userCC.text = CCuserName?: "-"
        profile_frag_userLinkedin.text = linkedin_profile?: "-"

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