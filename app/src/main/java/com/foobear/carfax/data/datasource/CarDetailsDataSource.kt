package com.foobear.carfax.data.datasource

import androidx.lifecycle.LiveData
import com.foobear.carfax.data.models.CarDetailsListingsRequest

interface CarDetailsDataSource {
    val downloadedCarDetailsRequest: LiveData<CarDetailsListingsRequest>
    fun getAllCarDetails()
}