package com.dombikpanda.doktarasor.charts.data

import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChartsData {
    fun size(myCallback: MyCallback) {
        val collection = Firebase.firestore.collection("questinCharts")
        collection
            .orderBy("date", Query.Direction.DESCENDING).limit(7)
            .get()
            .addOnSuccessListener {
                val answerQuestionData: ArrayList<Long> = ArrayList()
                val questionAskedData: ArrayList<Long> = ArrayList()
                for (document in it) {
                    val answerQuestion = document.get("answerQuestion")
                    answerQuestionData.add(answerQuestion as Long)
                    val questionAsked = document.get("questionAsked")
                    questionAskedData.add(questionAsked as Long)
                }
                myCallback.onCallback(answerQuestionData,questionAskedData)
            }
    }

    interface MyCallback {
        fun onCallback(value: ArrayList<Long>,value2: ArrayList<Long>)
    }
    interface MyCallbackEntry {
        fun onCallback(value: ArrayList<Entry>,value2: ArrayList<Entry>)
    }


    fun dataValue(myCallback: MyCallbackEntry) {
        val lineEntry = arrayListOf<Entry>()
        val lineEntry2 = arrayListOf<Entry>()
        size(object : MyCallback {
            override fun onCallback(value: ArrayList<Long>,value2:ArrayList<Long>) {
                lineEntry.add(Entry(0F, value.get(0).toFloat()))
                lineEntry.add(Entry(1F, value.get(1).toFloat()))
                lineEntry.add(Entry(2F, value.get(2).toFloat()))
                lineEntry.add(Entry(3F, value.get(3).toFloat()))
                lineEntry.add(Entry(4F, value.get(4).toFloat()))
                lineEntry.add(Entry(5F, value.get(5).toFloat()))
                lineEntry.add(Entry(6F, value.get(6).toFloat()))

                lineEntry2.add(Entry(0F, value2.get(0).toFloat()))
                lineEntry2.add(Entry(1F, value2.get(1).toFloat()))
                lineEntry2.add(Entry(2F, value2.get(2).toFloat()))
                lineEntry2.add(Entry(3F, value2.get(3).toFloat()))
                lineEntry2.add(Entry(4F, value2.get(4).toFloat()))
                lineEntry2.add(Entry(5F, value2.get(5).toFloat()))
                lineEntry2.add(Entry(6F, value2.get(6).toFloat()))
                myCallback.onCallback(lineEntry,lineEntry2)
            }
        })
    }

}