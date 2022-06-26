package com.dombikpanda.doktarasor.charts.view

import android.graphics.Color
import com.dombikpanda.doktarasor.charts.data.ChartsData
import com.dombikpanda.doktarasor.charts.data.XaxisData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class LineChart {
    val chartData = ChartsData()
    val xAxisData = XaxisData()

    fun charts(lineChartView: LineChart,data:String,data2:String) {
        chartData.dataValue(object :ChartsData.MyCallbackEntry{
            override fun onCallback(value: ArrayList<Entry>,value2:ArrayList<Entry>) {
                val lineDataSet = LineDataSet(value, data)
                val lineDataSet2 = LineDataSet(value2, data2)
                lineDataSet2.color = Color.rgb(233, 182, 29)
                val iLineDataSets: ArrayList<ILineDataSet> = ArrayList()
                iLineDataSets.add(lineDataSet)
                iLineDataSets.add(lineDataSet2)
                val lineData = LineData(iLineDataSets)
                lineChartView.data = lineData
                lineChartView.invalidate()
            }
        })

        val xAxis: XAxis = lineChartView.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisData.getAreaCount())
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        val yAxisRight: YAxis = lineChartView.axisRight
        yAxisRight.isEnabled = false
    }
}