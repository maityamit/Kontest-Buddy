package kontestbuddybyamitmaity.example.kontestbuddy

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ResourceCustomAdapter(val context:Context,private val dataList: MutableList<ResourceActivity.CustomData>) :
    RecyclerView.Adapter<ResourceCustomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.resources_layout, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position],position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       private val userName: TextView = itemView.findViewById(R.id.coding_sheet_name)
        fun bind(data: ResourceActivity.CustomData, position:Int) {
            userName.text = data.name.toString()
            itemView.setOnClickListener{
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
                context.startActivity(browserIntent)
            }
        }
    }
}