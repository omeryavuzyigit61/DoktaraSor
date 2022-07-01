package com.dombikpanda.doktarasor.data.repository

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.goActivity
import com.dombikpanda.doktarasor.showShortToast
import com.dombikpanda.doktarasor.ui.view.activity.AdminActivity
import com.dombikpanda.doktarasor.ui.view.activity.DoctorHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.dombikpanda.doktarasor.ui.view.activity.HomeActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber


class AuthenticationRepository {
    private val firebaseAuth: FirebaseAuth
    private val _userLiveData: MutableLiveData<FirebaseUser>
    private var prefences: SharedPreferences? = null
    /* val userLiveData: LiveData<FirebaseUser>
         get() = _userLiveData*/

    private var loggedOutLiveData: MutableLiveData<Boolean>

    fun login(email: String, password: String, context: Context) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    this._userLiveData.postValue(firebaseAuth.currentUser)
                    context.showShortToast(context.getString(R.string.succesful_save))
                    userControl(context)
                } else {
                    context.showShortToast(task.exception!!.message.toString())
                }
            }
    }

    fun register(
        email: String,
        password: String,
        model: Any,
        context: Context
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    this._userLiveData.postValue(firebaseAuth.currentUser)
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    savePerson(model, firebaseUser.uid, context)
                    val homeIntent = Intent(context, HomeActivity::class.java)
                    goActivity(context, homeIntent)
                } else {
                    context.showShortToast(context.getString(R.string.registed_failed))
                }
            }
    }

    private fun savePerson(model: Any, uid: String, context: Context) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val collection = Firebase.firestore.collection("users")
                collection.document(uid).set(model).await()
                withContext(Dispatchers.Main) {
                    context.showShortToast(context.getString(R.string.succesful_save))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    context.showShortToast(e.message.toString())
                }
            }
        }

    private fun userControl(context: Context) {
        prefences = context.getSharedPreferences("kontrol", Context.MODE_PRIVATE)
        val editor = prefences?.edit()
        val collection = Firebase.firestore.collection("users")
        firebaseAuth.currentUser?.let {
            collection.document(it.uid).get().addOnSuccessListener { result ->
                if (result.get("kullaniciseviyesi") == "0") {
                    val homeIntent = Intent(context, HomeActivity::class.java)
                    goActivity(context, homeIntent)
                    editor?.putString("userControl", "0")
                    editor?.apply()
                } else if (result.get("kullaniciseviyesi") == "1") {
                    val doctorHomeActivity = Intent(context, DoctorHomeActivity::class.java)
                    goActivity(context, doctorHomeActivity)
                    editor?.putString("userControl", "1")
                    editor?.apply()
                } else if (result.get("kullaniciseviyesi") == "2") {
                    val adminActivity = Intent(context, AdminActivity::class.java)
                    goActivity(context, adminActivity)
                    editor?.putString("userControl", "2")
                    editor?.apply()
                }
            }.addOnFailureListener {
                context.showShortToast(context.getString(R.string.error_user_control))
            }
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    fun updateUI(context: Context) {
        prefences = context.getSharedPreferences("kontrol", Context.MODE_PRIVATE)
        val user = firebaseAuth.currentUser
        if (user != null) {
            if (prefences?.getString("userControl", null) == "0") {
                val homeIntent = Intent(context, HomeActivity::class.java)
                goActivity(context, homeIntent)
            } else if (prefences?.getString("userControl", null) == "1") {
                val doctorHomeActivity = Intent(context, DoctorHomeActivity::class.java)
                goActivity(context, doctorHomeActivity)
            } else {
                val adminActivity = Intent(context, AdminActivity::class.java)
                goActivity(context, adminActivity)
            }
        } else {
            Timber.i("Error")
        }
    }

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        this._userLiveData = MutableLiveData()
        loggedOutLiveData = MutableLiveData()
        if (firebaseAuth.currentUser != null) {
            this._userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return this._userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }
    // Toast fonksiyonu ve activity geçişlerinden 29 satır kod azaltma keyfsss
}