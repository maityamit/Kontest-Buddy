package kontestbuddybyamitmaity.example.kontestbuddy.Agents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kontestbuddybyamitmaity.example.kontestbuddy.R

class DailyStatsCustomAdapter(val context: Context, private val dataList: MutableList<DailystatshowActivity.CustomData>) :
    RecyclerView.Adapter<DailyStatsCustomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_coding_timespent_layout, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position],position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val date: TextView = itemView.findViewById(R.id.daily_stat_show_date)
        private val day: TextView = itemView.findViewById(R.id.daily_stat_show_day)
        private val month: TextView = itemView.findViewById(R.id.daily_stat_show_month)
        private val show_more: RelativeLayout = itemView.findViewById(R.id.daily_stat_show_show_more)
        private val daily_stat_show_time_spent: TextView = itemView.findViewById(R.id.daily_stat_show_time_spent)

        val monthNames = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        fun bind(data: DailystatshowActivity.CustomData, position:Int) {

            date.text = data.date
            val (year, month, day) = data.date.split("-")
            val formattedMonth = monthNames[month.toInt() - 1]
            val formattedDay = day.toInt().toString().padStart(2, '0')
            this.day.text = formattedDay.toString()
            this.month.text = formattedMonth

            var total_time = 0L
            for((_, value) in (data.link_to_time_map) as Map<String, Long>) {
                total_time += value
            }

            val (hours, minutes) = convertMillisToTime(total_time)

            daily_stat_show_time_spent.text = "${hours}h ${minutes}m"

            show_more.setOnClickListener {
                val dataMap: MutableMap<String, String> = mutableMapOf()

                for ((website, value) in data.link_to_time_map) {
                    val (_hours, _minutes) = convertMillisToTime(value)
                    val temp_string = "${_hours}h ${_minutes}m"
                    dataMap[website] = temp_string
                }

                // Convert map to a list of string items
                val listData = dataMap.entries.map { "${it.key}: ${it.value}" }

                // Create a ListView and Adapter for the ListView
                val listAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, listData)

                // Create and show the AlertDialog
                AlertDialog.Builder(context)
                    .setTitle("Website List")
                    .setAdapter(listAdapter) { dialog, which ->
                        // You can handle item click here if needed
                        val selectedItem = listData[which]
                    }
                    .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
                    .show()
            }

        }
    }

    fun convertMillisToTime(ms: Long): Pair<Int, Int> {
        val totalSeconds = ms / 1000  // Convert milliseconds to seconds
        val hours = (totalSeconds / 3600).toInt()  // Calculate hours
        val minutes = ((totalSeconds % 3600) / 60).toInt()  // Calculate remaining minutes
        return Pair(hours, minutes)
    }
}
