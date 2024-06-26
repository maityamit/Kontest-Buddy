package kontestbuddybyamitmaity.example.kontestbuddy.Backend

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class CodeChefVerifyApiTask(private val callback: (JsonObject?) -> Unit) : AsyncTask<String, Void, JsonObject?>() {

    override fun doInBackground(vararg params: String): JsonObject? {
        val baseUrl = "ZZZZZZ"
        val endpoint = "isCCExist"
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
