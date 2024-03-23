package kontestbuddybyamitmaity.example.kontestbuddy.Compare

import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.JsonParser
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.CodeForcesCompareApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.Backend.LeetCodeCompareApiTask
import kontestbuddybyamitmaity.example.kontestbuddy.R

class CFcompareActivity : AppCompatActivity() {

    lateinit var codeforcesCmp_userName_user1: TextView
    lateinit var codeforcesCmp_userName_user2: TextView
    lateinit var codeforcesCmp_userName_user11: TextView
    lateinit var codeforcesCmp_userName_user22: TextView
    lateinit var codeforcesCmp_globalRank_user1: TextView
    lateinit var codeforcesCmp_globalRank_user2: TextView
    private lateinit var progressDialog: ProgressDialog
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cfcompare)
        progressDialog= ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)

        initialization()
        val extras = intent.extras
        val userName1 = extras?.getString("userName1")
        val userName2 = extras?.getString("userName2")
        callTheAPI(userName1,userName2)
    }

    private fun callTheAPI(userName1: String?, userName2: String?) {
        progressDialog.show()
        val str:String = userName1+";"+userName2
        val apiTask = CodeForcesCompareApiTask { isValid ->

            codeforcesCmp_userName_user1.text = userName1
            codeforcesCmp_userName_user2.text = userName2
            codeforcesCmp_userName_user11.text = userName1
            codeforcesCmp_userName_user22.text = userName2


            val user1Rating:String = isValid?.let { JsonParser().parse(it).asJsonObject }?.get("result")?.asJsonArray?.get(0)?.asJsonObject?.get("maxRating").toString()
            val user2Rating:String = isValid?.let { JsonParser().parse(it).asJsonObject }?.get("result")?.asJsonArray?.get(1)?.asJsonObject?.get("maxRating").toString()
            secondBarChart(user1Rating.toFloat(),user2Rating.toFloat())

            val user1Contest:String = isValid?.let { JsonParser().parse(it).asJsonObject }?.get("result")?.asJsonArray?.get(0)?.asJsonObject?.get("rating").toString()
            val user2Contest:String = isValid?.let { JsonParser().parse(it).asJsonObject }?.get("result")?.asJsonArray?.get(1)?.asJsonObject?.get("rating").toString()
            firstBarChart(user1Contest.toFloat(),user2Contest.toFloat())

            val user1Global:String = isValid?.let { JsonParser().parse(it).asJsonObject }?.get("result")?.asJsonArray?.get(0)?.asJsonObject?.get("rank").toString()
            val user2Global:String = isValid?.let { JsonParser().parse(it).asJsonObject }?.get("result")?.asJsonArray?.get(1)?.asJsonObject?.get("rank").toString()
            codeforcesCmp_globalRank_user1.text = user1Global
            codeforcesCmp_globalRank_user2.text = user2Global


            progressDialog.dismiss()

        }
        apiTask.execute(str)
    }

    private fun initialization() {
        codeforcesCmp_userName_user1 = findViewById(R.id.codeforcesCmp_userName_user1)
        codeforcesCmp_userName_user2 = findViewById(R.id.codeforcesCmp_userName_user2)
        codeforcesCmp_userName_user11 = findViewById(R.id.codeforcesCmp_userName_user11)
        codeforcesCmp_userName_user22 = findViewById(R.id.codeforcesCmp_userName_user22)
        codeforcesCmp_globalRank_user1 = findViewById(R.id.codeforcesCmp_globalRank_user1)
        codeforcesCmp_globalRank_user2 = findViewById(R.id.codeforcesCmp_globalRank_user2)
    }

    private fun secondBarChart(fl: Float, fl1: Float) {
        val horizontalBarChart: HorizontalBarChart = findViewById(R.id.CFchart2)

        val arrayList = ArrayList<BarEntry>()
        arrayList.add(BarEntry(0f, fl))
        arrayList.add(BarEntry(1f, fl1))

        val barDataSet = BarDataSet(arrayList, "")
        val barData = BarData(barDataSet)
        horizontalBarChart.axisLeft.axisMinimum = 0f
        horizontalBarChart.axisRight.axisMinimum = 0f
        horizontalBarChart.axisLeft.axisMaximum = 2500f
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

        lateinit var barData: BarData
        lateinit var barDataSet: BarDataSet

        val barChart: BarChart = findViewById(R.id.CFchart1)
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