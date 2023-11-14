package com.example.asisten_damkar.adapter

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.asisten_damkar.R
import com.example.asisten_damkar.listener.OnClickAdapter
import com.example.asisten_damkar.response.FireLocationResponse
import java.text.SimpleDateFormat
import java.util.Date

class AdapterPemadamFireLocationDetail(private var items: Array<FireLocationResponse>, private val geocoder: Geocoder, private val listener: OnClickAdapter<FireLocationResponse>): RecyclerView.Adapter<AdapterPemadamFireLocationDetail.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val place: TextView = itemView.findViewById(R.id.locationPlacePemadam)
        val time: TextView = itemView.findViewById(R.id.waktu_kebakaran)
        val button: Button = itemView.findViewById(R.id.lihat_pemadam_kebakaran)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_activity_pemadam_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(items: Array<FireLocationResponse>) {
        this.items = items
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(items.isNullOrEmpty()) {
            return
        }
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

        val tanggalString = dateFormat.format(tanggal)

        holder.time.text = "Waktu : $tanggalString"

        holder.button.setOnClickListener {
            listener.onClickAdapter(item)
        }
    }
}