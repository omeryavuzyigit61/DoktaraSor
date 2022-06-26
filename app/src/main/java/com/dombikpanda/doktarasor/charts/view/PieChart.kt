package com.dombikpanda.doktarasor.charts.view

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PieChart {

    fun charts(pieChartView: PieChart,colors:ArrayList<Int>) {
        val pieEntry = arrayListOf<PieEntry>()
            pieEntry.add(PieEntry(3f, "Doktorlar"))
            pieEntry.add(PieEntry(7f, "Kullanıcılar"))
            val pieDataSet = PieDataSet(pieEntry, "")
            pieDataSet.valueTextSize = 15f
            pieDataSet.colors = colors
            val pieData = PieData(pieDataSet)
            pieChartView.data = pieData
            pieChartView.description.text = "Kullanıcı Sayıları"
            pieChartView.centerText = "Veriler"
    }

    fun funPieDataSet(pieEntry: ArrayList<PieEntry>, label: String): PieDataSet {
        val pieDataSet = PieDataSet(pieEntry, label)
        return pieDataSet
    }
}