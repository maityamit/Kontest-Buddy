package kontestbuddybyamitmaity.example.kontestbuddy.utils

class Utils {
    companion object {
        fun isValidEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}
