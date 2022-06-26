package com.dombikpanda.doktarasor.repository

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.io.Questionio
import com.dombikpanda.doktarasor.model.Answer
import com.dombikpanda.doktarasor.model.Doctor
import com.dombikpanda.doktarasor.showShortToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.HashMap


class CrudRepository() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()

    fun retrieveUser(
        profileName: TextView,
        profilePhone: TextView,
        aboutDescription: TextView,
        globalprofileImage: CircleImageView,
        context: Context,
        collectionPath: String
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            val source = Source.CACHE
            val collection = Firebase.firestore.collection(collectionPath)
            collection.whereEqualTo("email", firebaseAuth.currentUser?.email.toString())
                .get(source)
                .addOnSuccessListener { result ->
                    for (document in result) {
                        profileName.text = document.get("fullname").toString()
                        profilePhone.text = document.get("phone").toString()
                        aboutDescription.text = document.get("about").toString()

                        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                            "users/ ${document.id}/profile.jpg"
                        )
                        sRef.downloadUrl.addOnSuccessListener {
                            Picasso.get().load(it).into(globalprofileImage)
                        }
                    }

                }
                .addOnFailureListener {
                    context.showShortToast(context.getString(R.string.error_getting))
                }
        }

    fun realtimeList(
        collectionPath: String,
        context: Context,
        cevapField: String,
        cevapdurum: Boolean,
        field: String,
        equel: String,
        query: Query.Direction
    ): LiveData<MutableList<Any>> {
        val mutableData = MutableLiveData<MutableList<Any>>()
        val docRef = Firebase.firestore.collection(collectionPath)
        docRef
            .whereEqualTo(field, equel)
            .whereEqualTo(cevapField, cevapdurum)
            .orderBy("date", query)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    context.showShortToast(it.message.toString())
                    return@addSnapshotListener
                }
                val listData: MutableList<Any> = mutableListOf()
                querySnapshot?.let {
                    for (document in it) {
                        /*val data = document.toObject<Questionio>()*/
                        val polyclinic = document.getString("policinic")
                        val title = document.getString("title")
                        val description = document.getString("descripiton")
                        val date: Long = document.get("date") as Long
                        val userid = document.getString("userid")
                        val quetinid = document.id
                        val cevapDurum = document.get("cevapdurum")
                        val questionio =
                            Questionio(
                                polyclinic!!,
                                title!!,
                                description!!,
                                date,
                                userid!!,
                                quetinid,
                                cevapDurum as Boolean
                            )
                        listData.add(questionio)
                    }
                    mutableData.value = listData
                }
            }
        return mutableData
    }

    fun realtimeDoctorList(context: Context): LiveData<MutableList<Doctor>> {
        val mutableData = MutableLiveData<MutableList<Doctor>>()
        val docRef = Firebase.firestore.collection("users")
        docRef
            .whereEqualTo("kullaniciseviyesi", "1")
            .whereEqualTo("kullaniciDurum", "2")
            .addSnapshotListener { querySnapshot, error ->
                error?.let {
                    context.showShortToast(it.message.toString())
                }
                val listData: MutableList<Doctor> = mutableListOf()
                querySnapshot?.let {
                    for (document in it) {
                        val fullName = document.getString("fullname")
                        val policlinic = document.getString("policlinic")
                        val phone = document.getString("phone")
                        val date: Long = document.get("date") as Long
                        val doctor = Doctor(
                            fullname = fullName!!,
                            policlinic = policlinic!!,
                            email = document.id,
                            phone = phone!!,
                            date = date
                        )
                        listData.add(doctor)
                    }
                    mutableData.value = listData
                }
            }
        return mutableData
    }


    //İstediğin tabloda istediğin elamanı güncelleyebilirsin.
    fun allUpdate(
        userMap: HashMap<String, Any>, collectionPath: String, context: Context, documentId: String
    ) {
        val collection = Firebase.firestore.collection(collectionPath)
        collection.document(documentId)
            .update(userMap)
            .addOnSuccessListener {
                context.showShortToast(context.getString(R.string.update_succes))
            }
            .addOnFailureListener {
                context.showShortToast(context.getString(R.string.sory))
            }
    }

    //istediğim tabloya istediğim modeli uygulayabiliyorum kısaca tüm verileri yazabilirim
    fun allCreate(model: Any, collectionPath: String, context: Context) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val collection = Firebase.firestore.collection(collectionPath)
                collection.document().set(model).await()
                withContext(Dispatchers.Main) {
                    context.showShortToast(context.getString(R.string.succesful_save))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    context.showShortToast(e.message.toString())
                }
            }
        }

    @OptIn(DelicateCoroutinesApi::class)
    fun readAllData(
        collectionPath: String,
        documentId: String,
        model: Any
    ): MutableLiveData<MutableList<Any>> {
        val mutableData = MutableLiveData<MutableList<Any>>()
        GlobalScope.launch(Dispatchers.IO) {
            val listData: MutableList<Any> = mutableListOf()
            val collection =
                Firebase.firestore.collection(collectionPath).document(documentId).get().await()
            val value = collection.toObject(model as Class<Any>)
            withContext(Dispatchers.Main) {
                listData.add(value!!)
                mutableData.value = listData
            }
        }
        return mutableData
    }

    fun readAnswer(documentId: String): MutableLiveData<MutableList<Any>> {
        val mutableData = MutableLiveData<MutableList<Any>>()
        val collection = Firebase.firestore.collection("answer")
        collection.whereEqualTo("questionId", documentId)
            .addSnapshotListener { querySnapShout, error ->
                error.let {
                }
                val listData: MutableList<Any> = mutableListOf()
                querySnapShout?.let { result ->
                    for (document in result) {
                        val data = document.toObject<Answer>()
                        listData.add(data)
                    }
                    mutableData.value = listData
                }
            }
        return mutableData
    }

    lateinit var model: Any
    private var sdataList = mutableListOf<Any>()
    fun setListData(data: MutableList<Any>): Any {
        sdataList = data
        model = sdataList[0]
        return model
    }

    init {
        if (firebaseAuth.currentUser != null) {
            this._userLiveData.postValue(firebaseAuth.currentUser)
        }
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return this._userLiveData
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return firebaseAuth
    }
}