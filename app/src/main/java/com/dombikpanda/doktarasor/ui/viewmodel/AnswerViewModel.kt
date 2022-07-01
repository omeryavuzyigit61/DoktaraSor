package com.dombikpanda.doktarasor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dombikpanda.doktarasor.data.model.Answer
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import com.google.firebase.auth.FirebaseAuth

class AnswerViewModel(
    private val crudRepository: CrudRepository
):ViewModel() {
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