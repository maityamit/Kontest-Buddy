package kontestbuddybyamitmaity.example.kontestbuddy.data.api

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.IOException
import kontestbuddybyamitmaity.example.kontestbuddy.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request

class APIServices(private val callback: (JsonObject?) -> Unit) :
    AsyncTask<String, Void, JsonObject?>() {

    override fun doInBackground(vararg params: String): JsonObject? {
        val username = params[0]
        val endpoint = params[1]
        val baseUrl = Constants.BASE_URL
        val fullurl = "$baseUrl/$endpoint?userName=$username"

        if (username.isEmpty()) {
            return null
        }

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(fullurl)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val jsonResponse = response.body?.string()
                val isValid = parseJsonResponse(jsonResponse)
                return isValid
            } else {
                Log.e("ApiTask", "Request failed with code: ${response.code}")
            }
        } catch (e: IOException) {
            Log.e("ApiTask", "Exception during API request: ${e.message}")
        }

        return null
    }

    override fun onPostExecute(result: JsonObject?) {
        callback(result)
    }

    private fun parseJsonResponse(jsonResponse: String?): JsonObject? {
        try {
            val jsonObject = jsonResponse?.let { JsonParser().parse(it).asJsonObject }
            return jsonObject
        } catch (e: Exception) {
            Log.e("ApiTask", "Error parsing JSON response: ${e.message}")

            try {
                val jsonArray = jsonResponse?.let { JsonParser().parse(it).asJsonArray }
                val jsonObject = JsonObject()
                jsonObject.add("result", jsonArray)
                return jsonObject
            } catch (e: Exception) {
                Log.e("ApiTask", "Error parsing JSON response: ${e.message}")
            }
        }

        return null
    }
}
