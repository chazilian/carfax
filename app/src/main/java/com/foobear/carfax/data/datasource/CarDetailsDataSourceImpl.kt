package com.foobear.carfax.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import com.foobear.carfax.data.CarFaxApi
import com.foobear.carfax.data.models.CarDetailsListingsRequest
import com.foobear.carfax.util.NoConnectivityException
import com.foobear.carfax.util.Resource
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception


class CarDetailsDataSourceImpl(private val carFaxApi: CarFaxApi): CarDetailsDataSource {

    private val _downloadedCarDetails = MediatorLiveData<CarDetailsListingsRequest>()
    override  val downloadedCarDetailsRequest: LiveData<CarDetailsListingsRequest>
        get() = _downloadedCarDetails

    override fun getAllCarDetails(): Resource<LiveData<CarDetailsListingsRequest>?> {
        try {
            val source = LiveDataReactiveStreams.fromPublisher(
                    carFaxApi.getCarDetailsListings()
                            .subscribeOn(Schedulers.io())
                            .onErrorReturn {
                                throw it
                            }
            )

            _downloadedCarDetails.addSource(source) {
                _downloadedCarDetails.postValue(it)
            }
            return Resource.success(source)

        } catch (e: NoConnectivityException){
           return  Resource.error(e.localizedMessage.toString(), null)
        } catch (e: Exception){
            return  Resource.error(e.localizedMessage.toString(), null)
        }
    }

}