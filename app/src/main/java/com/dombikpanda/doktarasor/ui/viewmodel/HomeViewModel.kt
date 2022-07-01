package com.dombikpanda.doktarasor.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dombikpanda.doktarasor.data.model.Questions
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val crudRepository: CrudRepository
) : ViewModel() {
    val firebaeAuth: FirebaseAuth
    private val _userLiveData: MutableLiveData<FirebaseUser>
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData

    fun questionsCreate(model: Questions, collectionPath: String, context: Context) = CoroutineScope(Dispatchers.Main).launch {
        crudRepository.allCreate(model, collectionPath, context)
    }

    init {
        _userLiveData = crudRepository.getUserLiveData()
        firebaeAuth = crudRepository.getFirebaseAuth()
    }
}