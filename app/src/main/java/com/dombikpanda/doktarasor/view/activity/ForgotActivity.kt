package com.dombikpanda.doktarasor.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dombikpanda.doktarasor.databinding.ActivityForgotBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSubmit.setOnClickListener {
                val email: String = etForgotEmail.text.toString().trim { it <= ' ' }
                if (email.isEmpty()) {
                    Toast.makeText(
                        this@ForgotActivity,
                        "Please enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@ForgotActivity,
                                    "Email sent successfully to reset your password",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@ForgotActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}