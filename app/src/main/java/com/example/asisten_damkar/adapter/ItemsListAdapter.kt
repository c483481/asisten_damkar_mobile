package com.example.asisten_damkar.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asisten_damkar.R
import com.example.asisten_damkar.listener.OnClickAdapter
import com.example.asisten_damkar.response.Item

class ItemsListAdapter(private var items: List<Item>, private val listenerOnUpdate: OnClickAdapter<Item>, private val listenerOnDelete: OnClickAdapter<Item>): RecyclerView.Adapter<ItemsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.health.text = if (item.active) "Baik" else "Buruk"
        holder.health.setTextColor(if (item.active) Color.parseColor("#38782E") else Color.parseColor("#3C7C2C"))
        holder.delete.setOnClickListener {
            listenerOnDelete.onClickAdapter(item)
        }
        holder.update.setOnClickListener {
            listenerOnUpdate.onClickAdapter(item)
        }
    }

    fun updateItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.items_name)
        val health: TextView = itemView.findViewById(R.id.items_health)
        val delete: Button = itemView.findViewById(R.id.adapter_delete_button)
        val update: Button = itemView.findViewById(R.id.adapter_update_button)
    }
}