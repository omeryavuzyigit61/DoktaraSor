package com.dombikpanda.doktarasor.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import com.dombikpanda.doktarasor.*
import com.dombikpanda.doktarasor.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.dombikpanda.doktarasor.viewmodel.LoginRegisterViewModel
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel

    companion object {
        private const val RC_SIGN_IN = 120
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginRegisterViewModel = ViewModelProvider(this).get(
            LoginRegisterViewModel::class.java
        )
        loginRegisterViewModel.userLiveData.observe(this,
            { firebaseUser ->
                if (firebaseUser != null) {
                    Timber.i("Succes")
                }
            })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))  //Bu hata değil arada kendini görüyor arada görmüyor. Kod sorunusuz çalışıyor görsede görmesede
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = FirebaseAuth.getInstance()

        binding.apply {

            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            signInBtn.setOnClickListener {
                signIn()
            }

            tvForgotPassword.setOnClickListener {
                val forgotintent = Intent(this@LoginActivity, ForgotActivity::class.java)
                startActivity(forgotintent)
            }

            btnLogin.setOnClickListener {
                when {
                    TextUtils.isEmpty(etLoginEmail.text.toString().trim { it <= ' ' }) -> {
                        showShortToast(getString(R.string.please_enter_email))
                    }
                    TextUtils.isEmpty(etLoginPassword.text.toString().trim { it <= ' ' }) -> {
                        showShortToast(getString(R.string.please_enter_password))
                    }
                    else -> {
                        val email: String = etLoginEmail.textValue.trim { it <= ' ' }
                        val password: String = etLoginPassword.textValue.trim { it <= ' ' }

                        loginRegisterViewModel.login(email, password, this@LoginActivity)

                    }
                }
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                showShortToast(getString(R.string.google_sigin_failed))
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        loginRegisterViewModel.updateUI(this)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loginRegisterViewModel.updateUI(this)
                } else {
                    showShortToast(getString(R.string.google_sigin_failed))
                }
            }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        hideKeyboard(event)
        return super.dispatchTouchEvent(event)
    }
}