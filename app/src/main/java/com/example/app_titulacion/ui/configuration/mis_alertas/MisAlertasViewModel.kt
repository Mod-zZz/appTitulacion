package com.example.app_titulacion.ui.configuration.mis_alertas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_titulacion.data.http.response.ListaNotificacionResponse

import com.example.app_titulacion.domain.repository.AppDataRepository
import com.example.app_titulacion.utils.ResourceV2
import com.example.app_titulacion.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MisAlertasViewModel @Inject constructor(private val appDataRepository: AppDataRepository) :
    ViewModel() {

    private val _getListNotification = MutableLiveData<ResourceV2<ListaNotificacionResponse>>()
    val getListaNotificaciones: LiveData<ResourceV2<ListaNotificacionResponse>> get() = _getListNotification

    fun doGetListNotification(email: String) {
        viewModelScope.launch {
            _getListNotification.postValue(ResourceV2.loading())
            try {
                val res = appDataRepository.getListaNotificaciones(email)
                if (res.status == Status.SUCCESS) {
                    _getListNotification.postValue(res)
                } else if (res.status == Status.ERROR) {
                    _getListNotification.postValue(ResourceV2.error(res.message!!))
                }
            } catch (e: Exception) {
                _getListNotification.postValue(ResourceV2.error(e.message!!))
            }
        }
    }
}