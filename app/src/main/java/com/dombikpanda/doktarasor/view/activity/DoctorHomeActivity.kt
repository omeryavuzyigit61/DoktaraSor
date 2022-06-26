package com.dombikpanda.doktarasor.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.databinding.ActivityDoctorHomeBinding
import com.dombikpanda.doktarasor.view.fragment.*

class DoctorHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = DoctorHomeFragment()
        val listFragment = DoctorListFragment()
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
            chipNavigationBar.setItemSelected(R.id.homeFragment,true)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            addToBackStack(null)
            commit()
        }
}