package com.dombikpanda.doktarasor.data.model

data class Questions(
    var userid: String = "",
    var policinic: String = "",
    var title: String = "",
    var descripiton: String = "",
    var date: Long = -1,
    var cevapdurum: Boolean = false,
    var messageDurum: Boolean = true
)