package com.example.airpolution.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.airpolution.databinding.ArimeasurementitemBinding
import com.example.airpolution.domain.CardAirMeasurementDisplay

class AirMeasurementsAdapter() :
    RecyclerView.Adapter<AirMeasurementsAdapter.AirMeasurementsViewHolder>() {

//    private val onValueClickListener: ((position: Int) -> Unit)? = null
    private val data = mutableListOf<CardAirMeasurementDisplay>()

    class AirMeasurementsViewHolder(private val binding: ArimeasurementitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card: CardAirMeasurementDisplay) {
            binding.txtAirMeasurementsRC.text = card.displayText
            binding.cardViewItem.setCardBackgroundColor(card.color)
            binding.iconView.setImageResource(card.imagePath)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirMeasurementsViewHolder {
        val binding = ArimeasurementitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val airMeasurementsViewHolder = AirMeasurementsViewHolder(binding)
        return airMeasurementsViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AirMeasurementsViewHolder, position: Int) {
//        val currentItem = data[holder.bindingAdapterPosition]
        holder.bind(data.get(position))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<CardAirMeasurementDisplay>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}