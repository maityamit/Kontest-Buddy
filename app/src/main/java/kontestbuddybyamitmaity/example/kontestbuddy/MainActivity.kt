package kontestbuddybyamitmaity.example.kontestbuddy

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.grpc.Server
import kontestbuddybyamitmaity.example.kontestbuddy.Auth.LoginActivity
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.DummyAPI
import kontestbuddybyamitmaity.example.kontestbuddy.Fragment.HomeFragment
import kontestbuddybyamitmaity.example.kontestbuddy.Fragment.ProfileFragment
import kontestbuddybyamitmaity.example.kontestbuddy.Fragment.SettingsFragment
class MainActivity : AppCompatActivity() {

    lateinit var linearLayout:LinearLayout
    lateinit var navigationBarView: NavigationBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        startTestAPI()

        linearLayout = findViewById(R.id.main_act_nav_layout)
        navigationBarView = findViewById(R.id.bottom_navigation)
        loadFragment(HomeFragment())

        navigationBarView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home_menu -> {
                    // Respond to navigation item 1 click
                    loadFragment(HomeFragment())
                }
                R.id.profile_menu -> {
                    // Respond to navigation item 2 click
                    loadFragment(ProfileFragment())
                }
                R.id.about_menu -> {
                    // Respond to navigation item 2 click
                    loadFragment(SettingsFragment())
                }
            }
            return@setOnItemSelectedListener true
        }
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

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_act_nav_layout,fragment)
        transaction.commit()
    }


}

