package com.example.asisten_damkar.adapter

import android.graphics.Color
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.asisten_damkar.R
import com.example.asisten_damkar.response.FireLocationResponse
import java.text.SimpleDateFormat
import java.util.Date

class AdapterFireLocationList(private val items: Array<FireLocationResponse>, private val geocoder: Geocoder): RecyclerView.Adapter<AdapterFireLocationList.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_activity_infomation, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        geocoder.getFromLocation(item.lat, item.lng, 1, Geocoder.GeocodeListener {
            if (!it[0].getAddressLine(0).split(", ")[1].isNullOrEmpty()) {
                holder.place.text = it[0].getAddressLine(0).split(", ")[1]
                return@GeocodeListener
            }
            if (!it[0].locality.isNullOrEmpty()) {
                holder.place.text = it[0].locality.toString()
                return@GeocodeListener
            }
            holder.place.text = "Unknown"
        })

        val tanggal = Date(item.createdAt.toLong() * 1000)

        val dateFormat = SimpleDateFormat("HH:mm")

        val dateFormatNew = SimpleDateFormat("dd-MM-yyyy")

        val formattedTime = dateFormat.format(tanggal)

        var value = formattedTime

        if(item.arriveAt != null) {
            val arriveAt = Date(item.arriveAt.toLong() * 1000)

            val formattedArriveAt = dateFormat.format(arriveAt)

            value += " $formattedArriveAt"



            val formattedDate = dateFormatNew.format(arriveAt)

            holder.timeStart.text = value

            holder.timeEnd.text = formattedDate

            holder.status.text = item.status
            return
        }

        holder.timeStart.text = value

        val formattedDate = dateFormatNew.format(tanggal)
        holder.timeEnd.text = formattedDate

        holder.status.text = item.status
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val place: TextView = itemView.findViewById(R.id.location)
        val timeStart: TextView = itemView.findViewById(R.id.waktu_pelaksanaan)
        val timeEnd: TextView = itemView.findViewById(R.id.waktu_selesai)
        val status: TextView = itemView.findViewById(R.id.status)
    }


}