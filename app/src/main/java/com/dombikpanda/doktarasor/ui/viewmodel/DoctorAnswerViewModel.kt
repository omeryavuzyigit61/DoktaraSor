package com.dombikpanda.doktarasor.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dombikpanda.doktarasor.data.model.Answer
import com.dombikpanda.doktarasor.data.model.Questions
import com.dombikpanda.doktarasor.data.model.User
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DoctorAnswerViewModel : ViewModel() {
    private val crudRepository = CrudRepository()
    val firebaseAuth: FirebaseAuth

    fun answerCreate(model: Answer, context: Context) = CoroutineScope(Dispatchers.Main).launch {
        crudRepository.allCreate(model, "answer", context)
    }

    fun answerUpdate(userMap: HashMap<String, Any>, context: Context, documentId: String) {
        crudRepository.allUpdate(userMap, "questions", context, documentId)
    }

    fun readUser(documentId: String): LiveData<MutableList<Any>> {
        val mutableData = MutableLiveData<MutableList<Any>>()
        crudRepository.readAllData("users", documentId, User::class.java)
            .observeForever { userlist ->
                mutableData.value = userlist
            }
        return mutableData
    }

    fun readQuestion(documentId: String): LiveData<MutableList<Any>> {
        val mutableData = MutableLiveData<MutableList<Any>>()
        crudRepository.readAllData("questions", documentId, Questions::class.java)
            .observeForever { userlist ->
                mutableData.value = userlist
            }
        return mutableData
    }

    fun setListData(data: MutableList<Any>): User {
        return crudRepository.setListData(data) as User
    }

    fun setQuestionListData(data: MutableList<Any>): Questions {
        return crudRepository.setListData(data) as Questions
    }

    init {
        firebaseAuth = crudRepository.getFirebaseAuth()
    }
}