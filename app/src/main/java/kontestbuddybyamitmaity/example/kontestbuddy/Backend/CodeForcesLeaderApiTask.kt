package kontestbuddybyamitmaity.example.kontestbuddy.Backend

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kontestbuddybyamitmaity.example.kontestbuddy.R
import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class CodeForcesLeaderApiTask(private val callback: (String?) -> Unit) : AsyncTask<String, Void, String?>() {

    override fun doInBackground(vararg params: String): String? {
        val baseUrl = "ZZZZZZ"
        val endpoint = "cfLeader"
        val username = params[0]

        val requestBody = FormBody.Builder()
            .add("userNames", username)
            .build()

        // Create OkHttpClient instance
        val client = OkHttpClient()

        // Build the request
        val request = Request.Builder()
            .url("$baseUrl/$endpoint")
            .post(requestBody)
            .build()

        try {
            // Execute the request and get the response
            val response = client.newCall(request).execute()

            // Check if the response is successful
            if (response.isSuccessful) {
                // Parse the JSON response
                val jsonResponse = response.body?.string()
                return jsonResponse

            } else {
                Log.e("ApiTask", "Request failed with code: ${response.code}")
            }
        } catch (e: IOException) {
            Log.e("ApiTask", "Exception during API request: ${e.message}")
        }

        return null
    }


    override fun onPostExecute(result: String?) {
        // Callback to handle the result
        callback(result)
    }

    private fun parseJsonResponse(jsonResponse: String?): JsonObject? {
        try {
            // Parse the JSON response
            val jsonObject = jsonResponse?.let { JsonParser().parse(it).asJsonObject }

            val parser = JsonParser()
            val jsonArray = parser.parse(jsonResponse).asJsonArray

            for(element in jsonArray){
                Log.e("AMIIIIT",element.asJsonObject.toString())
            }
            return jsonArray.get(0).asJsonObject
        } catch (e: Exception) {
            Log.e("ApiTask", "Error parsing JSON response: ${e.message}")
        }

        return null
    }
}
