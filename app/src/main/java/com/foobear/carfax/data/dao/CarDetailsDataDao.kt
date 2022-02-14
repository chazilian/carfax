package com.foobear.carfax.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.foobear.carfax.data.models.CarDetailsData

@Dao
interface CarDetailsDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCarDetails(carDetailsData: CarDetailsData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCarDetailsList(carDetailsDataList: List<CarDetailsData>)

    @Query("SELECT * FROM car_details")
    fun readAllCarDetails(): LiveData<List<CarDetailsData>>

    @Query("SELECT * FROM car_details WHERE vin = :vin")
    fun readSingleCarDetails(vin: String): LiveData<CarDetailsData>
}