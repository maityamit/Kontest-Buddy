package kontestbuddybyamitmaity.example.kontestbuddy.Backend

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kontestbuddybyamitmaity.example.kontestbuddy.R
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class LeetCodeVerifyApiTask(private val callback: (JsonObject?) -> Unit) : AsyncTask<String, Void, JsonObject?>() {

    override fun doInBackground(vararg params: String): JsonObject? {

        val baseUrl = "https://kontest-jdca.onrender.com"
        val endpoint = "isLCExist"
        val username = params[0]

        // Create MultipartBody for form-data
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("userName", username)
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

            Log.e("AMITA",response.toString())

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