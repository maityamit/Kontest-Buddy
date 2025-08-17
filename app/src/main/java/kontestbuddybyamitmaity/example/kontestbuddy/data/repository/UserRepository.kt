package kontestbuddybyamitmaity.example.kontestbuddy.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kontestbuddybyamitmaity.example.kontestbuddy.data.api.APIServices
import kontestbuddybyamitmaity.example.kontestbuddy.data.local.SharedPrefServices
import kontestbuddybyamitmaity.example.kontestbuddy.data.model.User
import kontestbuddybyamitmaity.example.kontestbuddy.data.model.UserStats
import kontestbuddybyamitmaity.example.kontestbuddy.utils.Constants

class UserRepository(
    private val context: Context,
    private val sharedPrefServices: SharedPrefServices
) {
    
    private val superbase: SupabaseClient = createSupabaseClient(
        supabaseUrl = Constants.SUPERBASE_URL,
        supabaseKey = Constants.SUPERBASE_KEY
    ) {
        install(Postgrest)
    }

    suspend fun getUserFromSupabase(userEmail: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val user = superbase.postgrest.from("users")
                    .select(
                        "user_email, user_codechef, " +
                        "user_codeforces, user_leetcode, " +
                        "user_name, linkedin_profile, is_public"
                    ) {
                        eq("user_email", userEmail)
                    }.body.jsonArray[0].jsonObject

                User(
                    email = user["user_email"]?.jsonPrimitive?.contentOrNull,
                    name = user["user_name"]?.jsonPrimitive?.contentOrNull,
                    leetcodeUsername = user["user_leetcode"]?.jsonPrimitive?.contentOrNull,
                    codeforcesUsername = user["user_codeforces"]?.jsonPrimitive?.contentOrNull,
                    codechefUsername = user["user_codechef"]?.jsonPrimitive?.contentOrNull,
                    linkedinProfile = user["linkedin_profile"]?.jsonPrimitive?.contentOrNull,
                    isPublic = user["is_public"]?.jsonPrimitive?.contentOrNull?.lowercase() == "true"
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun fetchUserStats(
        leetcodeUsername: String?,
        codeforcesUsername: String?,
        codechefUsername: String?
    ): UserStats {
        return withContext(Dispatchers.IO) {
            val leetcodeStats = if (!leetcodeUsername.isNullOrEmpty()) {
                APIServices {}.execute(leetcodeUsername, "isLCExist").get()
            } else null

            val codeforcesStats = if (!codeforcesUsername.isNullOrEmpty()) {
                APIServices {}.execute(codeforcesUsername, "isCFExist").get()
            } else null

            val codechefStats = if (!codechefUsername.isNullOrEmpty()) {
                APIServices {}.execute(codechefUsername, "isCCExist").get()
            } else null

            UserStats(
                leetcode = leetcodeStats,
                codeforces = codeforcesStats,
                codechef = codechefStats
            )
        }
    }

    suspend fun fetchLeetCodeStats(username: String): JsonObject? {
        return withContext(Dispatchers.IO) {
            APIServices {}.execute(username, "lcStats").get()
        }
    }

    suspend fun updateLeaderboardData(
        userEmail: String,
        parameter: String,
        value: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                superbase.postgrest.from("users")
                    .update({
                        set(parameter, value)
                    }) {
                        eq("user_email", userEmail)
                    }.body
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getLeaderboardData(userEmail: String, parameter: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val user = superbase.postgrest.from("users")
                    .select("user_email, $parameter") {
                        eq("user_email", userEmail)
                    }.body.jsonArray[0].jsonObject.get(parameter)

                user?.jsonPrimitive?.contentOrNull ?: ""
            } catch (e: Exception) {
                ""
            }
        }
    }

    suspend fun getAllPublicUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                val outer_user = superbase.postgrest.from("users")
                    .select(
                        "user_email, user_codechef, " +
                        "user_codeforces, user_leetcode, " +
                        "user_name, linkedin_profile, is_public"
                    )
                    .body

                val users = outer_user.jsonArray
                val publicUsers = mutableListOf<User>()

                for (i in 0 until users.size) {
                    val user = users[i].jsonObject
                    val isPublic = user["is_public"]?.jsonPrimitive?.contentOrNull

                    if (isPublic != null && isPublic.lowercase() == "true") {
                        publicUsers.add(
                            User(
                                email = user["user_email"]?.jsonPrimitive?.contentOrNull,
                                name = user["user_name"]?.jsonPrimitive?.contentOrNull,
                                leetcodeUsername = user["user_leetcode"]?.jsonPrimitive?.contentOrNull,
                                codeforcesUsername = user["user_codeforces"]?.jsonPrimitive?.contentOrNull,
                                codechefUsername = user["user_codechef"]?.jsonPrimitive?.contentOrNull,
                                linkedinProfile = user["linkedin_profile"]?.jsonPrimitive?.contentOrNull,
                                isPublic = true
                            )
                        )
                    }
                }
                publicUsers
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun saveUserStatsToLocal(userStats: UserStats) {
        sharedPrefServices.saveUserStats(userStats)
    }

    fun getUserStatsFromLocal(): UserStats {
        return sharedPrefServices.getUserStats()
    }

    suspend fun fetchLeetCodeRanking(userNames: String): JsonObject? {
        return withContext(Dispatchers.IO) {
            APIServices {}.execute(userNames, "lcLeader").get()
        }
    }

    suspend fun fetchCodeForcesRanking(userNames: String): JsonObject? {
        return withContext(Dispatchers.IO) {
            APIServices {}.execute(userNames, "cfLeader").get()
        }
    }

    suspend fun fetchCodeChefRanking(userNames: String): JsonObject? {
        return withContext(Dispatchers.IO) {
            APIServices {}.execute(userNames, "ccLeader").get()
        }
    }
}
