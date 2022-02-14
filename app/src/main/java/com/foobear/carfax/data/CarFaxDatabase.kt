package com.foobear.carfax.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.foobear.carfax.data.dao.CarDetailsDataDao
import com.foobear.carfax.data.models.CarDetailsData

@Database(entities = [CarDetailsData::class], version = 1, exportSchema = false)
abstract class CarFaxDatabase: RoomDatabase() {

    abstract fun carDetailsDataDao(): CarDetailsDataDao

    companion object{
        @Volatile
        private var INSTANCE: CarFaxDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CarFaxDatabase::class.java,
                "carfax_database.db")
                .build()
    }
}