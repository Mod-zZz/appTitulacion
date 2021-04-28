package com.walksafe.app_titulacion.ui.configuration.contactos_de_confianza

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walksafe.app_titulacion.data.http.response.ContactUpdateTokenResponse
import com.walksafe.app_titulacion.domain.repository.AppDataRepository
import com.walksafe.app_titulacion.utils.ResourceV2
import com.walksafe.app_titulacion.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactosViewModel @Inject constructor(private val appDataRepository: AppDataRepository) :
    ViewModel() {

    private val _contactoUpdateToken = MutableLiveData<ResourceV2<ContactUpdateTokenResponse>>()
    val contactoUpdateToken: LiveData<ResourceV2<ContactUpdateTokenResponse>> get() = _contactoUpdateToken

    fun doContactUpdateToken(email: String) {
        viewModelScope.launch {
            _contactoUpdateToken.postValue(ResourceV2.loading())
            try {
                val res = appDataRepository.contactUpdateToken(email)
                if (res.status == Status.SUCCESS) {
                    _contactoUpdateToken.postValue(res)
                } else if (res.status == Status.ERROR) {
                    _contactoUpdateToken.postValue(ResourceV2.error(res.message!!))
                }
            } catch (e: Exception) {
                _contactoUpdateToken.postValue(ResourceV2.error(e.message!!))
            }
        }
    }
}