package kontestbuddybyamitmaity.example.kontestbuddy.Compare

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import com.google.gson.JsonParser
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeCompareApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeVerifyApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.R


class LCcompareActivity : AppCompatActivity() {

    lateinit var leetcodeCmp_userName_user1:TextView
    lateinit var leetcodeCmp_userName_user2:TextView
    lateinit var leetcodeCmp_userName_user11:TextView
    lateinit var leetcodeCmp_userName_user22:TextView
    lateinit var leetcodeCmp_globalRank_user1:TextView
    lateinit var leetcodeCmp_globalRank_user2:TextView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lccompare)
        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("Sometimes it takes too longer !! \n Free services")


        initialization()
        val extras = intent.extras
        val userName1 = extras?.getString("userName1")
        val userName2 = extras?.getString("userName2")
        callTheAPI(userName1,userName2)
    }

    private fun callTheAPI(userName1: String?, userName2: String?) {
        progressDialog.show()
        val str:String = userName1+";"+userName2
        val apiTask = LeetCodeCompareApiTask { isValid ->

            leetcodeCmp_userName_user1.text = userName1
            leetcodeCmp_userName_user2.text = userName2
            leetcodeCmp_userName_user11.text = userName1
            leetcodeCmp_userName_user22.text = userName2

            val parser = JsonParser()
            val jsonArray = parser.parse(isValid).asJsonArray

            val user1Rating:String = jsonArray.get(0).asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("rating").toString()
            val user2Rating:String = jsonArray.get(1).asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("rating").toString()
            secondBarChart(user1Rating.toFloat(),user2Rating.toFloat())

            val user1Contest:String = jsonArray.get(0).asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("attendedContestsCount").toString()
            val user2Contest:String = jsonArray.get(1).asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("attendedContestsCount").toString()
            firstBarChart(user1Contest.toFloat(),user2Contest.toFloat())

            val user1Global:String = jsonArray.get(0).asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("globalRanking").toString()
            val user2Global:String = jsonArray.get(1).asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("globalRanking").toString()
            leetcodeCmp_globalRank_user1.text = user1Global
            leetcodeCmp_globalRank_user2.text = user2Global

            val user1Pos:String = jsonArray.get(0).asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("topPercentage").toString()
            val user2Pos:String = jsonArray.get(1).asJsonObject.get("result").asJsonArray.get(0).asJsonObject.get("topPercentage").toString()
            thirdLineCHart(100-user1Pos.toFloat(),100-user2Pos.toFloat())


            progressDialog.dismiss()

        }
        apiTask.execute(str)
    }

    private fun initialization() {
        leetcodeCmp_userName_user1 = findViewById(R.id.leetcodeCmp_userName_user1)
        leetcodeCmp_userName_user2 = findViewById(R.id.leetcodeCmp_userName_user2)
        leetcodeCmp_userName_user11 = findViewById(R.id.leetcodeCmp_userName_user11)
        leetcodeCmp_userName_user22 = findViewById(R.id.leetcodeCmp_userName_user22)
        leetcodeCmp_globalRank_user1 = findViewById(R.id.leetcodeCmp_globalRank_user1)
        leetcodeCmp_globalRank_user2 = findViewById(R.id.leetcodeCmp_globalRank_user2)
    }

    private fun thirdLineCHart(fl:Float, fl1:Float) {
        val lineChart = findViewById<LineChart>(R.id.LCchart3)

        // Sample data for the chart
        val entries = listOf(
            Entry(1f, fl),
            Entry(2f, fl1)
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
        arrayList.add(BarEntry(0f, fl))
        arrayList.add(BarEntry(1f, fl1))

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