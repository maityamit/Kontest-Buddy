package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R

class CommonRankingAdapter(private val dataList: List<CommonRankingActivity.RankingModel>) :
    RecyclerView.Adapter<CommonRankingAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.common_custom_leaderboard_view, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position],position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val user_rank: TextView = itemView.findViewById(R.id.common_leader_board_rank)
        private val contest_platform_image: ImageView = itemView.findViewById(R.id.common_leader_board_rank_image)

        private val user_name: TextView = itemView.findViewById(R.id.common_leader_board_rank_user_name)
        private val user_ratings:TextView = itemView.findViewById(R.id.common_leader_board_rank_user_rating)
        private val user_position:TextView = itemView.findViewById(R.id.common_leader_board_rank_user_rank)


        @SuppressLint("SetTextI18n")
        fun bind(data: CommonRankingActivity.RankingModel, position:Int) {
            user_name.text = data.name.substring(1,data.name.length-1)
            user_rank.text = (position+1).toString()
            user_ratings.text = data.rating.toString()
            user_position.text = data.rank.toString()

            if(data.type.lowercase()=="leetcode") {
                contest_platform_image.setImageResource(R.drawable.leetcode)
            }else if(data.type.lowercase()=="codeforces") {
                contest_platform_image.setImageResource(R.drawable.codeforces)
            }else if(data.type.lowercase()=="codechef") {
                contest_platform_image.setImageResource(R.drawable.codechef)
            }
        }
    }
}