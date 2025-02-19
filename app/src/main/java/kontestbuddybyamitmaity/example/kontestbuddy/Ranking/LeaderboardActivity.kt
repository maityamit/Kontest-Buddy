package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeChefVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Compare.LCcompareActivity
import kontestbuddybyamitmaity.example.kontestbuddy.CurrentFriends.CodeForcesFrndAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.CurrentFriends.LeetCodeCurrentFrndAdapter
import kontestbuddybyamitmaity.example.kontestbuddy.Fragment.HomeFragment
import kontestbuddybyamitmaity.example.kontestbuddy.R

class LeaderboardActivity : AppCompatActivity() {

    lateinit var leetcode_leaderboard_edit_friends_button:CardView
    lateinit var leetcode_show_leaderboard_button:CardView
    lateinit var codeforces_leaderboard_edit_friends_button:CardView
    lateinit var codeforces_show_leaderboard_button:CardView
    lateinit var codechef_leaderboard_edit_friends_button:CardView
    lateinit var codechef_show_leaderboard_button:CardView

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Free services")

        leetcode_leaderboard_edit_friends_button = findViewById(R.id.leetcode_leaderboard_edit_friends_button)
        leetcode_show_leaderboard_button = findViewById(R.id.leetcode_show_leaderboard_button)
        codeforces_leaderboard_edit_friends_button = findViewById(R.id.codeforces_leaderboard_edit_friends_button)
        codeforces_show_leaderboard_button = findViewById(R.id.codeforces_show_leaderboard_button)
        codechef_leaderboard_edit_friends_button = findViewById(R.id.codechef_leaderboard_edit_friends_button)
        codechef_show_leaderboard_button = findViewById(R.id.codechef_show_leaderboard_button)

        leetcode_leaderboard_edit_friends_button.setOnClickListener {
            CustomDialog3(this).show()
        }
        codeforces_leaderboard_edit_friends_button.setOnClickListener {
            CustomDialog4(this).show()
        }

        codechef_leaderboard_edit_friends_button.setOnClickListener{
            CustomDialog5(this).show()
        }

