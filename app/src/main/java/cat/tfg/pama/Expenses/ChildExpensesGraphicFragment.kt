package cat.tfg.pama.Expenses

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.APIConnection.OkHttpRequest
import cat.tfg.pama.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_child_expenses_graphic.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.DateFormatSymbols
import java.util.*

class ChildExpensesGraphicFragment: Fragment(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_EXPENSES_BY_DATE = "http://10.0.2.2:8000/api/expenses"
    val URL_FAMILY_USERS_COLORS = "http://10.0.2.2:8000/api/familyuserscolors"

    val spanish = Locale("es", "ES")
    var months = DateFormatSymbols(spanish).months;

    var current_year = - 1
    var current_month = -1

    lateinit var expenses_current_month:JSONObject

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_expenses_graphic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var expenses_current_month :JSONObject?= null
        val month = 4 + 1
        val currentYear = 2020
        var url = URL_EXPENSES_BY_DATE+"/"+currentYear+"/"+month

        current_month = Calendar.getInstance().get(Calendar.MONTH)
        current_year = Calendar.getInstance().get(Calendar.YEAR)
        this.updateDate(current_month,current_year)

        val dataList: MutableList<PieEntry> = ArrayList()

        OkHttpRequest.GET(url, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        expenses_current_month = getExpensesByMonth(response)
                        for(key in expenses_current_month!!.keys()) {
                            val value = JSONObject(expenses_current_month!![key].toString()).getString("value")
                            if(key == "1"){
                                dataList.add(PieEntry(value.toFloat(), "David"))
                            }else{
                                dataList.add(PieEntry(value.toFloat(), "Marta"))
                            }
                        }
                        piechart(pie_chart,dataList)
                    }
                    500 -> showMessage(STANDARD_MESSAGE_ERROR)
                    else -> {
                        val message = getResponseMessage(response);
                        if (message != null) {
                            showMessage(message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                showMessage(STANDARD_MESSAGE_ERROR);
            }
        })

        child_expenses_graphic_next_month.setOnClickListener(View.OnClickListener {
            if (current_month == 11) {
                current_month = 0
                current_year++
            } else {
                current_month++
            }
            updateDate(current_month,current_year)
            //updateExpensesByDate(current_month,current_year)
        })

        child_expenses_graphic_previous_month.setOnClickListener(View.OnClickListener {
            if (current_month == 0) {
                current_month = 11
                current_year--
            } else {
                current_month--
            }
            updateDate(current_month,current_year)
            //updateExpensesByDate(current_month,current_year)
        })
    }

    private fun updateDate(current_month: Int, current_year: Int){
        activity?.runOnUiThread(Runnable {
            child_expenses_graphic_date.text = months[current_month]+" "+current_year
        })
    }

    private fun piechart(piechart: PieChart, arrayList: MutableList<PieEntry>) {
        piechart.setUsePercentValues(true)
        piechart.description.isEnabled = false
        piechart.setExtraOffsets(2f, 5f, 2f, 2f)
        piechart.dragDecelerationFrictionCoef = 0.95f
        piechart.isDrawHoleEnabled = true
        piechart.setHoleColor(Color.WHITE)
        piechart.transparentCircleRadius = 61f
        val pieDataSet = PieDataSet(arrayList, " ")
        pieDataSet.sliceSpace = 3f
        pieDataSet.selectionShift = 5f
        // Custom colors to in the pie chart
        val colors = intArrayOf(Color.rgb(13, 166, 10), Color.rgb(255, 140, 0))
        val arrayList1: ArrayList<Int> = ArrayList()
        for (c in colors) {
            arrayList1.add(c)
        }
        pieDataSet.colors = arrayList1
        pieDataSet.colors = ColorTemplate.createColors(colors)
        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(18f)
        pieData.setValueTextColor(Color.WHITE)
        piechart.data = pieData
        piechart.setCenterTextSize(30f)
        piechart.setDrawEntryLabels(false)
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }
}