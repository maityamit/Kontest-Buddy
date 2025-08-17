package kontestbuddybyamitmaity.example.kontestbuddy.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R
import kontestbuddybyamitmaity.example.kontestbuddy.data.model.RankingModel

class CommonRankingAdapter(private val dataList: List<RankingModel>) :
    RecyclerView.Adapter<CommonRankingAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.common_custom_leaderboard_view, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val user_rank: TextView = itemView.findViewById(R.id.common_leader_board_rank)
        private val contest_platform_image: ImageView = itemView.findViewById(
            R.id.common_leader_board_rank_image
        )
        private val user_name: TextView = itemView.findViewById(
            R.id.common_leader_board_rank_user_name
        )
        private val user_ratings: TextView = itemView.findViewById(
            R.id.common_leader_board_rank_user_rating
        )
        private val user_position: TextView = itemView.findViewById(
            R.id.common_leader_board_rank_user_rank
        )

        @SuppressLint("SetTextI18n")
        fun bind(data: RankingModel, position: Int) {
            user_name.text = data.username
            user_rank.text = (position + 1).toString()
            user_ratings.text = data.rating
            user_position.text = data.rank

            when (data.platform.lowercase()) {
                "leetcode" -> contest_platform_image.setImageResource(R.drawable.leetcode)
                "codeforces" -> contest_platform_image.setImageResource(R.drawable.codeforces)
                "codechef" -> contest_platform_image.setImageResource(R.drawable.codechef)
            }
        }
    }
}
