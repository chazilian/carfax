package com.foobear.carfax.ui.carlist

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foobear.carfax.R
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.databinding.CarListDetailBinding
import com.foobear.carfax.ui.OnCarClickListener


class CarListRecyclerViewAdapter(
    private val onCarClickListener: OnCarClickListener
) : RecyclerView.Adapter<CarListViewHolder>() {

    private val values = ArrayList<CarDetailsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CarListDetailBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.car_list_detail,
            parent,
            false
        )
        return CarListViewHolder(binding, onCarClickListener)
    }

    override fun onBindViewHolder(holder: CarListViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int = values.size

    fun setList(list: List<CarDetailsData>){
        values.clear()
        values.addAll(list)
    }

    fun getItem(position: Int): CarDetailsData {
        return values[position]
    }
}

class CarListViewHolder(
    private val binding: CarListDetailBinding,
    private val onCarClickListener: OnCarClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.tvCallDealer.setOnClickListener {
            onCarClickListener.onDealerClick(absoluteAdapterPosition)
        }

        binding.llSelectable.setOnClickListener {
            onCarClickListener.onCarClick(absoluteAdapterPosition)
        }
    }

    fun bind(carDetailsData: CarDetailsData){
        Glide.with(binding.root)
                .load(carDetailsData.firstPhoto)
                .placeholder(R.drawable.no_image_found)
                .into(binding.ivCarPhoto)
        binding.tvYear.text = carDetailsData.year.toString()
        binding.tvMake.text = carDetailsData.make
        binding.tvModel.text = carDetailsData.model
        binding.tvTrim.text = carDetailsData.trim

        val formatter: NumberFormat = NumberFormat.getCurrencyInstance()
        formatter.maximumFractionDigits = 0
        binding.tvPrice.text = formatter.format(carDetailsData.currentPrice).toString()

        binding.tvMileage.text = carDetailsData.mileage.toString() + " mi"
        binding.tvCity.text = carDetailsData.city
        binding.tvState.text = carDetailsData.state
    }

}