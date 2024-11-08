package com.example.mobileapp_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappproject.R

class SensorAdapter(
    private val sensorList: List<SensorItem>,
    private val onItemClick: (SensorItem) -> Unit // Klicklyssnare för varje item
) : RecyclerView.Adapter<SensorAdapter.SensorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sensor, parent, false)
        return SensorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val sensorItem = sensorList[position]
        holder.sensorTitle.text = sensorItem.title
        holder.sensorDescription.text = sensorItem.description
        holder.sensorIcon.setImageResource(sensorItem.icon)

        // Hantera klick på itemView
        holder.itemView.setOnClickListener {
            onItemClick(sensorItem) // Anropa klicklyssnaren med aktuellt sensorItem
        }
    }

    override fun getItemCount(): Int = sensorList.size

    class SensorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sensorTitle: TextView = view.findViewById(R.id.sensorTitle)
        val sensorDescription: TextView = view.findViewById(R.id.sensorDescription)
        val sensorIcon: ImageView = view.findViewById(R.id.sensorIcon)
    }
}
