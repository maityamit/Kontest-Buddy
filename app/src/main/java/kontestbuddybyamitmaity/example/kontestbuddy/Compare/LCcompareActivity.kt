package kontestbuddybyamitmaity.example.kontestbuddy.Compare

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kontestbuddybyamitmaity.example.kontestbuddy.R


class LCcompareActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lccompare)

        firstBarChart(95f,76f)
        secondBarChart(1997f,2315f)
        thirdLineCHart()


    }

    private fun thirdLineCHart() {
        val lineChart = findViewById<LineChart>(R.id.LCchart3)

        // Sample data for the chart
        val entries = listOf(
            Entry(1f, 97.13f),
            Entry(2f, 99.46f)
        )

        lineChart.axisLeft.axisMinimum = 0f
        lineChart.axisRight.axisMinimum = 0f
        lineChart.axisLeft.axisMaximum = 120f
        lineChart.axisRight.axisMaximum = 120f
        lineChart.xAxis.axisMinimum = 0.5f
        lineChart.xAxis.axisMaximum = 2.5f

        // Create a LineDataSet
        val dataSet = LineDataSet(entries, "")
        dataSet.color = Color.WHITE

        val mutableColorList: MutableList<Int> = mutableListOf()
        mutableColorList.add(Color.parseColor("#FC911F"))
        mutableColorList.add(Color.parseColor("#0370D6"))

        dataSet.setValueTextColors(mutableColorList)
        dataSet.valueTextSize = 16f
        lineChart.xAxis.setDrawLabels(false)

        // Create a LineData object from the LineDataSet
        val lineData = LineData(dataSet)

        // Set data to the chart
        lineChart.data = lineData

        // Customize the appearance of the chart
        lineChart.setDrawGridBackground(false)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false

        // Customize X axis
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Customize Y axis
        lineChart.axisRight.isEnabled = false
        val yAxis: YAxis = lineChart.axisLeft
        yAxis.textColor = Color.parseColor("#6F6F6F")

        // Refresh the chart
        lineChart.invalidate()
    }

    private fun secondBarChart(fl: Float, fl1: Float) {
        val horizontalBarChart: HorizontalBarChart = findViewById(R.id.LCchart2)

        val arrayList = ArrayList<BarEntry>()
        arrayList.add(BarEntry(0f, 1993f))
        arrayList.add(BarEntry(1f, 2347f))

        val barDataSet = BarDataSet(arrayList, "")
        val barData = BarData(barDataSet)
        horizontalBarChart.axisLeft.axisMinimum = 0f
        horizontalBarChart.axisRight.axisMinimum = 0f
        horizontalBarChart.axisLeft.axisMaximum = 3000f
        horizontalBarChart.xAxis.setDrawGridLines(false)

        val mutableColorList: MutableList<Int> = mutableListOf()
        mutableColorList.add(Color.parseColor("#FC911F"))
        mutableColorList.add(Color.parseColor("#0370D6"))
        barDataSet.colors = mutableColorList

        barDataSet.setValueTextColors(mutableColorList)

        barDataSet.valueTextSize = 16f
        horizontalBarChart.xAxis.setDrawLabels(false)
        horizontalBarChart.description.isEnabled = false
        horizontalBarChart.data = barData
        horizontalBarChart.axisLeft.textColor = Color.parseColor("#6F6F6F")
        horizontalBarChart.axisRight.textColor = Color.parseColor("#6F6F6F")
        horizontalBarChart.invalidate()
    }

    private fun firstBarChart(fl: Float, fl1: Float) {

        lateinit var barData:BarData
        lateinit var barDataSet:BarDataSet

        val barChart:BarChart = findViewById(R.id.LCchart1)
        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.axisMinimum = 0f
        barChart.axisLeft.textColor = Color.parseColor("#6F6F6F")
        barChart.axisRight.textColor = Color.parseColor("#6F6F6F")

        val arrayList:ArrayList<BarEntry> = ArrayList<BarEntry>()
        arrayList.add(BarEntry(1f, fl))
        arrayList.add(BarEntry(2f, fl1))

        barDataSet = BarDataSet(arrayList, "")


        barData = BarData(barDataSet)

        barChart.xAxis.setDrawLabels(false)
        barData.barWidth = 0.5f

        barChart.data = barData
        barChart.invalidate()
        val mutableColorList: MutableList<Int> = mutableListOf()
        mutableColorList.add(Color.parseColor("#FC911F"))
        mutableColorList.add(Color.parseColor("#0370D6"))
        barDataSet.colors = mutableColorList

        barDataSet.setValueTextColors(mutableColorList)
        barDataSet.valueTextSize = (16f);
        barChart.description.isEnabled = false
    }
}