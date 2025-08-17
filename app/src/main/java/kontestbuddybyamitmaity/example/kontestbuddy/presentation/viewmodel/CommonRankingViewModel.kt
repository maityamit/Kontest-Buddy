package kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import kontestbuddybyamitmaity.example.kontestbuddy.data.model.RankingModel
import kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase.GetRankingUseCase

class CommonRankingViewModel(
    private val getRankingUseCase: GetRankingUseCase
) : ViewModel() {

    private val _rankingData = MutableLiveData<List<RankingModel>>()
    val rankingData: LiveData<List<RankingModel>> = _rankingData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    fun fetchRanking(userNames: String, platform: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                setTitle(platform)
                
                val result = getRankingUseCase.execute(userNames, platform)
                if (result != null) {
                    val rankingList = parseRankingData(result, platform)
                    _rankingData.value = rankingList.sortedByDescending { it.rating }
                } else {
                    _error.value = "Failed to fetch ranking data"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setTitle(platform: String) {
        _title.value = when (platform.lowercase()) {
            "leetcode" -> "LeetCode Ranking"
            "codeforces" -> "CodeForces Ranking"
            "codechef" -> "CodeChef Ranking"
            else -> "Ranking"
        }
    }

    private fun parseRankingData(result: JsonObject, platform: String): List<RankingModel> {
        val rankingList = mutableListOf<RankingModel>()
        val users = result.get("result")?.asJsonArray

        if (users != null) {
            for (user in users) {
                val rankingModel = when (platform.lowercase()) {
                    "leetcode" -> parseLeetCodeUser(user.asJsonObject)
                    "codeforces" -> parseCodeForcesUser(user.asJsonObject)
                    "codechef" -> parseCodeChefUser(user.asJsonObject)
                    else -> null
                }
                rankingModel?.let { rankingList.add(it) }
            }
        }

        return rankingList
    }

    private fun parseLeetCodeUser(user: JsonObject): RankingModel? {
        return try {
            val userData = user.get("result")?.asJsonArray?.get(0)?.asJsonObject
            if (userData != null) {
                val username = userData.get("username")?.asString?.removeSurrounding("\"") ?: ""
                val rating = userData.get("rating")?.asString?.split(".")?.get(0) ?: "0"
                val rank = userData.get("globalRanking")?.asString ?: ""
                
                RankingModel(
                    username = username,
                    rating = rating,
                    rank = rank,
                    platform = "leetcode"
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun parseCodeForcesUser(user: JsonObject): RankingModel? {
        return try {
            val username = user.get("username")?.asString?.removeSurrounding("\"") ?: ""
            val rating = user.get("rating")?.asString ?: "0"
            val rank = user.get("rank")?.asString?.removeSurrounding("\"") ?: ""
            
            RankingModel(
                username = username,
                rating = rating,
                rank = rank,
                platform = "codeforces"
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun parseCodeChefUser(user: JsonObject): RankingModel? {
        return try {
            val userData = user.get("result")?.asJsonArray?.get(0)?.asJsonObject
            if (userData != null) {
                val username = userData.get("username")?.asString?.removeSurrounding("\"") ?: ""
                val rating = userData.get("rating")?.asString?.removeSurrounding("\"") ?: "0"
                
                RankingModel(
                    username = username,
                    rating = rating,
                    rank = "",
                    platform = "codechef"
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
