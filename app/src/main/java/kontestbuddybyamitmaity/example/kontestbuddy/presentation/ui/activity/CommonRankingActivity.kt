package kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.di.AppModule
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.adapter.CommonRankingAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel.CommonRankingViewModel

class CommonRankingActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var common_ranking_title: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CommonRankingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_ranking)
        
        initialization()
        setupViewModel()
        setupObservers()
        loadRankingData()
    }

    private fun initialization() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please wait")
        
        common_ranking_title = findViewById(R.id.common_ranking_title)
        recyclerView = findViewById(R.id.common_ranking_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupViewModel() {
        viewModel = AppModule.provideCommonRankingViewModel(this)
    }

    private fun setupObservers() {
        viewModel.rankingData.observe(this) { rankingList ->
            recyclerView.adapter = CommonRankingAdapter(rankingList)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.title.observe(this) { title ->
            common_ranking_title.text = title
        }
    }

    private fun loadRankingData() {
        val extras = intent.extras
        val userNames = extras?.getString("userNames")?.dropLast(1) ?: ""
        val type = extras?.getString("type") ?: ""

        if (userNames.isNotEmpty() && type.isNotEmpty()) {
            viewModel.fetchRanking(userNames, type)
        } else {
            Toast.makeText(this, "Invalid data provided", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
