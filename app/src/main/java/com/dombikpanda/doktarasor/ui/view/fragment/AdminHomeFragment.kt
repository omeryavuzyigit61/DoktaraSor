package com.dombikpanda.doktarasor.ui.view.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dombikpanda.doktarasor.R
import com.dombikpanda.doktarasor.charts.view.LineChart
import com.dombikpanda.doktarasor.charts.view.PieChart
import com.dombikpanda.doktarasor.databinding.FragmentAdminHomeBinding

class AdminHomeFragment : Fragment() {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        binding.apply {
            val myColors = intArrayOf(
                Color.rgb(237, 91, 26),
                Color.rgb(7, 173, 234),
                Color.rgb(243, 205, 29)
            )

            val colors = ArrayList<Int>()
            for (c in myColors) colors.add(c)

            val pieChart=PieChart()
            pieChart.charts(pieChartView,colors)
        }

        binding.apply {
            val lineChart = LineChart()
            lineChart.charts(lineChartView, getString(R.string.asked),getString(R.string.answeredQue))
        }

        return binding.root
    }


}