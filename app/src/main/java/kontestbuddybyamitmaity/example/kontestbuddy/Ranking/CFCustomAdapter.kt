package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R


class CFCustomAdapter(private val dataList: List<CFrankingActivity.CustomData>) :
    RecyclerView.Adapter<CFCustomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cf_custom_leaderboard_view, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position],position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.CFrank_userName)
        private val cfrank_codeFrank: TextView = itemView.findViewById(R.id.CFrank_codeFrank)
        private val cfrank_userRatings: TextView = itemView.findViewById(R.id.CFrank_userRatings)
        private val cfrank_userPosition: TextView = itemView.findViewById(R.id.CFrank_userPosition)

        fun bind(data: CFrankingActivity.CustomData,position:Int) {
            userName.text = (data.name).substring(1,(data.name.length-1))
            cfrank_codeFrank.text = (position+1).toString()
            cfrank_userRatings.text = data.rating.toString()
            cfrank_userPosition.text = (data.rank).substring(1,(data.rank.length-1))
        }
    }
}