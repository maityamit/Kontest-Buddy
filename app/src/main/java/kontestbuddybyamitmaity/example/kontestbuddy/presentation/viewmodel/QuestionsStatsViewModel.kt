package kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import kontestbuddybyamitmaity.example.kontestbuddy.data.model.QuestionStats
import kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase.GetQuestionsStatsUseCase

class QuestionsStatsViewModel(
    private val getQuestionsStatsUseCase: GetQuestionsStatsUseCase
) : ViewModel() {

    private val _questionsStats = MutableLiveData<List<QuestionStats>>()
    val questionsStats: LiveData<List<QuestionStats>> = _questionsStats

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    fun fetchQuestionsStats(username: String, type: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                setTitle(type)
                
                val result = getQuestionsStatsUseCase.execute(username)
                if (result != null) {
                    val statsList = parseQuestionsStats(result, type)
                    _questionsStats.value = statsList
                } else {
                    _error.value = "Failed to fetch questions statistics"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setTitle(type: String) {
        _title.value = when (type.lowercase()) {
            "daily" -> "Daily Questions Solved"
            "monthly" -> "Monthly Questions Solved"
            "yearly" -> "Yearly Questions Solved"
            else -> "Questions Statistics"
        }
    }

    private fun parseQuestionsStats(result: JsonObject, type: String): List<QuestionStats> {
        val statsList = mutableListOf<QuestionStats>()
        
        try {
            val submissionCalendar = result.get("submissionCalendar")?.asJsonObject
            if (submissionCalendar != null) {
                val entries = submissionCalendar.entrySet()
                
                when (type.lowercase()) {
                    "daily" -> {
                        // Get last 7 days
                        val last7Days = entries.takeLast(7)
                        for (entry in last7Days) {
                            val date = entry.key
                            val questionsSolved = entry.value.asInt
                            statsList.add(QuestionStats(date, questionsSolved))
                        }
                    }
                    "monthly" -> {
                        // Group by month
                        val monthlyStats = mutableMapOf<String, Int>()
                        for (entry in entries) {
                            val date = entry.key
                            val month = date.substring(0, 6) // YYYYMM format
                            val questionsSolved = entry.value.asInt
                            monthlyStats[month] = (monthlyStats[month] ?: 0) + questionsSolved
                        }
                        
                        // Get last 12 months
                        val last12Months = monthlyStats.entries.sortedBy { it.key }.takeLast(12)
                        for (entry in last12Months) {
                            val month = entry.key
                            val formattedMonth = "${month.substring(4, 6)}/${month.substring(0, 4)}"
                            statsList.add(QuestionStats(formattedMonth, entry.value))
                        }
                    }
                    "yearly" -> {
                        // Group by year
                        val yearlyStats = mutableMapOf<String, Int>()
                        for (entry in entries) {
                            val date = entry.key
                            val year = date.substring(0, 4)
                            val questionsSolved = entry.value.asInt
                            yearlyStats[year] = (yearlyStats[year] ?: 0) + questionsSolved
                        }
                        
                        // Get all years
                        val sortedYears = yearlyStats.entries.sortedBy { it.key }
                        for (entry in sortedYears) {
                            statsList.add(QuestionStats(entry.key, entry.value))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "Error parsing questions statistics: ${e.message}"
        }
        
        return statsList
    }
}
