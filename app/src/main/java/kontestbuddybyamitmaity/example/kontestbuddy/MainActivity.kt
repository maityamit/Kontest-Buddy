package kontestbuddybyamitmaity.example.kontestbuddy

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import kontestbuddybyamitmaity.example.kontestbuddy.Fragment.HomeFragment
import kontestbuddybyamitmaity.example.kontestbuddy.Fragment.ProfileFragment
import kontestbuddybyamitmaity.example.kontestbuddy.Fragment.SettingsFragment

class MainActivity : AppCompatActivity() {

    lateinit var linearLayout: LinearLayout
    lateinit var navigationBarView: NavigationBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayout = findViewById(R.id.main_act_nav_layout)
        navigationBarView = findViewById(R.id.bottom_navigation)
        loadFragment(HomeFragment())

        navigationBarView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> {
                    loadFragment(HomeFragment())
                }

                R.id.profile_menu -> {
                    loadFragment(ProfileFragment())
                }

                R.id.about_menu -> {
                    loadFragment(SettingsFragment())
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_act_nav_layout, fragment)
        transaction.commit()
    }
}
