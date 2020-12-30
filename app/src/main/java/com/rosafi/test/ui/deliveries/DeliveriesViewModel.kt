package com.rosafi.test.ui.deliveries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosafi.test.data.data_manager.Repository
import com.rosafi.test.data.model.Delivery
import com.rosafi.test.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class DeliveriesViewModel : ViewModel() {

    private val repository = Repository(RetrofitClient)
    private  val _deliveriesLiveData = MutableLiveData<ArrayList<Delivery>>()
    val deliveriesLiveData: LiveData<ArrayList<Delivery>> = _deliveriesLiveData

    fun getRemoteDeliveries(){

        viewModelScope.launch {
            repository.getDeliveries.flowOn(Dispatchers.IO).collect {

                _deliveriesLiveData.postValue(it)

            }
        }

    }


}