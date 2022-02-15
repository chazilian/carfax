package com.foobear.carfax.ui.cardetails

import androidx.lifecycle.ViewModel
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.data.repo.CarDetailsRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class CarDetailsViewModel(private val carDetailsRepository: CarDetailsRepository): ViewModel() {



    fun getCarList(): Flowable<List<CarDetailsData>> {
        return carDetailsRepository.getCarDetails()
    }

    fun getSingleCarDetail(vin: String): Single<CarDetailsData> {
        return carDetailsRepository.getSingleCarDetails(vin)
    }

}