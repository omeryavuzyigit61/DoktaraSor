package com.dombikpanda.doktarasor.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dombikpanda.doktarasor.data.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseUser


class LoginRegisterViewModel :
    ViewModel() {
    private val authAppRepository: AuthenticationRepository=AuthenticationRepository()
    private val _userLiveData: MutableLiveData<FirebaseUser>
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData

    fun login(email: String, password: String, activity: Context) {
        authAppRepository.login(email, password, activity)
    }

    fun register(
        email: String,
        password: String,
        model: Any,
        activity: Context
    ) {
        authAppRepository.register(email, password, model, activity)
    }

    fun updateUI(context: Context){
        authAppRepository.updateUI(context)
    }

    init {
        _userLiveData = authAppRepository.getUserLiveData()
    }
}