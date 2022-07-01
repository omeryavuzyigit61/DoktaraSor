package com.dombikpanda.doktarasor.ui.view.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.dombikpanda.doktarasor.ui.view.fragment.HomeFragment
import com.dombikpanda.doktarasor.ui.view.fragment.ListFragment
import com.dombikpanda.doktarasor.ui.view.fragment.ProfileFragment
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.databinding.ActivityHomeBinding
import com.dombikpanda.doktarasor.hideKeyboard
import com.dombikpanda.doktarasor.service.MyFirebaseMessagingService

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val listFragment = ListFragment()
        val profileFragment = ProfileFragment()

        binding.apply {
            chipNavigationBar.setOnItemSelectedListener {
                when (it) {
                    R.id.homeFragment -> setCurrentFragment(homeFragment)
                    R.id.listFragment -> setCurrentFragment(listFragment)
                    R.id.profileFragment -> setCurrentFragment(profileFragment)
                }
                return@setOnItemSelectedListener
            }
            chipNavigationBar.setItemSelected(R.id.homeFragment, true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        val notificationIntent = Intent(this, MyFirebaseMessagingService::class.java)
        startService(notificationIntent)
        super.onStart()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            addToBackStack(null)
            commit()
        }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        hideKeyboard(event)
        return super.dispatchTouchEvent(event)
    }
}