package com.dombikpanda.doktarasor.ui.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.databinding.ActivityAdminBinding
import com.dombikpanda.doktarasor.hideKeyboard

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {
            val navController=findNavController(R.id.fragmentAdmin)
            adminMenu.setupWithNavController(navController)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        hideKeyboard(event)
        return super.dispatchTouchEvent(event)
    }
}