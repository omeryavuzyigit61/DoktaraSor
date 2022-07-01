package com.dombikpanda.doktarasor.ui.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dombikpanda.doktarasor.databinding.ActivityDoctorAnswerBinding
import com.dombikpanda.doktarasor.data.model.Answer
import com.dombikpanda.doktarasor.textValue
import com.dombikpanda.doktarasor.ui.viewmodel.DoctorAnswerViewModel

class DoctorAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorAnswerBinding
    private lateinit var doctorAnswerViewModel: DoctorAnswerViewModel
    private lateinit var cevap: String
    private lateinit var questionId: String
    private lateinit var userId: String
    private lateinit var doctorUid: String
    private lateinit var fullNameStr: String
    private lateinit var phoneStr: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        doctorAnswerViewModel = ViewModelProvider(this).get(DoctorAnswerViewModel::class.java)
        questionId = intent.getStringExtra("questionId").toString()
        userId = intent.getStringExtra("userId").toString()
        doctorUid = doctorAnswerViewModel.firebaseAuth.uid!!
        observeData()
        binding.apply {
            doctorAnswerSubmitBtn.setOnClickListener {
                val answer = getAnswer()
                doctorAnswerViewModel.answerCreate(answer, this@DoctorAnswerActivity)
                updateQuestionCevapDurum("cevapdurum", true)
            }
        }

    }

    private fun observeData() {
        doctorAnswerViewModel.apply {
            readQuestion(questionId).observe(this@DoctorAnswerActivity, {
                setQuestionListData(it).apply {
                    binding.apply {
                        readUserFun(userId)
                        doctorAnswerTitleTxt.text = title
                        doctorAnswerDescriptionTxt.text = descripiton
                    }
                }
            })
            readUserFun(doctorUid)
        }
    }

    fun readUserFun(rID: String) {
        doctorAnswerViewModel.apply {
            readUser(rID).observe(this@DoctorAnswerActivity, {
                setListData(it).apply {
                    if (kullaniciseviyesi == "0") {
                        binding.doctorAnswerNameTxt.text = fullname
                    } else if (kullaniciseviyesi == "1") {
                        fullNameStr = fullname
                        phoneStr = phone
                    }
                }
            })
        }
    }

    private var userMap: HashMap<String, Any> = HashMap()
    fun updateQuestionCevapDurum(updateField: String, boolean: Boolean) {
        userMap[updateField] = boolean
        doctorAnswerViewModel.answerUpdate(userMap, this, questionId)
    }

    private fun getAnswer(): Answer {
        readUserFun(doctorUid)
        binding.apply {
            cevap = doctorAnswerEt.textValue.trim()
        }
        val date = System.currentTimeMillis()
        return Answer(userId, questionId, cevap, phoneStr, date, doctorName = fullNameStr)
    }
}