package com.foobear.carfax.ui.cardetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.foobear.carfax.data.repo.CarDetailsRepository

class CarDetailsViewModelFactory(private val carDetailRepo: CarDetailsRepository):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun<T : ViewModel?> create(modelclass: Class<T>): T{
        return CarDetailsViewModel(carDetailRepo) as T
    }
}