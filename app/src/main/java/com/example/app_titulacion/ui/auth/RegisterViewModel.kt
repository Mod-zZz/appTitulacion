package com.example.app_titulacion.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_titulacion.domain.repository.AuthDataRepository
import com.example.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authDataRepository: AuthDataRepository) :
    ViewModel() {

    private val _signUp = MutableLiveData<Resource<AuthResult>>()
    val signUp: LiveData<Resource<AuthResult>> get() = _signUp

    fun executeSignUp() {
        _signUp.postValue(Resource.Loading())
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val res = authDataRepository.signUp("", "")
                _signUp.postValue(res)
            }
        } catch (e: Exception) {
            _signUp.postValue(Resource.Failure(e))
        }

    }


}