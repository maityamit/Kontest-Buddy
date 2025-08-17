package kontestbuddybyamitmaity.example.kontestbuddy.data.model

import com.google.gson.JsonObject

data class UserStats(
    val leetcode: JsonObject? = null,
    val codeforces: JsonObject? = null,
    val codechef: JsonObject? = null
)
