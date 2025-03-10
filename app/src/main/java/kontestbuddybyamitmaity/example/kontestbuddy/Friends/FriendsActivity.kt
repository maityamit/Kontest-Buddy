package kontestbuddybyamitmaity.example.kontestbuddy.Friends

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.Services.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class FriendsActivity : AppCompatActivity() {

    private lateinit var friends_recycler_view: RecyclerView
    private lateinit var friendsAdapter: FriendsAdapter
    private lateinit var freindsList: ArrayList<FreindsModel>
    private lateinit var progressDialog: ProgressDialog
    private lateinit var superbase: SupabaseClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        initializations()
        fetchFreinds()

    }

    private fun initializations() {

        superbase = createSupabaseClient(
            supabaseUrl = Constants.SUPERBASE_URL,
            supabaseKey = Constants.SUPERBASE_KEY
        ) {
            install(Postgrest)
        }


        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please waiiit")
        friends_recycler_view = findViewById(R.id.friends_recycler_view)
        freindsList = ArrayList()


        friendsAdapter = FriendsAdapter(freindsList)
        friends_recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        friends_recycler_view.adapter = friendsAdapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchFreinds() {
        progressDialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val outer_user = superbase.postgrest.from("users")
                    .select("user_email, user_codechef, user_codeforces, user_leetcode, user_name, linkedin_profile, is_public")
                    .body

                val users = outer_user.jsonArray
                val newFriendsList = ArrayList<FreindsModel>()


                for (i in 0 until users.size) {

                    val user = users[i].jsonObject
                    val userName = user["user_name"]?.jsonPrimitive?.contentOrNull
                    val userLeetcode = user["user_leetcode"]?.jsonPrimitive?.contentOrNull
                    val userCodeforces = user["user_codeforces"]?.jsonPrimitive?.contentOrNull
                    val userCodechef = user["user_codechef"]?.jsonPrimitive?.contentOrNull
                    val is_public = user["is_public"]?.jsonPrimitive?.contentOrNull
                    val linkedin_profile = user["linkedin_profile"]?.jsonPrimitive?.contentOrNull


                    if(is_public!=null && is_public=="true"){
                        val withdraw = FreindsModel(userName, userLeetcode, userCodeforces, userCodechef, "is_public", linkedin_profile)
                        Log.e("User_AMIT", withdraw.toString())
                        newFriendsList.add(withdraw)
                    }

                }

                withContext(Dispatchers.Main) {
                    freindsList.clear()
                    freindsList.addAll(newFriendsList)
                    friendsAdapter.notifyDataSetChanged()
                    progressDialog.dismiss()
                }


            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressDialog.dismiss()
                    println("Error fetching users: ${e.message}")
                }
            }
        }


    }
}