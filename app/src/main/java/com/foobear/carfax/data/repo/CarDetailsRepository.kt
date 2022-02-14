package com.foobear.carfax.data.repo

import androidx.lifecycle.LiveData
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.data.models.CarDetailsListingsRequest

interface CarDetailsRepository {

    suspend fun getCarDetails(): LiveData<List<CarDetailsData>>?

    suspend fun getSingleCarDetails(vin: String): LiveData<CarDetailsData>

}