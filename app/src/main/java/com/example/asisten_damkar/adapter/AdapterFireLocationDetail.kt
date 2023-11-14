package com.example.asisten_damkar.adapter

import android.graphics.Color
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.asisten_damkar.R
import com.example.asisten_damkar.listener.OnClickAdapter
import com.example.asisten_damkar.response.FireLocationResponse
import java.time.Duration
import java.time.Instant

class AdapterFireLocationDetail(private val items: Array<FireLocationResponse>, private val geocoder: Geocoder, private val listener: OnClickAdapter<FireLocationResponse>): RecyclerView.Adapter<AdapterFireLocationDetail.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_fire_location_detail, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        geocoder.getFromLocation(item.lat, item.lng, 1, GeocodeListener {
            if(!it[0].getAddressLine(0).split(", ")[1].isNullOrEmpty())  {
                holder.place.text = it[0].getAddressLine(0).split(", ")[1]
                return@GeocodeListener
            }
            if(!it[0].locality.isNullOrEmpty()) {
                holder.place.text = it[0].locality.toString()
                return@GeocodeListener
            }
            holder.place.text = "Unknown"
        })
        val createdAtInstant = Instant.ofEpochSecond(item.createdAt.toLong())

        val currentTimeInstant = Instant.now()

        val duration = Duration.between(createdAtInstant, currentTimeInstant)

        val minutesAgo = duration.toMinutes()

        holder.time.text = "$minutesAgo min ago"
        holder.time.setTextColor(if (minutesAgo > 15) Color.parseColor("#B82D1D") else Color.parseColor("#38782E"))
        holder.button.setOnClickListener {
            listener.onClickAdapter(item)
        }
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val place: TextView = itemView.findViewById(R.id.locationPlace)
        val time: TextView = itemView.findViewById(R.id.locationDetailTime)
        val button: LinearLayout = itemView.findViewById(R.id.locationContainer)
    }
}