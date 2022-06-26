package com.dombikpanda.doktarasor.model

data class Doctor(
    val fullname: String = "",
    val email: String = "",
    val age: Int = -1,
    val password: String = "",
    val about: String = "",
    val phone: String = "",
    val policlinic: String = "",
    val kullaniciseviyesi: String = "1",
    val date: Long = -1,
    val kullaniciDurum: String = "2"
)
