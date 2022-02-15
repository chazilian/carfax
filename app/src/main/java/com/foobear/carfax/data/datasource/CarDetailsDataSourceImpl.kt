package com.foobear.carfax.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import com.foobear.carfax.data.CarFaxApi
import com.foobear.carfax.data.models.CarDetailsListingsRequest
import io.reactivex.rxjava3.schedulers.Schedulers


class CarDetailsDataSourceImpl(private val carFaxApi: CarFaxApi): CarDetailsDataSource {

    private val _downloadedCarDetails = MediatorLiveData<CarDetailsListingsRequest>()
    override  val downloadedCarDetailsRequest: LiveData<CarDetailsListingsRequest>
        get() = _downloadedCarDetails

    override fun getAllCarDetails() {

        val source = LiveDataReactiveStreams.fromPublisher(
            carFaxApi.getCarDetailsListings()
                .subscribeOn(Schedulers.io())
        )

        _downloadedCarDetails.addSource(source) {
            _downloadedCarDetails.postValue(it)
        }
    }

}