package com.foobear.carfax.ui.carlist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.foobear.carfax.R
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.databinding.CarListDetailBinding


class CarListDetailsRecyclerViewAdapter(
    private val values: List<CarDetailsData>,
    private val clickListener:(CarDetailsData)->Unit,
    private val dealerListener:(CarDetailsData)->Unit
) : RecyclerView.Adapter<CarListDetailsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:  CarListDetailBinding = DataBindingUtil.inflate(layoutInflater, R.layout.car_list_detail, parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position], clickListener, dealerListener)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding: CarListDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(carDetailsData: CarDetailsData, clickListener: (CarDetailsData) -> Unit, dealerListener: (CarDetailsData) -> Unit){
            Glide.with(binding.root)
                    .load(carDetailsData.firstPhoto)
                    .into(binding.ivCarPhoto)
            binding.tvYear.text = carDetailsData.year.toString()
            binding.tvMake.text = carDetailsData.make
            binding.tvModel.text = carDetailsData.model
            binding.tvTrim.text = carDetailsData.trim
            binding.tvPrice.text = "$" + carDetailsData.currentPrice.toString()
            binding.tvMileage.text = carDetailsData.mileage.toString() + " mi"
            binding.tvCity.text = carDetailsData.city
            binding.tvState.text = carDetailsData.state
            binding.tvCallDealer.setOnClickListener {
                dealerListener(carDetailsData)
            }
            binding.llSelectable.setOnClickListener {
                clickListener(carDetailsData)
            }
        }

    }
}