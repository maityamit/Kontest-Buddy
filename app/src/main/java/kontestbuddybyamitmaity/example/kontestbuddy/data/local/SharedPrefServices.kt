package kontestbuddybyamitmaity.example.kontestbuddy.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kontestbuddybyamitmaity.example.kontestbuddy.data.model.UserStats

class SharedPrefServices(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "userDataStoreLocal",
        AppCompatActivity.MODE_PRIVATE
    )

    fun saveUserStats(userStats: UserStats) {
        val editor = sharedPreferences.edit()
        
        // Save LeetCode stats
        userStats.leetcode?.let { leetcode ->
            editor.putString("leetcode_user_name", leetcode.get("userName")?.asString)
            editor.putString("leetcode_attended_contests_count", leetcode.get("attendedContestsCount")?.asString)
            editor.putString("leetcode_rating", leetcode.get("rating")?.asString)
            editor.putString("leetcode_global_ranking", leetcode.get("globalRanking")?.asString)
            editor.putString("leetcode_total_participants", leetcode.get("totalParticipants")?.asString)
            editor.putString("leetcode_top_percentage", leetcode.get("topPercentage")?.asString)
        }

        // Save CodeForces stats
        userStats.codeforces?.let { codeforces ->
            editor.putString("codeforces_user_name", codeforces.get("userName")?.asString)
            editor.putString("codeforces_rating", codeforces.get("rating")?.asString)
            editor.putString("codeforces_max_rating", codeforces.get("maxRating")?.asString)
            editor.putString("codeforces_rank", codeforces.get("rank")?.asString)
            editor.putString("codeforces_max_rank", codeforces.get("maxRank")?.asString)
        }

        // Save CodeChef stats
        userStats.codechef?.let { codechef ->
            editor.putString("codechef_user_name", codechef.get("userName")?.asString)
            editor.putString("codechef_rating", codechef.get("rating")?.asString)
        }

        // Save last update timestamp
        val currentTimestamp = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yy - hh:mm a", Locale.getDefault())
        editor.putString("lastUpdate", dateFormat.format(Date(currentTimestamp)))

        editor.apply()
    }

    fun getUserStats(): UserStats {
        // This would need to be implemented based on how you want to reconstruct UserStats
        // For now, returning empty UserStats
        return UserStats()
    }

    fun saveUserInfo(email: String, name: String, linkedinProfile: String?, isPublic: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putString("user_name", name)
        editor.putString("linkedin_profile", linkedinProfile)
        editor.putString("is_public", isPublic.toString().lowercase())
        editor.apply()
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("user_email", null)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString("user_name", null)
    }

    fun getLeetCodeUsername(): String? {
        return sharedPreferences.getString("leetcode_user_name", null)
    }

    fun getCodeForcesUsername(): String? {
        return sharedPreferences.getString("codeforces_user_name", null)
    }

    fun getCodeChefUsername(): String? {
        return sharedPreferences.getString("codechef_user_name", null)
    }

    fun getLastUpdate(): String? {
        return sharedPreferences.getString("lastUpdate", null)
    }

    fun isProfilePublic(): Boolean {
        return sharedPreferences.getString("is_public", "false")?.lowercase() == "true"
    }

    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }
}
