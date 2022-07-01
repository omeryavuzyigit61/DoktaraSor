package com.dombikpanda.doktarasor.ui.viewmodel

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dombikpanda.doktarasor.data.repository.CrudRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ProfileViewModel :
    ViewModel() {
    private val crudRepository = CrudRepository()
    val firebaseAuth: FirebaseAuth
    private val _userLiveData: MutableLiveData<FirebaseUser>
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData

    fun retrieveUser(
        profileName: TextView,
        profilePhone: TextView,
        aboutDescription: TextView,
        globalprofileImage: CircleImageView,
        context: Context,
        collectionPath: String
    ) {
        crudRepository.retrieveUser(
            profileName,
            profilePhone,
            aboutDescription,
            globalprofileImage,
            context,
            collectionPath
        )
    }

    fun updateUser(userMap: HashMap<String, Any>, activity: Context,documentId:String) {
        crudRepository.allUpdate(userMap, "users", activity, documentId)
    }

    init {
        // crudRepository = CrudRepository(application)
        _userLiveData = crudRepository.getUserLiveData()
        firebaseAuth = crudRepository.getFirebaseAuth()
    }
}