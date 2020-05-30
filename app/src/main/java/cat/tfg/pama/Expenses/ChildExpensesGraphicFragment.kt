package cat.tfg.pama.Expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    val spanish = Locale("es", "ES")
    var months = DateFormatSymbols(spanish).months;

    var current_year = - 1
    var current_month = -1

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_expenses_graphic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        current_month = Calendar.getInstance().get(Calendar.MONTH)
        current_year = Calendar.getInstance().get(Calendar.YEAR)
        this.updateDate(current_month,current_year)

        OkHttpRequest.GET("$URL_EXPENSES_BY_DATE/$current_year/$current_month", object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        val expenses_current_month = getExpensesByMonth(response)
                        val first_key = expenses_current_month.keys().next()
                        val percentage = JSONObject(expenses_current_month[first_key].toString()).getString("percentage")
                        stats_progressbar.setProgress(percentage.toFloat().roundToInt());
                        addValues(expenses_current_month)
                    }
                    else -> showMessage(STANDARD_MESSAGE_ERROR)
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

            var url = URL_EXPENSES_BY_DATE + "/" + current_year + "/" + current_month
            updateDate(current_month, current_year)

            OkHttpRequest.GET(url, object : Callback {
                override fun onResponse(call: Call?, response: Response) {
                    when (response.code()) {
                        200 -> {
                            var expenses_current_month = getExpensesByMonth(response)
                            val first_key = expenses_current_month.keys().next()
                            val percentage = JSONObject(expenses_current_month[first_key].toString()).getString("percentage")
                            stats_progressbar.setProgress(percentage.toFloat().roundToInt());
                            addValues(expenses_current_month)
                        }
                        else -> showMessage(STANDARD_MESSAGE_ERROR)
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    showMessage(STANDARD_MESSAGE_ERROR);
                }
            })
        })

        child_expenses_graphic_previous_month.setOnClickListener(View.OnClickListener {
            if (current_month == 0) {
                current_month = 11
                current_year--
            } else {
                current_month--
            }
            //var month = current_month + 1
            var url = URL_EXPENSES_BY_DATE + "/" + current_year + "/" + current_month
            updateDate(current_month, current_year)

            OkHttpRequest.GET(url, object : Callback {
                override fun onResponse(call: Call?, response: Response) {
                    when (response.code()) {
                        200 -> {
                            val expenses_current_month = getExpensesByMonth(response)
                            val first_key = expenses_current_month.keys().next()
                            val percentage = JSONObject(expenses_current_month[first_key].toString()).getString("percentage")
                            stats_progressbar.progress = percentage.toFloat().roundToInt();
                            addValues(expenses_current_month)
                        }
                        else -> showMessage(STANDARD_MESSAGE_ERROR)
                    }
                }
                override fun onFailure(call: Call?, e: IOException?) {
                    showMessage(STANDARD_MESSAGE_ERROR);
                }
            })
        })
    }

    private fun addValues(expensesCurrentMonth: JSONObject) {
        var i = 0
        for(key in expensesCurrentMonth.keys()){
            if(i == 0){
                activity?.runOnUiThread(Runnable {
                    expenses_graphic_tv_user1.text = key
                    expenses_user1_name.text = key
                    expenses_user1_value.text = JSONObject(expensesCurrentMonth[key].toString()).getString("value")+"€"
                })
            }else{
                activity?.runOnUiThread(Runnable {
                    expenses_graphic_tv_user2.text = key
                    expenses_user2_name.text = key
                    expenses_user2_value.text = JSONObject(expensesCurrentMonth[key].toString()).getString("value")+"€"
                })
            }
            i++
        }
    }

    private fun updateDate(current_month: Int, current_year: Int){
        activity?.runOnUiThread(Runnable {
            child_expenses_graphic_date.text = months[current_month]+" "+current_year
        })
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }
}