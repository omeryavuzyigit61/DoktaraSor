package com.dombikpanda.doktarasor.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dombikpanda.doktarasor.model.Questions
import com.dombikpanda.doktarasor.repository.CrudRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeViewModel :
    ViewModel() {
    private val crudRepository = CrudRepository()
    val firebaeAuth: FirebaseAuth
    private val _userLiveData: MutableLiveData<FirebaseUser>
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData

    fun questionsCreate(model: Questions, collectionPath: String, context: Context) {
        crudRepository.allCreate(model, collectionPath, context)
    }

    init {
        // crudRepository = CrudRepository(application)
        _userLiveData = crudRepository.getUserLiveData()
        firebaeAuth = crudRepository.getFirebaseAuth()
    }
}