package com.foobear.carfax.data.models

data class CarDetailsListingsRequest(
    val listings: ArrayList<CarDetails>
)

data class CarDetails(
    val year: Short,
    val make: String,
    val model: String,
    val trim: String,
    //details
    val dealer: Dealer,
    val vin: String,
    val mileage: Int,
    val currentPrice: Float,
    val exteriorColor: String,
    val interiorColor: String,
    val engine: String,
    val drivetype: String,
    val transmission: String,
    val bodytype: String,
    val images: Images
)

data class Dealer(
    val city: String,
    val state: String,
    //details
    val phone: String
)

data class Images(
        val firstPhoto: FirstPhoto
)

data class FirstPhoto(
    val large: String
)