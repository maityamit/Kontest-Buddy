package kontestbuddybyamitmaity.example.kontestbuddy.data.model

data class User(
    val email: String? = null,
    val name: String? = null,
    val leetcodeUsername: String? = null,
    val codeforcesUsername: String? = null,
    val codechefUsername: String? = null,
    val linkedinProfile: String? = null,
    val isPublic: Boolean = false
)
