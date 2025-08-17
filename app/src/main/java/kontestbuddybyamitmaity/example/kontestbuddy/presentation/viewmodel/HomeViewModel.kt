package kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kontestbuddybyamitmaity.example.kontestbuddy.data.model.User
import kontestbuddybyamitmaity.example.kontestbuddy.data.model.UserStats
import kontestbuddybyamitmaity.example.kontestbuddy.data.repository.UserRepository
import kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase.GetUserStatsUseCase

class HomeViewModel(
    private val userRepository: UserRepository,
    private val getUserStatsUseCase: GetUserStatsUseCase
) : ViewModel() {

    private val _userStats = MutableLiveData<UserStats>()
    val userStats: LiveData<UserStats> = _userStats

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun refreshUserStats() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                val user = userRepository.getUserFromSupabase(userRepository.getUserEmail() ?: "")
                if (user != null) {
                    val stats = getUserStatsUseCase.execute(
                        user.leetcodeUsername,
                        user.codeforcesUsername,
                        user.codechefUsername
                    )
                    _userStats.value = stats
                    userRepository.saveUserStatsToLocal(stats)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUserStatsFromLocal() {
        val stats = userRepository.getUserStatsFromLocal()
        _userStats.value = stats
    }
}
