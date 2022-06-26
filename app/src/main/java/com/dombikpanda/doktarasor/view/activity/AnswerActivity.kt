package com.dombikpanda.doktarasor.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dombikpanda.doktarasor.SimpleDateFormat
import com.dombikpanda.doktarasor.databinding.ActivityAnswerBinding
import com.dombikpanda.doktarasor.showShortToast
import com.dombikpanda.doktarasor.viewmodel.AnswerViewModel

class AnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnswerBinding
    private lateinit var questionID: String
    private lateinit var answerViewModel: AnswerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        questionID = intent.getStringExtra("questionID").toString()
        answerViewModel = ViewModelProvider(this).get(AnswerViewModel::class.java)
        observeData()

        binding.apply {
            answerViewModel.apply {
                showShortToast(questionID)
                readAnswer(questionID).observe(this@AnswerActivity, {
                    setAllListData(it).apply {
                        answerNameTxt.text=doctorName
                        answerTitlePhone.text=doctorPhone
                        answerDescriptionTxt.text=cevap
                        answerTitleDate.text= SimpleDateFormat(date,"dd / MMMM / yyyy - HH:mm")
                    }
                })
            }
        }
    }

    private fun observeData() {

    }
}