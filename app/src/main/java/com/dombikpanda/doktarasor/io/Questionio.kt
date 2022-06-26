package com.dombikpanda.doktarasor.io

data class Questionio(
    val policinic: String = "DEFAULT_POL",
    val title: String = "DEFAULT_TİTLE",
    val descripiton: String = "DEFAULT_DESC",
    val date: Long = -1,
    val userid: String = "DEFAULT_USER_ID",
    val questionid: String = "DEFAULT_QUESTION_ID",
    val cevapdurum: Boolean = false
)
