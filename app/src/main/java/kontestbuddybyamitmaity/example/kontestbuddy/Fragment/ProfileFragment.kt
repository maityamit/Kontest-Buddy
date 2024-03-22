package kontestbuddybyamitmaity.example.kontestbuddy.Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kontestbuddybyamitmaity.example.kontestbuddy.Auth.LoginActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_profile, container, false)

        progressDialog = ProgressDialog(context)
        auth = FirebaseAuth.getInstance()
        retrieveData(view)

        view.findViewById<RelativeLayout>(R.id.profile_logout_button).setOnClickListener{
            auth.signOut()
            val intent: Intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun retrieveData(view: View) {

        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        view.findViewById<TextView>(R.id.profile_frag_userName).text = sharedPreferences?.getString("userName","")
        view.findViewById<TextView>(R.id.profile_frag_userEmail).text = sharedPreferences?.getString("userEmail","")
        view.findViewById<TextView>(R.id.profile_frag_userLC).text = sharedPreferences?.getString("userLeetcode","")
        view.findViewById<TextView>(R.id.profile_frag_userCF).text = sharedPreferences?.getString("userCodeforces","")
        view.findViewById<TextView>(R.id.profile_frag_userCC).text = sharedPreferences?.getString("userCodechef","")
    }
}