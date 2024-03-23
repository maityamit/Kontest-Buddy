package kontestbuddybyamitmaity.example.kontestbuddy.Ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R
import org.w3c.dom.Text

class LCCustomAdapter(private val dataList: List<LCrankingActivity.CustomData>) :
    RecyclerView.Adapter<LCCustomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lc_custom_leaderboard_view, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position],position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.LCrank_userName)
        private val LCrank_codeFrank:TextView = itemView.findViewById(R.id.LCrank_codeFrank)
        private val LCrank_userRatings:TextView = itemView.findViewById(R.id.LCrank_userRatings)
        private val LCrank_userPosition:TextView = itemView.findViewById(R.id.LCrank_userPosition)

        fun bind(data: LCrankingActivity.CustomData,position:Int) {
            userName.text = data.name
            LCrank_codeFrank.text = (position+1).toString()
            LCrank_userRatings.text = data.rating.toString()
            LCrank_userPosition.text = data.rank.toString()+"%"
        }
    }
}