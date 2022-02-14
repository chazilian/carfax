package com.foobear.carfax

import android.app.Application
import com.foobear.carfax.data.CarFaxApi
import com.foobear.carfax.data.CarFaxDatabase
import com.foobear.carfax.data.datasource.CarDetailsDataSource
import com.foobear.carfax.data.datasource.CarDetailsDataSourceImpl
import com.foobear.carfax.data.repo.CarDetailsRepository
import com.foobear.carfax.data.repo.CarDetailsRepositoryImpl
import com.foobear.carfax.ui.cardetails.CarDetailsViewModel
import com.foobear.carfax.ui.cardetails.CarDetailsViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CarFaxApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@CarFaxApplication))

        bind() from singleton { CarFaxApi() }
        bind() from singleton { CarFaxDatabase(instance()) }
        bind() from singleton { instance<CarFaxDatabase>().carDetailsDataDao() }

        bind() from provider { CarDetailsViewModelFactory(instance()) }
        bind() from singleton { CarDetailsViewModel(instance()) }

        bind<CarDetailsDataSource>() with singleton {
            CarDetailsDataSourceImpl(instance())
        }

        bind<CarDetailsRepository>() with singleton {
            CarDetailsRepositoryImpl(instance(), instance())
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}