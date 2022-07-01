package com.dombikpanda.doktarasor.ui.viewmodel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import androidx.lifecycle.ViewModel
import com.dombikpanda.doktarasor.data.repository.AuthenticationRepository


class LoggedInViewModel :
    ViewModel() {
    private val authAppRepository: AuthenticationRepository
    val userLiveData: MutableLiveData<FirebaseUser>
    val loggedOutLiveData: MutableLiveData<Boolean>

    fun logOut() {
        authAppRepository.logOut()
    }

    init {
        authAppRepository = AuthenticationRepository()
        userLiveData = authAppRepository.getUserLiveData()
        loggedOutLiveData = authAppRepository.getLoggedOutLiveData()
    }
}