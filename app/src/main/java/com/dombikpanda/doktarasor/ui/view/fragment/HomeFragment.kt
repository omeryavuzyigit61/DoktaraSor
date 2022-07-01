package com.dombikpanda.doktarasor.ui.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.dombikpanda.doktarasor.*
import com.dombikpanda.doktarasor.ui.Factories.HomeViewModelFactory
import com.dombikpanda.doktarasor.databinding.FragmentHomeBinding
import com.dombikpanda.doktarasor.data.model.Questions
import com.dombikpanda.doktarasor.showShortToast
import com.dombikpanda.doktarasor.ui.viewmodel.HomeViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    private val factory:HomeViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var policinic: String
    private lateinit var title: String
    private lateinit var description: String
    private var prefences: SharedPreferences? = null

    override fun onResume() {
        super.onResume()
        val doctorlist = resources.getStringArray(R.array.doctorlist)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, doctorlist)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.setDropDownBackgroundResource(R.color.white)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
        prefences = activity?.getSharedPreferences("kontrol", Context.MODE_PRIVATE)
        binding.apply {
            binding.btnHomeSubmit.setOnClickListener {
                when {
                    TextUtils.isEmpty(autoCompleteTextView.text.toString().trim { it <= ' ' }) -> {
                        showShortToast(getString(R.string.please_enter_policlinic))
                    }
                    TextUtils.isEmpty(etTitle.text.toString().trim { it <= ' ' }) -> {
                        showShortToast(getString(R.string.please_enter_title))
                    }
                    TextUtils.isEmpty(etDescription.text.toString().trim { it <= ' ' }) -> {
                        showShortToast(getString(R.string.please_enter_description))
                    }
                    else -> {
                        val date=System.currentTimeMillis()
                        if (prefences?.getString("todayQuestion", null) != SimpleDateFormat(date,"dd MMMM yyyy")) {
                            val questions = getQuestion()
                            homeViewModel.questionsCreate(questions, "questions", requireContext())
                        }
                        else{
                            showLongToast(getString(R.string.cant_ask_questions))
                        }
                    }
                }
            }
        }
        return binding.root
    }

    private fun getQuestion(): Questions {
        val editor = prefences?.edit()
        binding.apply {
            policinic = autoCompleteTextView.textValue.trim()
            title = etTitle.textValue.trim()
            description = etDescription.textValue.trim()
        }
        val uid = homeViewModel.firebaeAuth.uid.toString()
        val date = System.currentTimeMillis()
        editor?.putLong("date", date)
        editor?.putString("todayQuestion", SimpleDateFormat(date,"dd MMMM yyyy"))
        editor?.apply()
        return Questions(uid, policinic, title, description, date, false, true)
    }
}