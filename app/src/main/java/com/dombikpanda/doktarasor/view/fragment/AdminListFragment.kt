package com.dombikpanda.doktarasor.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dombikpanda.doktarasor.adapter.DoctorListAdapter
import com.dombikpanda.doktarasor.databinding.FragmentAdminListBinding
import com.dombikpanda.doktarasor.model.Doctor
import com.dombikpanda.doktarasor.showShortToast
import com.dombikpanda.doktarasor.viewmodel.ListViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class AdminListFragment : Fragment(),DoctorListAdapter.onItemClickListener {

    private lateinit var _binding: FragmentAdminListBinding
    private val binding
        get() = _binding
    private lateinit var listViewModel: ListViewModel
    private lateinit var listAdapter: DoctorListAdapter
    private lateinit var doctor: Doctor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminListBinding.inflate(inflater, container, false)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listAdapter = DoctorListAdapter(this)

        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listAdapter
            }
        }
        obseveData()
        return binding.root
    }

    override fun onItemClick(position: Int) {
        doctor = listAdapter.dataList[position]
        showShortToast(doctor.email)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun obseveData() {
        listViewModel.apply {
            realtimeDoctorList(requireContext()).observe(viewLifecycleOwner, {
                listAdapter.setListData(it)
                listAdapter.notifyDataSetChanged()
            })
        }
    }

}