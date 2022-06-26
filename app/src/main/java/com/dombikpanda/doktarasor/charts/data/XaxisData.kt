package com.dombikpanda.doktarasor.charts.data

class XaxisData {
    fun getAreaCount(): ArrayList<String> {
        val xAxisLabel: ArrayList<String> = ArrayList()
        xAxisLabel.add("Mon")
        xAxisLabel.add("Tue")
        xAxisLabel.add("Wed")
        xAxisLabel.add("Thu")
        xAxisLabel.add("Fri")
        xAxisLabel.add("Sat")
        xAxisLabel.add("Sun")
        return xAxisLabel
    }
}