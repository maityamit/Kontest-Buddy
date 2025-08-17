package kontestbuddybyamitmaity.example.kontestbuddy.di

import android.content.Context
import kontestbuddybyamitmaity.example.kontestbuddy.data.api.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.data.local.SharedPrefServices
import kontestbuddybyamitmaity.example.kontestbuddy.data.repository.UserRepository
import kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase.GetAllPublicUsersUseCase
import kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase.GetLeetCodeStatsUseCase
import kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase.GetQuestionsStatsUseCase
import kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase.GetRankingUseCase
import kontestbuddybyamitmaity.example.kontestbuddy.domain.usecase.GetUserStatsUseCase
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel.CommonRankingViewModel
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel.FriendsViewModel
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel.HomeViewModel
import kontestbuddybyamitmaity.example.kontestbuddy.presentation.viewmodel.QuestionsStatsViewModel

object AppModule {

    fun provideSharedPrefServices(context: Context): SharedPrefServices {
        return SharedPrefServices(context)
    }

    fun provideUserRepository(context: Context): UserRepository {
        return UserRepository(context, provideSharedPrefServices(context))
    }

    fun provideGetUserStatsUseCase(userRepository: UserRepository): GetUserStatsUseCase {
        return GetUserStatsUseCase(userRepository)
    }

    fun provideGetLeetCodeStatsUseCase(userRepository: UserRepository): GetLeetCodeStatsUseCase {
        return GetLeetCodeStatsUseCase(userRepository)
    }

    fun provideGetAllPublicUsersUseCase(userRepository: UserRepository): GetAllPublicUsersUseCase {
        return GetAllPublicUsersUseCase(userRepository)
    }

    fun provideGetRankingUseCase(userRepository: UserRepository): GetRankingUseCase {
        return GetRankingUseCase(userRepository)
    }

    fun provideGetQuestionsStatsUseCase(userRepository: UserRepository): GetQuestionsStatsUseCase {
        return GetQuestionsStatsUseCase(userRepository)
    }

    fun provideHomeViewModel(context: Context): HomeViewModel {
        val userRepository = provideUserRepository(context)
        val getUserStatsUseCase = provideGetUserStatsUseCase(userRepository)
        return HomeViewModel(userRepository, getUserStatsUseCase)
    }

    fun provideFriendsViewModel(context: Context): FriendsViewModel {
        val userRepository = provideUserRepository(context)
        val getAllPublicUsersUseCase = provideGetAllPublicUsersUseCase(userRepository)
        return FriendsViewModel(getAllPublicUsersUseCase)
    }

    fun provideCommonRankingViewModel(context: Context): CommonRankingViewModel {
        val userRepository = provideUserRepository(context)
        val getRankingUseCase = provideGetRankingUseCase(userRepository)
        return CommonRankingViewModel(getRankingUseCase)
    }

    fun provideQuestionsStatsViewModel(context: Context): QuestionsStatsViewModel {
        val userRepository = provideUserRepository(context)
        val getQuestionsStatsUseCase = provideGetQuestionsStatsUseCase(userRepository)
        return QuestionsStatsViewModel(getQuestionsStatsUseCase)
    }
}
