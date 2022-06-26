package com.dombikpanda.doktarasor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dombikpanda.doktarasor.model.Answer
import com.dombikpanda.doktarasor.repository.CrudRepository
import com.google.firebase.auth.FirebaseAuth

class AnswerViewModel:ViewModel() {
    private val crudRepository = CrudRepository()
    val firebaseAuth: FirebaseAuth

    fun readAnswer(documentId: String): LiveData<MutableList<Any>> {
        val mutableData = MutableLiveData<MutableList<Any>>()
        crudRepository.readAnswer(documentId).observeForever{userlist->
            mutableData.value=userlist
        }
        return mutableData
    }

    fun setAllListData(data: MutableList<Any>): Answer {
        return crudRepository.setListData(data) as Answer
    }

    init {
        firebaseAuth = crudRepository.getFirebaseAuth()
    }
}