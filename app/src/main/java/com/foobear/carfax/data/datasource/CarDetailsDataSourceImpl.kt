package com.foobear.carfax.data.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foobear.carfax.data.CarFaxApi
import com.foobear.carfax.data.models.CarDetailsListingsRequest
import com.foobear.carfax.util.Resource

class CarDetailsDataSourceImpl(private val carFaxApi: CarFaxApi): CarDetailsDataSource {

    private val _downloadedCarDetails = MutableLiveData<CarDetailsListingsRequest>()
    override  val downloadedCarDetailsRequest: LiveData<CarDetailsListingsRequest>
        get() = _downloadedCarDetails

    override suspend fun getAllCarDetails(): Resource<CarDetailsListingsRequest> {
        return try {
            val fetchedList = carFaxApi.getCarDetailsListings()
            val carListResult = fetchedList.body()
            if (fetchedList.isSuccessful && carListResult != null) {
                _downloadedCarDetails.postValue(carListResult)
                Resource.Success(carListResult)
            } else {
                Resource.Error(fetchedList.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

}