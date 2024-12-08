package kontestbuddybyamitmaity.example.kontestbuddy.Friends

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kontestbuddybyamitmaity.example.kontestbuddy.R

class FriendsActivity : AppCompatActivity() {

    private lateinit var friends_recycler_view: RecyclerView
    private lateinit var friendsAdapter: FriendsAdapter
    private lateinit var freindsList: ArrayList<FreindsModel>
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)


        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Please waiiit")
        friends_recycler_view = findViewById(R.id.friends_recycler_view)
        freindsList = ArrayList()

        fetchFreinds()

        friendsAdapter = FriendsAdapter(freindsList)
        friends_recycler_view.layoutManager = LinearLayoutManager(this)
        friends_recycler_view.adapter = friendsAdapter



    }

    private fun fetchFreinds() {
        val db = FirebaseFirestore.getInstance()

        progressDialog.show()

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                freindsList.clear()

                for (document in result) {
                    var userName = document.getString("userName")
                    var userLeetcode = document.getString("userLeetcode")
                    var userCodeforces = document.getString("userCodeforces")
                    var userCodechef = document.getString("userCodechef")
                    val is_public = document.getString("is_public")
                    var linkedin_profile = document.getString("linkedin_profile")

                    if(userName==null) userName = "-"
                    if(userLeetcode == null) userLeetcode = "-"
                    if(userCodeforces == null) userCodeforces = "-"
                    if(userCodechef == null) userCodechef = "-"
                    if(linkedin_profile == null) linkedin_profile = ""

                    if(is_public!=null && is_public=="true"){
                        val withdraw = FreindsModel(userName, userLeetcode, userCodeforces, userCodechef, "is_public", linkedin_profile)
                        freindsList.add(withdraw)
                    }

                }

                friendsAdapter.notifyDataSetChanged()

                progressDialog.dismiss()


            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Log.w("WithdrawFragment", "Error getting documents.", exception)
            }
    }
}