package com.foobear.carfax.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_details")
data class CarDetailsData(
    @PrimaryKey val vin: String,
    val year: Short,
    val make: String,
    val model: String,
    val trim: String,
    val mileage: Int,
    val currentPrice: Float,
    val exteriorColor: String,
    val interiorColor: String,
    val engine: String,
    val drivetype: String,
    val transmission: String,
    val bodytype: String,
    //dealer
    val city: String,
    val state: String,
    val phone: String,
    //photo
    val firstPhoto: String
)