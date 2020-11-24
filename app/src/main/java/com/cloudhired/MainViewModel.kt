package com.cloudhired

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudhired.api.CloudhiredApi
import com.cloudhired.api.Repository
import com.cloudhired.api.ProfessionalSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {
    private var difficulty = "badValue"
    // XXX You need some important member variables
    private val cloudhiredApi = CloudhiredApi.create()
    private val cloudhiredRepository = Repository(cloudhiredApi)
    private val proSums = MutableLiveData<List<ProfessionalSummary>>()

    fun netRefresh() = viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO)
    {
        // XXX Write me.  This is where the network request is initiated.
        proSums.postValue(cloudhiredRepository.fetchProSum())
    }
    // XXX Another function is necessary
    fun observeProSums(): LiveData<List<ProfessionalSummary>> {
        return proSums
    }
}
