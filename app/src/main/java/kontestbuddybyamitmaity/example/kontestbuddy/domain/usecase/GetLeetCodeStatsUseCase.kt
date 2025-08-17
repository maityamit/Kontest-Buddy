package kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase

import com.google.gson.JsonObject
import kontestbuddybyamitmaity.example.kontestbuddy.data.repository.UserRepository

class GetLeetCodeStatsUseCase(private val userRepository: UserRepository) {
    
    suspend fun execute(username: String): JsonObject? {
        return userRepository.fetchLeetCodeStats(username)
    }
}
