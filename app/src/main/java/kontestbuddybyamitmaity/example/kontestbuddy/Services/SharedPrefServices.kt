package kontestbuddybyamitmaity.example.kontestbuddy.Services

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SharedPrefServices(private val context: Context) {

    public fun store_data_in_shared_pref(data_map: Map<String, String>) {
        val sharedPreferences = context.getSharedPreferences(
            "userDataStoreLocal",
            Context.MODE_PRIVATE
        )
        val myEdit = sharedPreferences.edit()
        val currentTimestamp: Long = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yy - hh:mm a", Locale.getDefault())
        myEdit.putString("lastUpdate", dateFormat.format(Date(currentTimestamp)))

        for ((key, value) in data_map) {
            myEdit.putString(key, value)
        }
        myEdit.apply()
    }
}
