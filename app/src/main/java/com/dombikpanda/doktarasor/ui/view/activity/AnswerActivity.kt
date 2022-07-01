package com.dombikpanda.doktarasor.ui.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dombikpanda.doktarasor.SimpleDateFormat
import com.dombikpanda.doktarasor.databinding.ActivityAnswerBinding
import com.dombikpanda.doktarasor.showShortToast
import com.dombikpanda.doktarasor.ui.Factories.AnswerViewModelFactory
import com.dombikpanda.doktarasor.ui.viewmodel.AnswerViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AnswerActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: AnswerViewModelFactory by instance()

    private lateinit var binding: ActivityAnswerBinding
    private lateinit var questionID: String
    private lateinit var answerViewModel: AnswerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        questionID = intent.getStringExtra("questionID").toString()
        answerViewModel = ViewModelProvider(this, factory).get(AnswerViewModel::class.java)

        binding.apply {
            answerViewModel.apply {
                showShortToast(questionID)
                readAnswer(questionID).observe(this@AnswerActivity, {
                    setAllListData(it).apply {
                        answerNameTxt.text = doctorName
                        answerTitlePhone.text = doctorPhone
                        answerDescriptionTxt.text = cevap
                        answerTitleDate.text = SimpleDateFormat(date, "dd / MMMM / yyyy - HH:mm")
                    }
                })
            }
        }
    }
}