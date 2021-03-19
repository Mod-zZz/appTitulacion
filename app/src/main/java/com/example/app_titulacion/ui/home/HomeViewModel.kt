package com.example.app_titulacion.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_titulacion.data.http.response.NotificationSosResponse
import com.example.app_titulacion.domain.repository.AppDataRepository
import com.example.app_titulacion.utils.ResourceV2
import com.example.app_titulacion.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val appDataRepository: AppDataRepository) :
    ViewModel() {

    private val _sendNotification = MutableLiveData<ResourceV2<NotificationSosResponse>>()
    val sendNotification: LiveData<ResourceV2<NotificationSosResponse>> get() = _sendNotification

    fun doSendNotification(email: String) {
        viewModelScope.launch {
            _sendNotification.postValue(ResourceV2.loading())
            try {
                val res = appDataRepository.sendNotification(email)
                if (res.status == Status.SUCCESS) {
                    _sendNotification.postValue(res)
                } else if (res.status == Status.ERROR) {
                    _sendNotification.postValue(ResourceV2.error(res.message!!))
                }
            } catch (e: Exception) {
                _sendNotification.postValue(ResourceV2.error(e.message!!))
            }
        }
    }
}