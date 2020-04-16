package cat.tfg.pama

import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_child_vaccines_data.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

data class Expenditure(var title: String, var date: String, var price: Double,var description: String)

/**
 * A simple [Fragment] subclass.
 */
class ChildExpensesFragment : Fragment(),Helper {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_EXPENSES = "http://10.0.2.2:8000/api/expenses"

    private val expenses_list: MutableList<Expenditure> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_expenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)

        OkHttpRequest.GET(URL_EXPENSES, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        addExpensesToList(getExpenses(response));
                        addItemsBottomLine()
                        addListAdapter()
                    }
                    500 -> showMessage(STANDARD_MESSAGE_ERROR)
                    else -> {
                        val message = getResponseMessage(response);
                        if(message != null){
                            showMessage(message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                showMessage(STANDARD_MESSAGE_ERROR);
            }
        })

        child_vaccines_data_save.setOnClickListener {
            changeFragmentToAddExpenditureFragment();
        }
    }

    private fun changeFragmentToAddExpenditureFragment() {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.content, AddVaccineFragment())
        transaction.commit()
    }

    private fun addItemsBottomLine(){
        activity?.runOnUiThread(Runnable {
            val itemDecor = DividerItemDecoration(context, ClipDrawable.HORIZONTAL)
            list_recycler_view.addItemDecoration(itemDecor)
        })
    }

    private fun addListAdapter(){
        activity?.runOnUiThread(Runnable {
            list_recycler_view.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ListAdapter(expenses_list, { expenditure_item : Any -> partItemClicked(expenditure_item) })
            }
        })
    }


    private fun partItemClicked(item : Any) {
        var expenditure_item = item as Expenditure;
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, expenditure_item.title+" clicked", Toast.LENGTH_SHORT).show()
        })
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun addExpensesToList(expenses: JSONArray){
        for (i in 0 until expenses.length()) {
            var expediture_jsonObject = expenses.getJSONObject(i)
            var expediture = Expenditure(expediture_jsonObject.getString("name"),expediture_jsonObject.getString("date"),expediture_jsonObject.getDouble("price"),expediture_jsonObject.getString("description"))
            expenses_list.add(expediture)
        }
    }

}
