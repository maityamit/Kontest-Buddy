package kontestbuddybyamitmaity.example.kontestbuddy.questionsStats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R

class QuestionStatAdapter(private val dataList: List<QuestionsStatsActivity.CustomData>) :
    RecyclerView.Adapter<QuestionStatAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.qns_stats_view_layout, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val solved_date: TextView = itemView.findViewById(R.id.qn_stats_solved_date)
        private val no_of_qns: TextView = itemView.findViewById(R.id.qn_solved_no_of_qns)

        fun bind(data: QuestionsStatsActivity.CustomData, position: Int) {
            solved_date.text = data.date
            no_of_qns.text = data.no_of_qns.toString()
        }
    }
}
