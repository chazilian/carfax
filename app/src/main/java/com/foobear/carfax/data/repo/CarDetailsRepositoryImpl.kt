package com.foobear.carfax.data.repo

import androidx.lifecycle.LiveData
import com.foobear.carfax.data.dao.CarDetailsDataDao
import com.foobear.carfax.data.datasource.CarDetailsDataSource
import com.foobear.carfax.data.models.CarDetails
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.data.models.CarDetailsListingsRequest
import com.foobear.carfax.util.NoConnectivityException
import com.foobear.carfax.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CarDetailsRepositoryImpl(
    private val carDetailsDataSource: CarDetailsDataSource,
    private val carDetailsDataDao: CarDetailsDataDao
    ): CarDetailsRepository {

    init {
        carDetailsDataSource.apply {
            downloadedCarDetailsRequest.observeForever {
                addCarDetailsListings(it.listings)
            }
        }
    }

    private fun addCarDetailsListings(carList: ArrayList<CarDetails>){
        GlobalScope.launch(Dispatchers.IO) {
            val list = convertRequestToData(carList)
            carDetailsDataDao.addCarDetailsList(list.toList())
        }
    }

    private fun convertRequestToData(carList: ArrayList<CarDetails>): ArrayList<CarDetailsData>{
        val list = ArrayList<CarDetailsData>()
        for (car in carList){
            list.add(CarDetailsData(
                vin = car.vin,
                year = car.year,
                make = car.make,
                model = car.model,
                trim = car.trim,
                mileage = car.mileage,
                currentPrice = car.currentPrice,
                exteriorColor = car.exteriorColor,
                interiorColor = car.interiorColor,
                engine = car.engine,
                drivetype = car.drivetype,
                transmission = car.transmission,
                bodytype = car.bodytype,
                city = car.dealer.city.toLowerCase().capitalize(),
                state = car.dealer.state,
                phone = car.dealer.phone,
                firstPhoto = car.images.firstPhoto.large
            ))
        }
        return list
    }

    override suspend fun getCarListDetails(): Resource<CarDetailsListingsRequest?> {
        return try {
            return carDetailsDataSource.getAllCarDetails()
        } catch (e:NoConnectivityException){
            Resource.error("Error", null)
        }
    }

    override fun getSingleCarDetails(vin: String): LiveData<CarDetailsData> {
        return carDetailsDataDao.readSingleCarDetails(vin)
    }

    override fun getCarListDetailsLocal(): LiveData<List<CarDetailsData>> {
        return carDetailsDataDao.readAllCarDetails()
    }
}