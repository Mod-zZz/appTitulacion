package com.walksafe.app_titulacion.ui.configuration.alertar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walksafe.app_titulacion.data.http.response.NotificationAcosoSexualResponse
import com.walksafe.app_titulacion.data.http.response.NotificationAgresionFisicaResponse
import com.walksafe.app_titulacion.data.http.response.NotificationAgresionVerbalResponse
import com.walksafe.app_titulacion.domain.repository.AppDataRepository
import com.walksafe.app_titulacion.utils.ResourceV2
import com.walksafe.app_titulacion.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AlertarViewModel @Inject constructor(private val appDataRepository: AppDataRepository) :
    ViewModel() {

    private val _sendNotificationAcosoSexual = MutableLiveData<ResourceV2<NotificationAcosoSexualResponse>>()
    val sendNotificationAcosoSexual: LiveData<ResourceV2<NotificationAcosoSexualResponse>> get () = _sendNotificationAcosoSexual

    fun doSendNotificationAcosoSexual(email: String,latitud: String, longitud: String){
        viewModelScope.launch {
            _sendNotificationAcosoSexual.postValue(ResourceV2.loading())
            try {
                val res = appDataRepository.sendNotificationAcosoSexual(email,latitud,longitud)
                if (res.status == Status.SUCCESS) {
                    _sendNotificationAcosoSexual.postValue(res)
                } else if (res.status == Status.ERROR) {
                    _sendNotificationAcosoSexual.postValue(ResourceV2.error(res.message!!))
                }

            } catch (e: Exception) {
                _sendNotificationAcosoSexual.postValue(ResourceV2.error(e.message!!))
            }
        }
    }

    private val _sendNotificationAgresionFisica = MutableLiveData<ResourceV2<NotificationAgresionFisicaResponse>>()
    val sendNotificationAgresionFisica: LiveData<ResourceV2<NotificationAgresionFisicaResponse>> get () = _sendNotificationAgresionFisica

    fun doSendNotificationAgresionFisica(email: String,latitud: String, longitud: String){
        viewModelScope.launch {
            _sendNotificationAgresionFisica.postValue(ResourceV2.loading())
            try {
                val res = appDataRepository.sendNotificationAgresionFisica(email,latitud,longitud)
                if (res.status == Status.SUCCESS) {
                    _sendNotificationAgresionFisica.postValue(res)
                } else if (res.status == Status.ERROR) {
                    _sendNotificationAgresionFisica.postValue(ResourceV2.error(res.message!!))
                }

            } catch (e: Exception) {
                _sendNotificationAgresionFisica.postValue(ResourceV2.error(e.message!!))
            }
        }
    }

    private val _sendNotificationAgresionVerbal = MutableLiveData<ResourceV2<NotificationAgresionVerbalResponse>>()
    val sendNotificationAgresionVerbal: LiveData<ResourceV2<NotificationAgresionVerbalResponse>> get () = _sendNotificationAgresionVerbal

    fun doSendNotificationAgresionVerbal(email: String,latitud: String, longitud: String){
        viewModelScope.launch {
            _sendNotificationAgresionVerbal.postValue(ResourceV2.loading())
            try {
                val res = appDataRepository.sendNotificationAgresionVerbal(email,latitud,longitud)
                if (res.status == Status.SUCCESS) {
                    _sendNotificationAgresionVerbal.postValue(res)
                } else if (res.status == Status.ERROR) {
                    _sendNotificationAgresionVerbal.postValue(ResourceV2.error(res.message!!))
                }

            } catch (e: Exception) {
                _sendNotificationAgresionVerbal.postValue(ResourceV2.error(e.message!!))
            }
        }
    }

}