package com.foobear.carfax.ui.carlist

import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foobear.carfax.R
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.databinding.CarListDetailBinding


class CarListRecyclerViewAdapter(
    private val clickListener: (CarDetailsData) -> Unit,
    private val dealerListener: (CarDetailsData) -> Unit
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
        return CarListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarListViewHolder, position: Int) {
        holder.bind(values[position], clickListener, dealerListener)
    }

    override fun getItemCount(): Int = values.size

    fun setList(list: List<CarDetailsData>){
        values.clear()
        values.addAll(list)
    }
}

class CarListViewHolder(private val binding: CarListDetailBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        carDetailsData: CarDetailsData,
        clickListener: (CarDetailsData) -> Unit,
        dealerListener: (CarDetailsData) -> Unit
    ){
        Glide.with(binding.root)
                .load(carDetailsData.firstPhoto)
                .placeholder(R.drawable.no_image_found)
                .into(binding.ivCarPhoto)
        binding.tvYear.text = carDetailsData.year.toString()
        binding.tvMake.text = carDetailsData.make
        binding.tvModel.text = carDetailsData.model
        binding.tvTrim.text = carDetailsData.trim
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("USD")

        binding.tvPrice.text = "$" + format.format(carDetailsData.currentPrice).toString()
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