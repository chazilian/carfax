package com.foobear.carfax.ui.cardetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.data.repo.CarDetailsRepository

class CarDetailsViewModel(private val carDetailsRepository: CarDetailsRepository): ViewModel() {



    suspend fun getCarList(): LiveData<List<CarDetailsData>>? {
        return carDetailsRepository.getCarDetails()
    }

    suspend fun getSingleCarDetail(vin: String): LiveData<CarDetailsData>? {
        return carDetailsRepository.getSingleCarDetails(vin)
    }

}