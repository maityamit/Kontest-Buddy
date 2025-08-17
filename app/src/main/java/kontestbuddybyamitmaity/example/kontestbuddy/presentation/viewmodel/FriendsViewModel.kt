package kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kontestbuddybyamitmaity.example.kontestbuddy.data.model.User
import kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase.GetAllPublicUsersUseCase

class FriendsViewModel(
    private val getAllPublicUsersUseCase: GetAllPublicUsersUseCase
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadPublicUsers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val publicUsers = getAllPublicUsersUseCase.execute()
                _users.value = publicUsers
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load users"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
