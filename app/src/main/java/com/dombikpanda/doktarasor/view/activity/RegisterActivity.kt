package com.dombikpanda.doktarasor.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import com.dombikpanda.doktarasor.databinding.ActivityRegisterBinding
import com.dombikpanda.doktarasor.model.User
import com.dombikpanda.doktarasor.viewmodel.LoginRegisterViewModel
import com.dombikpanda.doktarasor.*
import com.dombikpanda.doktarasor.model.Doctor


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var fullname: String
    private lateinit var email: String
    private var age: Int = 0
    private lateinit var password: String
    private lateinit var about: String
    private lateinit var phoneNumber: String
    private lateinit var policlinic: String
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel

    private val fromAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_right_anim)
    }
    private val toAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_right_anim)
    }

    override fun onResume() {
        super.onResume()
        val doctorlist = resources.getStringArray(R.array.doctorlist)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, doctorlist)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.setDropDownBackgroundResource(R.color.white)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginRegisterViewModel = ViewModelProvider(this).get(
            LoginRegisterViewModel::class.java
        )
        loginRegisterViewModel.userLiveData.observe(this,
            { firebaseUser ->
                if (firebaseUser != null) {
                    showShortToast(getString(R.string.success))
                }
            })

        binding.countryCodePicker.registerCarrierNumberEditText(binding.etRegistedPhone)

        binding.btnSignup.setOnClickListener {
            binding.apply {
                val editTextList = listOf(
                    etRegistedFullname,
                    etRegistedEmail,
                    etRegistedAge,
                    etRegistedPassword,
                    etRegistedConfirmpassword,
                    etRegistedPhone
                )
                //Bu for dögüsü sayaseinde 11 satır koddan kurtuldum
                for (item in editTextList) {
                    when {
                        TextUtils.isEmpty(
                            item.text.toString().trim { it <= ' ' }) -> {
                            item.error = getString(R.string.cannot_be_blank)
                            item.requestFocus()
                        }
                    }
                }
                if (isValidPassword(etRegistedPassword.textValue.trim())) {
                    if (etRegistedPassword.textValue
                        == (etRegistedConfirmpassword.textValue)
                    ) {
                        if (binding.userRb.isChecked) {
                            val user = getPerson()
                            authcreateUser(user)
                        } else {
                            val doctor = getDoctor()
                            authcreateUser(doctor)
                        }
                    } else {
                        showShortToast(getString(R.string.password_and_password_replay_mismatch))
                    }
                } else {
                    etRegistedPassword.error = getString(R.string.pattern_password_error)
                    etRegistedPassword.requestFocus()
                }
            }
        }

    }


    private fun getPerson(): User {

        fullname = binding.etRegistedFullname.textValue.trim { it <= ' ' }
        email = binding.etRegistedEmail.textValue.trim { it <= ' ' }
        age = binding.etRegistedAge.textValue.trim { it <= ' ' }.toInt()
        password = binding.etRegistedPassword.textValue.trim { it <= ' ' }
        phoneNumber = binding.countryCodePicker.formattedFullNumber
        about = ""

        return User(fullname, email, age, password, about, phoneNumber)
    }

    private fun getDoctor(): Doctor {

        fullname = binding.etRegistedFullname.textValue.trim { it <= ' ' }
        email = binding.etRegistedEmail.textValue.trim { it <= ' ' }
        age = binding.etRegistedAge.textValue.trim { it <= ' ' }.toInt()
        password = binding.etRegistedPassword.textValue.trim { it <= ' ' }
        policlinic = binding.autoCompleteTextView.textValue.trim { it <= ' ' }
        phoneNumber = binding.countryCodePicker.formattedFullNumber
        about = ""

        return Doctor(
            fullname,
            email,
            age,
            password,
            about,
            phoneNumber,
            policlinic,
            date = System.currentTimeMillis()
        )
    }

    private fun authcreateUser(model: Any) {
        loginRegisterViewModel.register(email, password, model, this)
    }

    private fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            when (view.getId()) {
                R.id.user_rb -> {
                    binding.tilRegisterPoliclinic.hide()
                    binding.tilRegisterPoliclinic.startAnimation(toAnimation)
                }
                R.id.doctor_rb -> {
                    binding.tilRegisterPoliclinic.show()
                    binding.tilRegisterPoliclinic.startAnimation(fromAnimation)
                }
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        hideKeyboard(event)
        return super.dispatchTouchEvent(event)
    }
}