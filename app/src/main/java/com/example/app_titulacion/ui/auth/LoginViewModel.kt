package com.example.app_titulacion.ui.auth

import androidx.lifecycle.*
import com.example.app_titulacion.domain.repository.AuthDataRepository
import com.example.app_titulacion.utils.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authDataRepository: AuthDataRepository) :
    ViewModel() {

    private val _signIn = MutableLiveData<Resource<AuthResult>>()
    val signIn: LiveData<Resource<AuthResult>> get() = _signIn

    fun doSignIn(email: String, password: String) {
        _signIn.postValue(Resource.Loading())
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val res = authDataRepository.signIn(email, password)
                _signIn.postValue(res)
            }
        } catch (e: Exception) {
            _signIn.postValue(Resource.Failure(e))
        }
    }
}