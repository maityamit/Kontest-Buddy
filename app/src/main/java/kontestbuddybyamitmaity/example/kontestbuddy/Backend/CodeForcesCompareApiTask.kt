package kontestbuddybyamitmaity.example.kontestbuddy.Backend

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class CodeForcesCompareApiTask(private val callback: (String?) -> Unit) : AsyncTask<String, Void, String?>() {

    override fun doInBackground(vararg params: String): String? {
        val baseUrl = "https://cooperative-jade-sun-hat.cyclic.app/"
        val endpoint = "/cfCompare"
        val username = params[0]

        // Create MultipartBody for form-data
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("userNames", username)
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