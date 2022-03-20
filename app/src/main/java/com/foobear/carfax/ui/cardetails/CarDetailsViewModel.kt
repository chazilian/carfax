package com.foobear.carfax.ui.cardetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foobear.carfax.data.models.CarDetailsData
import com.foobear.carfax.data.repo.CarDetailsRepository
import com.foobear.carfax.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarDetailsViewModel(private val carDetailsRepository: CarDetailsRepository): ViewModel() {


    private val _isRefreshingData = MutableLiveData<Boolean>()
    val isRefreshingData: LiveData<Boolean>
            get() = _isRefreshingData

    private val _isErrorData = MutableLiveData<Boolean>()
    val isErrorData: LiveData<Boolean>
        get() = _isErrorData

    init {
        getCarList()
    }


    fun getCarList() {
        _isRefreshingData.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = carDetailsRepository.getCarListDetails()
            viewModelScope.launch(Dispatchers.Main) {
                if(result.status == Status.SUCCESS){
                    _isErrorData.postValue(false)
                } else {
                    _isErrorData.postValue(true)
                }
                _isRefreshingData.postValue(false)
            }
        }
    }

    fun getCarListLocal(): LiveData<List<CarDetailsData>> {
        return carDetailsRepository.getCarListDetailsLocal()
    }

    fun getSingleCarDetail(vin: String): LiveData<CarDetailsData> {
        return carDetailsRepository.getSingleCarDetails(vin)
    }

}