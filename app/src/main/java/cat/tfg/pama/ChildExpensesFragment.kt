package cat.tfg.pama

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_child_expenses.*
import kotlinx.android.synthetic.main.fragment_child_vaccines_data.list_recycler_view
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException


data class Expenditure(var id: Int, var title: String, var date: String, var price: Double,var description: String)

/**
 * A simple [Fragment] subclass.
 */
class ChildExpensesFragment : Fragment(),Helper {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_EXPENSES = "http://10.0.2.2:8000/api/expenses"

    private val expenses_list: MutableList<Expenditure> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_expenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity!!.setTitle("Gastos")

        OkHttpRequest.GET(URL_EXPENSES, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        addExpensesToList(getExpenses(response));
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

        child_expenses_add.setOnClickListener {
            changeFragmentToAddExpenditureFragment();
        }
    }

    private fun changeFragmentToAddExpenditureFragment() {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout, AddExpenditureFragment())
        transaction.commit()
    }

    private fun addListAdapter(){
        activity?.runOnUiThread(Runnable {
            list_recycler_view.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ListAdapter(expenses_list, { expenditure_item : Any -> expenditureItemClicked(expenditure_item) })
            }
        })
    }

    private fun expenditureItemClicked(item : Any) {
        var expenditure_item = item as Expenditure;
        changeFragmentToExpenditureDetailFragment(expenditure_item);
    }

    private fun changeFragmentToExpenditureDetailFragment(expenditure_item: Expenditure) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout, ExpenditureDetailsFragment.newInstance(expenditure_item.id, expenditure_item.title, expenditure_item.date, expenditure_item.price,expenditure_item.description))
        transaction.commit()
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun addExpensesToList(expenses: JSONArray){

        for (i in 0 until expenses.length()) {
            var expediture_jsonObject = expenses.getJSONObject(i)
            var expediture = Expenditure(expediture_jsonObject.getInt("id"),expediture_jsonObject.getString("name"),expediture_jsonObject.getString("date"),expediture_jsonObject.getDouble("price"),expediture_jsonObject.getString("description"))
            expenses_list.add(expediture)
        }

        expenses_list.sortByDescending { it.date }
    }
}
