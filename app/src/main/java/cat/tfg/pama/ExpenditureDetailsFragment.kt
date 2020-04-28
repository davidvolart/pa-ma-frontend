package cat.tfg.pama

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_add_expenditure.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class ExpenditureDetailsFragment() : Fragment(), Helper {

    private val URL_STORE_EXPENDITURE = "http://10.0.2.2:8000/api/expenses"
    private var URL_DELETE_EXPENDITURE = "http://10.0.2.2:8000/api/expenses/"
    private val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    private val SUCCESSFUL_DELETE_MESSAGE = "Se ha eliminado correctamente."
    private val SUCCESSFUL_MESSAGE = "Se ha creado correctamente."
    private val BUTTON_SAVE_TEXT = "Guardar"
    private val BUTTON_DELETE_TEXT = "Eliminar"

    companion object {
        fun newInstance(id: Int, title: String, date: String, price: Double, description: String): ExpenditureDetailsFragment {
            val fragment = ExpenditureDetailsFragment()
            val args = Bundle()
            args.putInt("id", id)
            args.putString("title", title)
            args.putString("date", date)
            args.putDouble("price", price)
            args.putString("description", description)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_expenditure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val args = arguments
        add_expenditure_title.setText(args?.getString("title", ""));
        var expenditure_date = this.getDateInEuropeanFormat(args!!.getString("date", ""))
        add_expenditure_date.setText(expenditure_date);
        val price = args.getDouble("price", 0.0).toString();
        add_expenditure_price.setText(price);

        val description = args.getString("description")
        if(description != "null"){
            add_expenditure_description.setText(description);
        }

        add_expenditure_create.setText(BUTTON_SAVE_TEXT)
        add_expenditure_cancel.setText(BUTTON_DELETE_TEXT)

        add_expenditure_date.setOnClickListener(object : View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            override fun onClick(v: View) {

                val datepicker = DatePickerDialog(
                    activity!!,
                    { view, year, monthOfYear, dayOfMonth ->
                        val selectedDate =
                            dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                        (add_expenditure_date as EditText).setText(selectedDate)
                    },
                    year, month, day
                )

                datepicker.show()
            }
        })

        add_expenditure_cancel.setOnClickListener {
            deleteExpenditure();
        }

        add_expenditure_create.setOnClickListener {
            updateExpenditure();
        }
    }

    private fun getDateInEuropeanFormat(expenditure_date: String): String{
        var birthdate_parts = expenditure_date.split("-", ignoreCase = true, limit  = 0)
        return birthdate_parts[2]+'/'+birthdate_parts[1]+'/'+birthdate_parts[0]
    }

    private fun deleteExpenditure(){
        URL_DELETE_EXPENDITURE = URL_DELETE_EXPENDITURE + arguments!!.getInt("id").toString()
        OkHttpRequest.DELETE(URL_DELETE_EXPENDITURE, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        showMessage(SUCCESSFUL_DELETE_MESSAGE)
                        changeFragmentToChildExpensesDataFragment();
                    };
                    500 -> showMessage("error 500")
                    else -> {
                        val message = getResponseMessage(response);
                        if(message != null){ showMessage(message) }
                    }
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                showMessage(STANDARD_MESSAGE_ERROR);
            }
        })
    }

    private fun updateExpenditure(){
        OkHttpRequest.POST(URL_STORE_EXPENDITURE, getParameters(),object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    201 -> {
                        showMessage(SUCCESSFUL_MESSAGE)
                        changeFragmentToChildExpensesDataFragment();
                    };
                    500 -> showMessage("error 500")
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
    }

    private fun getParameters(): HashMap<String, String> {
        val parameters = HashMap<String, String>()

        parameters.put("id", arguments!!.getInt("id").toString())
        parameters.put("name", add_expenditure_title.text.toString())
        parameters.put("price", add_expenditure_price.text.toString())
        parameters.put("date", add_expenditure_date.text.toString())

        if(add_expenditure_description.text.toString() != ""){
            parameters.put("description", add_expenditure_description.text.toString())
        }

        return parameters
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun changeFragmentToChildExpensesDataFragment() {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout, ChildExpensesFragment())
        transaction.commit()
    }
}