package kontestbuddybyamitmaity.example.kontestbuddy.Backend

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class GFGVerifyApiTask(private val callback: (JsonObject?) -> Unit) : AsyncTask<String, Void, JsonObject?>() {

    override fun doInBackground(vararg params: String): JsonObject? {
        val baseUrl = "https://express-liard-nine.vercel.app"
        val endpoint = "isGFGExist"
        val username = params[0]

        val requestBody = FormBody.Builder()
            .add("userName", username)
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
        // Callback to handle the result
        callback(result)
    }

    private fun parseJsonResponse(jsonResponse: String?): JsonObject? {
        try {
            // Parse the JSON response
            val jsonObject = jsonResponse?.let { JsonParser().parse(it).asJsonObject }
//            Log.e("KLKSK",jsonObject.toString())
//            val isValid = jsonObject?.get("isValid").toString()
            return jsonObject
        } catch (e: Exception) {
            Log.e("ApiTask", "Error parsing JSON response: ${e.message}")
        }

        return null
    }
}