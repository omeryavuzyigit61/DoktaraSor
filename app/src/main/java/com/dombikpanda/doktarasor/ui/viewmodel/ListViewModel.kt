package com.dombikpanda.doktarasor.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dombikpanda.doktarasor.data.model.Doctor
import com.dombikpanda.doktarasor.data.model.User
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

class ListViewModel : ViewModel() {
    private val crudRepository = CrudRepository()
    val firebaseAuth: FirebaseAuth

    fun fetchQuestinData(
        collectionPath: String,
        context: Context,
        cevapField: String,
        cevapDurum: Boolean,
        field: String,
        equel: String,
        query: Query.Direction
    ): LiveData<MutableList<Any>> {
        val mutableData = MutableLiveData<MutableList<Any>>()
        crudRepository.realtimeList(
            collectionPath,
            context,
            cevapField,
            cevapDurum,
            field,
            equel,
            query
        ).observeForever { questionList ->
            mutableData.value = questionList
        }
        return mutableData
    }



    fun realtimeDoctorList(context: Context): LiveData<MutableList<Doctor>> {
        val mutableData = MutableLiveData<MutableList<Doctor>>()
        crudRepository.realtimeDoctorList(context).observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun readUser(documentId: String): LiveData<MutableList<Any>> {
        val mutableData = MutableLiveData<MutableList<Any>>()
        crudRepository.readAllData("users", documentId, User::class.java)
            .observeForever { userlist ->
                mutableData.value = userlist
            }
        return mutableData
    }

    fun setListData(data: MutableList<Any>): User {
        return crudRepository.setListData(data) as User
    }

    init {
        firebaseAuth = crudRepository.getFirebaseAuth()
    }

}