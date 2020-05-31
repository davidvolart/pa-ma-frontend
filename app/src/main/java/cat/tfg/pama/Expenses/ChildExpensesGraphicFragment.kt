package cat.tfg.pama.Expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.APIConnection.OkHttpRequest
import cat.tfg.pama.R
import kotlinx.android.synthetic.main.fragment_child_expenses_graphic.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.text.DateFormatSymbols
import java.util.*
import kotlin.math.roundToInt

class ChildExpensesGraphicFragment: Fragment(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_EXPENSES_BY_DATE = "http://10.0.2.2:8000/api/expenses"

    var okHttpRequest: OkHttpRequest? = null

    var current_year = - 1
    var current_month = -1

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_expenses_graphic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        okHttpRequest = OkHttpRequest.getInstance(context)

        current_month = Calendar.getInstance().get(Calendar.MONTH)
        current_year = Calendar.getInstance().get(Calendar.YEAR)
        updateDate(current_month,current_year)

        getExpensesByMonth()

        child_expenses_graphic_next_month.setOnClickListener(View.OnClickListener {
            updateCurrentDateForNextMonth()
            updateDate(current_month, current_year)
            getExpensesByMonth()

        })

        child_expenses_graphic_previous_month.setOnClickListener(View.OnClickListener {
            updateCurrentDateForPreviousMonth()
            updateDate(current_month, current_year)
            getExpensesByMonth()
        })
    }

    private fun updateCurrentDateForPreviousMonth() {
        if (current_month == 0) {
            current_month = 11
            current_year--
        } else {
            current_month--
        }
    }

    private fun updateCurrentDateForNextMonth() {
        if (current_month == 11) {
            current_month = 0
            current_year++
        } else {
            current_month++
        }
    }

    private fun getExpensesByMonth() {
        okHttpRequest?.GET("$URL_EXPENSES_BY_DATE/$current_year/$current_month", object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        val expenses_current_month = getExpensesByMonth(response)
                        setPercentageToProgressBar(expenses_current_month)
                        addValues(expenses_current_month)
                    }
                    else -> showMessage(STANDARD_MESSAGE_ERROR)
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                showMessage(STANDARD_MESSAGE_ERROR);
            }
        })
    }

    private fun setPercentageToProgressBar(expensesCurrentMonth: JSONObject) {
        val first_key = expensesCurrentMonth.keys().next()
        val percentage = JSONObject(expensesCurrentMonth[first_key].toString()).getString("percentage")
        stats_progressbar.progress = percentage.toFloat().roundToInt();
    }

    private fun addValues(expensesCurrentMonth: JSONObject) {
        var i = 0
        for(key in expensesCurrentMonth.keys()){
            if(i == 0){
                addLegendValues(expenses_graphic_tv_user1, expenses_user1_name, expenses_user1_value, key, expensesCurrentMonth)
            }else{
                addLegendValues(expenses_graphic_tv_user2, expenses_user2_name, expenses_user2_value, key, expensesCurrentMonth)
            }
            i++
        }
    }

    private fun addLegendValues(top_legend_user_name: TextView, bottom_legend_user_name: TextView, bottom_legend_user_expenses: TextView, key: String?, expensesCurrentMonth: JSONObject) {
        activity?.runOnUiThread(Runnable {
            top_legend_user_name.text = key
            bottom_legend_user_name.text = key
            bottom_legend_user_expenses.text = JSONObject(expensesCurrentMonth[key].toString()).getString("value") + "€"
        })
    }

    private fun updateDate(current_month: Int, current_year: Int){
        activity?.runOnUiThread(Runnable {
            child_expenses_graphic_date.text = getMonthName(current_month)+" "+current_year
        })
    }

    private fun getMonthName(month: Int): String{
        val spanish = Locale("es", "ES")
        val months = DateFormatSymbols(spanish).months;
        return months[current_month].capitalize()
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }
}