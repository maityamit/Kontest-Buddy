package kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase

import kontestbuddybyamitmaity.example.kontestbuddy.data.model.User
import kontestbuddybyamitmaity.example.kontestbuddy.data.repository.UserRepository

class GetAllPublicUsersUseCase(private val userRepository: UserRepository) {
    
    suspend fun execute(): List<User> {
        return userRepository.getAllPublicUsers()
    }
}
