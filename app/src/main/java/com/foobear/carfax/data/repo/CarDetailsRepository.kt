package com.foobear.carfax.data.repo

import androidx.lifecycle.LiveData
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.data.models.CarDetailsListingsRequest
import com.foobear.carfax.util.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface CarDetailsRepository {

    suspend fun getCarListDetails(): Resource<CarDetailsListingsRequest?>

    fun getSingleCarDetails(vin: String): LiveData<CarDetailsData>

    fun getCarListDetailsLocal(): LiveData<List<CarDetailsData>>

}