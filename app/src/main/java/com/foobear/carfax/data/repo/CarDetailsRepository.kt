package com.foobear.carfax.data.repo

import com.foobear.carfax.data.models.CarDetailsData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface CarDetailsRepository {

    fun getCarDetails(): Flowable<List<CarDetailsData>>

    fun getSingleCarDetails(vin: String): Single<CarDetailsData>

}