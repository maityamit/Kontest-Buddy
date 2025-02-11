package kontestbuddybyamitmaity.example.kontestbuddy.Friends

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R


class FriendsAdapter(val withdrawList: List<FreindsModel>) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friends_user_name: TextView = itemView.findViewById(R.id.friends_user_name)
        val friends_leetcode_name: TextView = itemView.findViewById(R.id.friends_leetcode_name)
        var friends_codeforces_name: TextView = itemView.findViewById(R.id.friends_codeforces_name)
        var friends_codechef_name: TextView = itemView.findViewById(R.id.friends_codechef_name)
        var friends_linkedin_name: ImageView = itemView.findViewById(R.id.friends_linkedin_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friends_show_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val withdraw = withdrawList[position]

        holder.friends_user_name.text = withdraw.userName
        holder.friends_leetcode_name.text = withdraw.userLeetcode
        holder.friends_codeforces_name.text = withdraw.userCodeforces
        holder.friends_codechef_name.text = withdraw.userCodechef

        if(withdraw.userLeetcode.isEmpty()) holder.friends_leetcode_name.text = "-"
        if(withdraw.userCodeforces.isEmpty()) holder.friends_codeforces_name.text = "-"
        if(withdraw.userCodechef.isEmpty()) holder.friends_codechef_name.text = "-"


        holder.friends_linkedin_name.setOnClickListener {
            if(withdraw.linkedin_profile.isNotBlank()){
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/${withdraw.linkedin_profile}/"))
                holder.itemView.context.startActivity(browserIntent)
            }else{
                Toast.makeText(holder.itemView.context, "No LinkedIn Profile Found", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun getItemCount(): Int {
        return withdrawList.size
    }
}