        leetcode_show_leaderboard_button.setOnClickListener {
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {

                        val LCleader = document.getString("LCleader")
                        if(LCleader?.isNotBlank()==true){
                            progressDialog.dismiss()
                            val intent:Intent = Intent(this,LCrankingActivity::class.java)
                            intent.putExtra("userNames",LCleader)
                            startActivity(intent)
                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(this,"You have not any friends !!",Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        codeforces_show_leaderboard_button.setOnClickListener {
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {

                        val LCleader = document.getString("CFleader")
                        if(LCleader?.isNotBlank()==true){
                            progressDialog.dismiss()
                            val intent:Intent = Intent(this, CFrankingActivity::class.java)
                            intent.putExtra("userNames",LCleader)
                            startActivity(intent)
                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(this,"You have not any friends !!",Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        codechef_show_leaderboard_button.setOnClickListener{
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {

                        val LCleader = document.getString("CCleader") ?: ""
                        if(LCleader?.isNotBlank()==true){
                            progressDialog.dismiss()
                            val intent:Intent = Intent(this, CCrankingActivity::class.java)
                            intent.putExtra("userNames",LCleader)
                            startActivity(intent)
                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(this,"You have not any friends !!",Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }


    class CustomDialog3(context: Context) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(this.context)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.leetcode_leaderboard_dialoguebox)


            val current_user_list_show_recyclerView_leetcode: RecyclerView = findViewById(R.id.current_user_list_show_recyclerView_leetcode)
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        val LCleader = document.getString("LCleader")
                        // Toast.makeText(context,LCleader.toString(),Toast.LENGTH_SHORT).show()

                        if (LCleader != null) {
                            if (LCleader.isBlank()){
                                Toast.makeText(context,"You haven't any friends yet!",Toast.LENGTH_SHORT).show()
                            }
                        }
                        val list:MutableList<String> = mutableListOf()
                        var temp:String = ""
                        if (LCleader != null) {
                            for(ele in LCleader){
                                if(ele==';'){
                                    list.add(temp)
                                    temp = ""
                                }else{
                                    temp+=ele
                                }
                            }
                        }
                        current_user_list_show_recyclerView_leetcode.layoutManager = LinearLayoutManager(context)
                        current_user_list_show_recyclerView_leetcode.adapter = list.let { it1 ->
                            LeetCodeCurrentFrndAdapter(
                                it1
                            )
                        }
                        progressDialog.dismiss()

                    } else {
                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }

            val leetcode_leaderboard_inputUser1:EditText = findViewById(R.id.leetcode_leaderboard_inputUser1)
            val leetcode_leaderboard_inputUser1_Verify:TextView = findViewById(R.id.leetcode_leaderboard_inputUser1_Verify)
            val leetcode_leaderboard_all_frinds_show:TextView = findViewById(R.id.show_leetcode_current_friends)

            leetcode_leaderboard_inputUser1_Verify.setOnClickListener {
                val strUser:String = leetcode_leaderboard_inputUser1.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    progressDialog.show()
                    val apiTask = LeetCodeVerifyApiTask { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){

                            val db = Firebase.firestore
                            val auth: FirebaseAuth = FirebaseAuth.getInstance()

                            val userID = auth.currentUser?.uid.toString()
                            val docRef = db.collection("users").document(userID)
                            docRef.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    if (document != null) {

                                        var LCleader = document.getString("LCleader")
                                        if(LCleader?.isBlank() == true){
                                            LCleader = strUser+";"
                                        }else if(LCleader?.contains(strUser) == false){
                                            LCleader = LCleader+strUser+";"
                                        }

                                        docRef.update("LCleader",LCleader).addOnSuccessListener {
                                            leetcode_leaderboard_inputUser1.text.clear()
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "User Added Done !", Toast.LENGTH_SHORT)
                                                .show()
                                        }.addOnFailureListener{
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                                .show()
                                        }


                                    } else {
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                    }
                    apiTask.execute(strUser)
                }
            }

            val leetcode_leaderboard_inputUser2:EditText = findViewById(R.id.leetcode_leaderboard_inputUser2)
            val leetcode_leaderboard_inputUser2_Verify:TextView = findViewById(R.id.leetcode_leaderboard_inputUser2_verify)
            leetcode_leaderboard_inputUser2_Verify.setOnClickListener {
                var strUser:String = leetcode_leaderboard_inputUser2.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    strUser+=";"
                    progressDialog.show()
                    val db = Firebase.firestore
                    val auth: FirebaseAuth = FirebaseAuth.getInstance()

                    val userID = auth.currentUser?.uid.toString()
                    val docRef = db.collection("users").document(userID)
                    docRef.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null) {

                                val LCleader = document.getString("LCleader")

                                if(LCleader?.isBlank() == false) {
                                    val resultString:String = LCleader.replace(strUser, "")
                                    docRef.update("LCleader",resultString).addOnSuccessListener {
                                        leetcode_leaderboard_inputUser2.text.clear()
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "User Removed Done !", Toast.LENGTH_SHORT)
                                            .show()
                                    }.addOnFailureListener{
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }else{
                                    progressDialog.dismiss()
                                    Toast.makeText(context, "No Such User in your Friends", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

            leetcode_leaderboard_all_frinds_show.setOnClickListener {
                progressDialog.show()
                val db = Firebase.firestore
                val auth: FirebaseAuth = FirebaseAuth.getInstance()

                val userID = auth.currentUser?.uid.toString()
                val docRef = db.collection("users").document(userID)
                docRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            val LCleader = document.getString("LCleader")
                            // Toast.makeText(context,LCleader.toString(),Toast.LENGTH_SHORT).show()
                            val list:MutableList<String> = mutableListOf()
                            var temp:String = ""
                            if (LCleader != null) {
                                for(ele in LCleader){
                                    if(ele==';'){
                                        list.add(temp)
                                        temp = ""
                                    }else{
                                        temp+=ele
                                    }
                                }
                            }
                            current_user_list_show_recyclerView_leetcode.layoutManager = LinearLayoutManager(context)
                            current_user_list_show_recyclerView_leetcode.adapter = list.let { it1 ->
                                LeetCodeCurrentFrndAdapter(
                                    it1
                                )
                            }
                            progressDialog.dismiss()

                        } else {
                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                .show()
                            progressDialog.dismiss()
                        }
                    } else {
                        Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                }
            }

        }

        override fun dismiss() {
            super.dismiss()
            progressDialog.dismiss()
        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }

    class CustomDialog4(context: Context) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(this.context)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.codeforces_leaderboard_dialoguebox)

            val current_user_list_show_recyclerView_codeforces:RecyclerView = findViewById(R.id.current_user_list_show_recyclerView_codeforces)
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        val LCleader = document.getString("CFleader")
                        // Toast.makeText(context,LCleader.toString(),Toast.LENGTH_SHORT).show()

                        if (LCleader != null) {
                            if (LCleader.isBlank()){
                                Toast.makeText(context,"You haven't any friends yet!",Toast.LENGTH_SHORT).show()
                            }
                        }
                        val list:MutableList<String> = mutableListOf()
                        var temp:String = ""
                        if (LCleader != null) {
                            for(ele in LCleader){
                                if(ele==';'){
                                    list.add(temp)
                                    temp = ""
                                }else{
                                    temp+=ele
                                }
                            }
                        }
                        current_user_list_show_recyclerView_codeforces.layoutManager = LinearLayoutManager(context)
                        current_user_list_show_recyclerView_codeforces.adapter = list.let { it1 ->
                            CodeForcesFrndAdapter(
                                it1
                            )
                        }
                        progressDialog.dismiss()

                    } else {
                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }


            val codeforces_leaderboard_inputUser1:EditText = findViewById(R.id.codeforces_leaderboard_inputUser1)
            val codeforces_leaderboard_inputUser1_Verify:TextView = findViewById(R.id.codeforces_leaderboard_inputUser1_Verify)
            codeforces_leaderboard_inputUser1_Verify.setOnClickListener {
                val strUser:String = codeforces_leaderboard_inputUser1.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    progressDialog.show()
                    val apiTask = CodeForcesVerifyApiTask { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){

                            val db = Firebase.firestore
                            val auth: FirebaseAuth = FirebaseAuth.getInstance()

                            val userID = auth.currentUser?.uid.toString()
                            val docRef = db.collection("users").document(userID)
                            docRef.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    if (document != null) {

                                        var LCleader = document.getString("CFleader")
                                        if(LCleader?.isBlank() == true){
                                            LCleader = strUser+";"
                                        }else if(LCleader?.contains(strUser) == false){
                                            LCleader = LCleader+strUser+";"
                                        }

                                        docRef.update("CFleader",LCleader).addOnSuccessListener {
                                            codeforces_leaderboard_inputUser1.text.clear()
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "User Added Done !", Toast.LENGTH_SHORT)
                                                .show()
                                        }.addOnFailureListener{
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                                .show()
                                        }


                                    } else {
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                    }
                    apiTask.execute(strUser)
                }
            }

            val codeforces_leaderboard_inputUser2:EditText = findViewById(R.id.codeforces_leaderboard_inputUser2)
            val codeforces_leaderboard_inputUser2_Verify:TextView = findViewById(R.id.codeforces_leaderboard_inputUser2_verify)
            codeforces_leaderboard_inputUser2_Verify.setOnClickListener {
                var strUser:String = codeforces_leaderboard_inputUser2.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    strUser+=";"
                    progressDialog.show()
                    val db = Firebase.firestore
                    val auth: FirebaseAuth = FirebaseAuth.getInstance()

                    val userID = auth.currentUser?.uid.toString()
                    val docRef = db.collection("users").document(userID)
                    docRef.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null) {

                                val LCleader = document.getString("CFleader")

                                if(LCleader?.isBlank() == false) {
                                    val resultString:String = LCleader.replace(strUser, "")
                                    docRef.update("CFleader",resultString).addOnSuccessListener {
                                        codeforces_leaderboard_inputUser2.text.clear()
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "User Removed Done !", Toast.LENGTH_SHORT)
                                            .show()
                                    }.addOnFailureListener{
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }else{
                                    progressDialog.dismiss()
                                    Toast.makeText(context, "No Such User in your Friends", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

            val show_codeforces_current_friends:TextView = findViewById(R.id.show_codeforces_current_friends)
            show_codeforces_current_friends.setOnClickListener {
                progressDialog.show()
                val db = Firebase.firestore
                val auth: FirebaseAuth = FirebaseAuth.getInstance()

                val userID = auth.currentUser?.uid.toString()
                val docRef = db.collection("users").document(userID)
                docRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            val LCleader = document.getString("CFleader")
                            // Toast.makeText(context,LCleader.toString(),Toast.LENGTH_SHORT).show()
                            val list:MutableList<String> = mutableListOf()
                            var temp:String = ""
                            if (LCleader != null) {
                                for(ele in LCleader){
                                    if(ele==';'){
                                        list.add(temp)
                                        temp = ""
                                    }else{
                                        temp+=ele
                                    }
                                }
                            }
                            current_user_list_show_recyclerView_codeforces.layoutManager = LinearLayoutManager(context)
                            current_user_list_show_recyclerView_codeforces.adapter = list.let { it1 ->
                                CodeForcesFrndAdapter(
                                    it1
                                )
                            }
                            progressDialog.dismiss()

                        } else {
                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                .show()
                            progressDialog.dismiss()
                        }
                    } else {
                        Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                }
            }


        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }

    class CustomDialog5(context: Context) : Dialog(context) {

        var progressDialog: ProgressDialog = ProgressDialog(this.context)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.codechef_leaderboard_dialoguebox)

            val current_user_list_show_recyclerView_codeforces:RecyclerView = findViewById(R.id.current_user_list_show_recyclerView_codechef)
            progressDialog.show()
            val db = Firebase.firestore
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val userID = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userID)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {

                        val LCleader = document.getString("CCleader") ?: ""

                        if(LCleader.isBlank()) {
                            Toast.makeText(context,"You haven't any friends yet!",Toast.LENGTH_SHORT).show()
                        }

                        val list:MutableList<String> = mutableListOf()
                        var temp:String = ""
                        if (LCleader != "") {
                            for(ele in LCleader){
                                if(ele==';'){
                                    list.add(temp)
                                    temp = ""
                                }else{
                                    temp+=ele
                                }
                            }
                        }
                        current_user_list_show_recyclerView_codeforces.layoutManager = LinearLayoutManager(context)
                        current_user_list_show_recyclerView_codeforces.adapter = list.let { it1 ->
                            CodeForcesFrndAdapter(
                                it1
                            )
                        }
                        progressDialog.dismiss()

                    } else {
                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
            }


            val codeforces_leaderboard_inputUser1:EditText = findViewById(R.id.codechef_leaderboard_inputUser1)
            val codeforces_leaderboard_inputUser1_Verify:TextView = findViewById(R.id.codechef_leaderboard_inputUser1_Verify)
            codeforces_leaderboard_inputUser1_Verify.setOnClickListener {
                val strUser:String = codeforces_leaderboard_inputUser1.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    progressDialog.show()
                    val apiTask = CodeChefVerifyApiTask { isValid ->
                        if(isValid?.get("isValid").toString()=="\"true\""){

                            val db = Firebase.firestore
                            val auth: FirebaseAuth = FirebaseAuth.getInstance()

                            val userID = auth.currentUser?.uid.toString()
                            val docRef = db.collection("users").document(userID)
                            docRef.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    if (document != null) {

                                        var LCleader = document.getString("CCleader") ?: ""
                                        if(LCleader.isBlank()){
                                            LCleader = strUser+";"
                                        }else if(LCleader?.contains(strUser) == false){
                                            LCleader = LCleader+strUser+";"
                                        }

                                        docRef.update("CCleader",LCleader).addOnSuccessListener {
                                            codeforces_leaderboard_inputUser1.text.clear()
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "User Added Done !", Toast.LENGTH_SHORT)
                                                .show()
                                        }.addOnFailureListener{
                                            progressDialog.dismiss()
                                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                                .show()
                                        }


                                    } else {
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                        }else{
                            progressDialog.dismiss()
                            Toast.makeText(context, "This UserName is not valid",Toast.LENGTH_SHORT).show()
                        }
                    }
                    apiTask.execute(strUser)
                }
            }

            val codeforces_leaderboard_inputUser2:EditText = findViewById(R.id.codechef_leaderboard_inputUser2)
            val codeforces_leaderboard_inputUser2_Verify:TextView = findViewById(R.id.codechef_leaderboard_inputUser2_verify)
            codeforces_leaderboard_inputUser2_Verify.setOnClickListener {
                var strUser:String = codeforces_leaderboard_inputUser2.text.toString()
                if(strUser.isBlank()){
                    Toast.makeText(context,"Enter the UserName",Toast.LENGTH_SHORT).show()
                }else{
                    strUser+=";"
                    progressDialog.show()
                    val db = Firebase.firestore
                    val auth: FirebaseAuth = FirebaseAuth.getInstance()

                    val userID = auth.currentUser?.uid.toString()
                    val docRef = db.collection("users").document(userID)
                    docRef.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null) {

                                val LCleader = document.getString("CCleader") ?: ""

                                if(!LCleader.isBlank()) {
                                    val resultString:String = LCleader.replace(strUser, "")
                                    docRef.update("CCleader",resultString).addOnSuccessListener {
                                        codeforces_leaderboard_inputUser2.text.clear()
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "User Removed Done !", Toast.LENGTH_SHORT)
                                            .show()
                                    }.addOnFailureListener{
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }else{
                                    progressDialog.dismiss()
                                    Toast.makeText(context, "No Such User in your Friends", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

            val show_codeforces_current_friends:TextView = findViewById(R.id.show_codechef_current_friends)
            show_codeforces_current_friends.setOnClickListener {
                progressDialog.show()
                val db = Firebase.firestore
                val auth: FirebaseAuth = FirebaseAuth.getInstance()

                val userID = auth.currentUser?.uid.toString()
                val docRef = db.collection("users").document(userID)
                docRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            val LCleader = document.getString("CCleader")
                            // Toast.makeText(context,LCleader.toString(),Toast.LENGTH_SHORT).show()
                            val list:MutableList<String> = mutableListOf()
                            var temp:String = ""
                            if (LCleader != null) {
                                for(ele in LCleader){
                                    if(ele==';'){
                                        list.add(temp)
                                        temp = ""
                                    }else{
                                        temp+=ele
                                    }
                                }
                            }
                            current_user_list_show_recyclerView_codeforces.layoutManager = LinearLayoutManager(context)
                            current_user_list_show_recyclerView_codeforces.adapter = list.let { it1 ->
                                CodeForcesFrndAdapter(
                                    it1
                                )
                            }
                            progressDialog.dismiss()

                        } else {
                            Toast.makeText(context, "No Such document", Toast.LENGTH_SHORT)
                                .show()
                            progressDialog.dismiss()
                        }
                    } else {
                        Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
                }
            }


        }

        interface DialogListener {
            fun onDialogOkButtonClicked(inputText: String)
        }
    }

}