package kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase

import com.google.gson.JsonObject
import kontestbuddybyamitmaity.example.kontestbuddy.data.repository.UserRepository

class GetRankingUseCase(private val userRepository: UserRepository) {
    
    suspend fun execute(userNames: String, platform: String): JsonObject? {
        return when (platform.lowercase()) {
            "leetcode" -> userRepository.fetchLeetCodeRanking(userNames)
            "codeforces" -> userRepository.fetchCodeForcesRanking(userNames)
            "codechef" -> userRepository.fetchCodeChefRanking(userNames)
            else -> null
        }
    }
}
