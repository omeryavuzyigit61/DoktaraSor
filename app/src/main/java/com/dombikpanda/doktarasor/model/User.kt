package com.dombikpanda.doktarasor.model

data class User(
    var fullname: String = "",
    var email: String = "",
    var age: Int = -1,
    var password: String = "",
    var about: String = "",
    var phone: String = "",
    var kullaniciseviyesi: String = "0",
    var policlinic: String = ""
)

