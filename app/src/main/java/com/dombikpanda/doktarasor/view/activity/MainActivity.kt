package com.dombikpanda.doktarasor.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dombikpanda.doktarasor.MyContextWrapper
import com.dombikpanda.doktarasor.MyPreference
import com.dombikpanda.doktarasor.R


class MainActivity : AppCompatActivity() {

    private lateinit var myPreference: MyPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 750)
    }

    override fun attachBaseContext(newBase: Context?) {
        myPreference = MyPreference(newBase!!)
        val lang: String? = myPreference.getLanguageCount()
        super.attachBaseContext(lang?.let { MyContextWrapper.wrap(newBase, it) })
    }
}