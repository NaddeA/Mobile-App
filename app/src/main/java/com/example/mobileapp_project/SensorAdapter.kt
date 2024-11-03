package com.example.mobileapp_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SensorAdapter(private val sensorList: List<SensorItem>) : RecyclerView.Adapter<SensorAdapter.SensorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sensor, parent, false)
        return SensorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val sensorItem = sensorList[position]
        holder.sensorTitle.text = sensorItem.title
        holder.sensorDescription.text = sensorItem.description
        holder.sensorIcon.setImageResource(sensorItem.icon)
    }

    override fun getItemCount(): Int = sensorList.size

    class SensorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sensorTitle: TextView = view.findViewById(R.id.sensorTitle)
        val sensorDescription: TextView = view.findViewById(R.id.sensorDescription)
        val sensorIcon: ImageView = view.findViewById(R.id.sensorIcon)
    }
}
