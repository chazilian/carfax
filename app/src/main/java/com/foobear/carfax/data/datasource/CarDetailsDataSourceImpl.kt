package com.foobear.carfax.data.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.foobear.carfax.data.CarFaxApi
import com.foobear.carfax.data.models.CarDetailsListingsRequest
import com.foobear.carfax.util.NoConnectivityException
import com.foobear.carfax.util.Resource
import java.lang.Exception


class CarDetailsDataSourceImpl(private val carFaxApi: CarFaxApi): CarDetailsDataSource {

    private val _downloadedCarDetails = MediatorLiveData<CarDetailsListingsRequest>()
    override  val downloadedCarDetailsRequest: LiveData<CarDetailsListingsRequest>
        get() = _downloadedCarDetails

    override suspend fun getAllCarDetails(): Resource<CarDetailsListingsRequest?> {
        return try {
            val source = carFaxApi.getCarDetailsListings()
            if(source.isSuccessful){
                _downloadedCarDetails.postValue(source.body())
                Resource.success(source.body())
            } else {
                Resource.error("Error Occurred", null)
            }
        } catch (e: NoConnectivityException){
            Resource.error(e.localizedMessage.toString(), null)
        } catch (e: Exception){
            Log.e("ERROR", e.localizedMessage.toString())
            Resource.error(e.localizedMessage.toString(), null)
        }
    }

}