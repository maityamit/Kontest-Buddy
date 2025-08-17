package kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.di.AppModule
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.adapter.QuestionStatAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel.QuestionsStatsViewModel

class QuestionsStatsActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var titleTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: QuestionsStatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_stats)
        
        initialization()
        setupViewModel()
        setupObservers()
        loadQuestionsStats()
    }

    private fun initialization() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Loading questions statistics...")
        
        titleTextView = findViewById(R.id.questions_stats_title)
        recyclerView = findViewById(R.id.questions_stats_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupViewModel() {
        viewModel = AppModule.provideQuestionsStatsViewModel(this)
    }

    private fun setupObservers() {
        viewModel.questionsStats.observe(this) { statsList ->
            recyclerView.adapter = QuestionStatAdapter(statsList)
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
            titleTextView.text = title
        }
    }

    private fun loadQuestionsStats() {
        val extras = intent.extras
        val type = extras?.getString("type") ?: ""
        
        // Get LeetCode username from SharedPreferences or other source
        val leetcodeUsername = getLeetCodeUsername()
        
        if (leetcodeUsername.isNotEmpty() && type.isNotEmpty()) {
            viewModel.fetchQuestionsStats(leetcodeUsername, type)
        } else {
            Toast.makeText(this, "LeetCode username not found or invalid type", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getLeetCodeUsername(): String {
        // This should get the LeetCode username from SharedPreferences or UserRepository
        // For now, returning a placeholder - you'll need to implement this based on your app's logic
        return AppModule.provideSharedPrefServices(this).getLeetCodeUsername() ?: ""
    }
}
