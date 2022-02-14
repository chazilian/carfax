package com.foobear.carfax.data.datasource

import androidx.lifecycle.LiveData
import com.foobear.carfax.data.models.CarDetailsListingsRequest
import com.foobear.carfax.util.Resource

interface CarDetailsDataSource {
    val downloadedCarDetailsRequest: LiveData<CarDetailsListingsRequest>
    suspend fun getAllCarDetails(): Resource<CarDetailsListingsRequest>
}