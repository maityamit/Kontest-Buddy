package kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.activity.CompareActivity
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.activity.FriendsActivity
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.activity.LeaderboardActivity
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.activity.QuestionsStatsActivity
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog

    // UI Components
    private lateinit var userName_TextView: TextView
    private lateinit var ratingsLC_TextView: TextView
    private lateinit var toppercentageLC_TextView: TextView
    private lateinit var livecontestLC_TextView: TextView
    private lateinit var globalrankingLC_TextView: TextView
    private lateinit var ratingsCF_TextView: TextView
    private lateinit var ratingCC_TextView: TextView
    private lateinit var lcUserName_TextView: TextView
    private lateinit var cfUserName_TextView: TextView
    private lateinit var ccUserName_TextView: TextView
    private lateinit var lastUpdateHomeMainPage_TextView: TextView
    private lateinit var mainHomePageRatingsRefresh_ImageView: ImageView
    private lateinit var LeaderBoardButton_Homepage: LinearLayout
    private lateinit var compareButton_Homepage: LinearLayout
    private lateinit var find_koding_buddy_button: CardView
    private lateinit var daily_coding_stats_via_extension: CardView
    private lateinit var monthly_coding_stats_via_extension: CardView
    private lateinit var yearly_coding_stats_via_extension: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        initialization(view)
        setupObservers()
        setupClickListeners()
        loadInitialData()
        return view
    }

    private fun setupObservers() {
        viewModel.userStats.observe(viewLifecycleOwner) { userStats ->
            updateUI(userStats)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        mainHomePageRatingsRefresh_ImageView.setOnClickListener {
            viewModel.refreshUserStats()
        }

        LeaderBoardButton_Homepage.setOnClickListener {
            val intent = Intent(context, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        compareButton_Homepage.setOnClickListener {
            val intent = Intent(context, CompareActivity::class.java)
            startActivity(intent)
        }

        daily_coding_stats_via_extension.setOnClickListener {
            val intent = Intent(context, QuestionsStatsActivity::class.java)
            intent.putExtra("type", "Daily")
            startActivity(intent)
        }

        monthly_coding_stats_via_extension.setOnClickListener {
            val intent = Intent(context, QuestionsStatsActivity::class.java)
            intent.putExtra("type", "Monthly")
            startActivity(intent)
        }

        yearly_coding_stats_via_extension.setOnClickListener {
            val intent = Intent(context, QuestionsStatsActivity::class.java)
            intent.putExtra("type", "Yearly")
            startActivity(intent)
        }

        find_koding_buddy_button.setOnClickListener {
            find_koding_buddy_actions()
        }
    }

    private fun loadInitialData() {
        viewModel.loadUserStatsFromLocal()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(userStats: kontestbuddybyamitmaity.example.kontestbuddy.data.model.UserStats) {
        // Update LeetCode stats
        userStats.leetcode?.let { leetcode ->
            lcUserName_TextView.text = leetcode.get("userName")?.asString ?: "-"
            ratingsLC_TextView.text = leetcode.get("rating")?.asString?.split(".")?.get(0) ?: "-"
            toppercentageLC_TextView.text = "${leetcode.get("topPercentage")?.asString ?: "-"}%"
            livecontestLC_TextView.text = leetcode.get("attendedContestsCount")?.asString ?: "-"
            globalrankingLC_TextView.text = leetcode.get("globalRanking")?.asString ?: "-"
        }

        // Update CodeForces stats
        userStats.codeforces?.let { codeforces ->
            cfUserName_TextView.text = codeforces.get("userName")?.asString ?: "-"
            ratingsCF_TextView.text = codeforces.get("rating")?.asString ?: "-"
        }

        // Update CodeChef stats
        userStats.codechef?.let { codechef ->
            ccUserName_TextView.text = codechef.get("userName")?.asString ?: "-"
            ratingCC_TextView.text = codechef.get("rating")?.asString ?: "-"
        }
    }

    private fun find_koding_buddy_actions() {
        // This would need to be implemented based on your current logic
        val intent = Intent(context, FriendsActivity::class.java)
        startActivity(intent)
    }

    private fun initialization(view: View) {
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please wait")

        // Initialize UI components
        userName_TextView = view.findViewById(R.id.userNameinMainPage)
        ratingsLC_TextView = view.findViewById(R.id.lcUserRatingsMainPage)
        toppercentageLC_TextView = view.findViewById(R.id.lcUsertopMainPage)
        livecontestLC_TextView = view.findViewById(R.id.lcUsercontestMainPage)
        globalrankingLC_TextView = view.findViewById(R.id.lcUserglobalMainPage)
        ratingsCF_TextView = view.findViewById(R.id.cfUserRatingsMainPage)
        ratingCC_TextView = view.findViewById(R.id.ccUserRatingsMainPage)
        lcUserName_TextView = view.findViewById(R.id.lcUserNameMainPage)
        cfUserName_TextView = view.findViewById(R.id.cfUserNameMainPage)
        ccUserName_TextView = view.findViewById(R.id.ccUserNameMainPage)
        lastUpdateHomeMainPage_TextView = view.findViewById(R.id.lastUpdateHomeMainPage)
        mainHomePageRatingsRefresh_ImageView = view.findViewById(R.id.mainHomePageRatingsRefresh)
        LeaderBoardButton_Homepage = view.findViewById(R.id.leaderBoardButton_Homepage)
        compareButton_Homepage = view.findViewById(R.id.compareButton_Homepage)
        find_koding_buddy_button = view.findViewById(R.id.find_koding_buddy_button)
        daily_coding_stats_via_extension = view.findViewById(R.id.daily_coding_stats_via_extension)
        monthly_coding_stats_via_extension = view.findViewById(R.id.monthly_coding_stats_via_extension)
        yearly_coding_stats_via_extension = view.findViewById(R.id.yearly_coding_stats_via_extension)
    }
}
