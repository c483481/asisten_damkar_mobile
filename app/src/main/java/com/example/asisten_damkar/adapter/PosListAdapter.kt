package com.example.asisten_damkar.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asisten_damkar.R
import com.example.asisten_damkar.listener.OnClickAdapter
import com.example.asisten_damkar.response.PosResponse

class PosListAdapter(private val items: Array<PosResponse>, private val listener: OnClickAdapter<PosResponse>): RecyclerView.Adapter<PosListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list_default, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.active.text = if (item.active) "Active" else "Non Active"
        holder.active.setTextColor(if (item.active) Color.parseColor("#38782E") else Color.parseColor("#3C7C2C"))
        holder.container.setOnClickListener{
            listener.onClickAdapter(item)
        }
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.adapterDefaultName)
        val active: TextView = itemView.findViewById(R.id.adapterDefaultActive)
        val container: LinearLayout = itemView.findViewById(R.id.adapterDefault)
    }
}