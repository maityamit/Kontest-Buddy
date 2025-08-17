package kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase

import kontestbuddybyamitmaity.example.kontestbuddy.data.model.UserStats
import kontestbuddybyamitmaity.example.kontestbuddy.data.repository.UserRepository

class GetUserStatsUseCase(private val userRepository: UserRepository) {
    
    suspend fun execute(
        leetcodeUsername: String?,
        codeforcesUsername: String?,
        codechefUsername: String?
    ): UserStats {
        return userRepository.fetchUserStats(leetcodeUsername, codeforcesUsername, codechefUsername)
    }
}
