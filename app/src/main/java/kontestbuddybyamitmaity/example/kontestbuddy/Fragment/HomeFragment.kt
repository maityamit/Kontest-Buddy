package kontestbuddybyamitmaity.example.kontestbuddy.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kontestbuddybyamitmaity.example.kontestbuddy.R


class HomeFragment : Fragment() {


    lateinit var userName:TextView
    lateinit var ratingsLC:TextView
    lateinit var toppercentageLC:TextView
    lateinit var livecontestLC:TextView
    lateinit var globalrankingLC:TextView
    lateinit var ratingsCF :TextView
    lateinit var ratingCC :TextView
    lateinit var lcUserName :TextView
    lateinit var cfUserName :TextView
    lateinit var ccUserName :TextView
    lateinit var lastUpdateHomeMainPage:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View  = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialization
        initialization(view)

        val sharedPreferences = getActivity()?.getSharedPreferences("userDataStoreLocal",
            AppCompatActivity.MODE_PRIVATE
        )
        //Retrieve Data From Local Storage and Set Data into The Container
        val userFullName:String? = sharedPreferences?.getString("userName","")

        if (userFullName != null) {
            userName.text = "\uD83D\uDC4B Hello, " + userFullName.split(" ")[0]
        }else{
            userName.text = "\uD83D\uDC4B Hello, "
        }
        ratingsLC.text = sharedPreferences?.getString("ratingsLC","")
        toppercentageLC.text = sharedPreferences?.getString("toppercentageLC","")+"%"
        livecontestLC.text = sharedPreferences?.getString("livecontestLC","")
        globalrankingLC.text = sharedPreferences?.getString("globalrankingLC","")
        ratingsCF.text = sharedPreferences?.getString("ratingsCF","")
        ratingCC.text = sharedPreferences?.getString("ratingCC","")
        lcUserName.text = sharedPreferences?.getString("userLeetcode","")
        cfUserName.text = sharedPreferences?.getString("userCodeforces","")
        ccUserName.text = sharedPreferences?.getString("userCodechef","")
        lastUpdateHomeMainPage.text = sharedPreferences?.getString("lastUpdate","")


        // Update the Refresh Button
        // Retrieve the details from firebase -> Call the API to Fetch Latest Data -> set into Container -> Store Local Storage



        return view
    }

    private fun initialization(view: View) {
        userName = view.findViewById(R.id.userNameinMainPage);
        ratingsLC = view.findViewById(R.id.lcUserRatingsMainPage);
        toppercentageLC = view.findViewById(R.id.lcUsertopMainPage);
        livecontestLC = view.findViewById(R.id.lcUsercontestMainPage);
        globalrankingLC = view.findViewById(R.id.lcUserglobalMainPage);
        ratingsCF  = view.findViewById(R.id.cfUserRatingsMainPage);
        ratingCC  = view.findViewById(R.id.ccUserRatingsMainPage);
        lcUserName  = view.findViewById(R.id.lcUserNameMainPage);
        cfUserName  = view.findViewById(R.id.cfUserNameMainPage);
        ccUserName  = view.findViewById(R.id.ccUserNameMainPage);
        lastUpdateHomeMainPage = view.findViewById(R.id.lastUpdateHomeMainPage)

    }

}