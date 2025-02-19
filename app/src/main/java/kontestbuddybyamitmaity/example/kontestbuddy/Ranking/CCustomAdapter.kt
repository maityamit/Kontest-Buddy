package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R

class CCustomAdapter(private val dataList: List<CCrankingActivity.CustomData>) :
    RecyclerView.Adapter<CCustomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cc_custom_leaderboard_view, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position],position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.CCrank_userName)
        private val LCrank_codeFrank:TextView = itemView.findViewById(R.id.CCrank_codeFrank)
        private val LCrank_userRatings:TextView = itemView.findViewById(R.id.CCrank_userRatings)


        fun bind(data: CCrankingActivity.CustomData,position:Int) {
            userName.text = data.username.substring(1,data.username.length-1)
            LCrank_codeFrank.text = (position+1).toString()
            LCrank_userRatings.text = data.rating.toString()
        }
    }
}