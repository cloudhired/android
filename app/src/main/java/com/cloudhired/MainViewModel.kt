package com.cloudhired

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudhired.api.CloudhiredApi
import com.cloudhired.api.ProfessionalProfile
import com.cloudhired.api.Repository
import com.cloudhired.api.ProfessionalSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {
    private val cloudhiredApi = CloudhiredApi.create()
    private val cloudhiredRepository = Repository(cloudhiredApi)
    private val proSums = MutableLiveData<List<ProfessionalSummary>>()
    private val proProfile = MutableLiveData<ProfessionalProfile>()

    fun netRefresh() = viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO)
    {
        proSums.postValue(cloudhiredRepository.fetchProSum())
    }

    fun netProfile(username: String) = viewModelScope.launch(
        context = viewModelScope.coroutineContext + Dispatchers.IO)
    {
        proProfile.postValue(cloudhiredRepository.fetchProfile(username))
    }

    fun getProfile(): ProfessionalProfile? {
        return proProfile.value
    }

    fun observeProSums(): LiveData<List<ProfessionalSummary>> {
        return proSums
    }

    fun observeProfile(): LiveData<ProfessionalProfile> {
        return proProfile
    }
}
