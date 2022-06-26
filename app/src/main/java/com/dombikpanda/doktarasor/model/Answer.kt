package com.dombikpanda.doktarasor.model

data class Answer(
    var userId: String = "",
    var questionId: String = "",
    var cevap: String = "",
    var doctorPhone: String = "",
    var date: Long = -1,
    var doctorName: String = ""
)
