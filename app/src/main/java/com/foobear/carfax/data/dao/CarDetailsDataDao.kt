package com.foobear.carfax.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.foobear.carfax.data.models.CarDetailsData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface CarDetailsDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCarDetails(carDetailsData: CarDetailsData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCarDetailsList(carDetailsDataList: List<CarDetailsData>)

    @Query("SELECT * FROM car_details")
    fun readAllCarDetails(): Flowable<List<CarDetailsData>>

    @Query("SELECT * FROM car_details WHERE vin = :vin")
    fun readSingleCarDetails(vin: String): Single<CarDetailsData>
}