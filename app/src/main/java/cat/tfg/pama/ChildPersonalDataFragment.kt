package cat.tfg.pama

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_child_personal_data.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChildPersonalDataFragment : Fragment(), Helper  {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val SUCCESSFUL_SAVE_MESSAGE = "Se ha guardado correctamente."
    val URL = "http://10.0.2.2:8000/api/personaldata"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_child_personal_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        child_personal_data_birthdate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val datepicker = DatePickerDialog(
                    activity!!,
                    { view, year, monthOfYear, dayOfMonth ->
                        val selectedDate = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                        (child_personal_data_birthdate as EditText).setText(selectedDate)
                    },
                    year, month, day)

                datepicker.datePicker.maxDate = c.getTimeInMillis()
                datepicker.show()
            }
        })

        child_persona_data_save.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                OkHttpRequest.POST(URL, getParameters(), object : Callback {
                    override fun onResponse(call: Call?, response: Response) {
                        when (response.code()) {
                            200 -> showMessage(SUCCESSFUL_SAVE_MESSAGE)
                            500 -> showMessage(STANDARD_MESSAGE_ERROR)
                            else -> showMessage(getResponseMessage(response))
                        }
                    }
                    override fun onFailure(call: Call?, e: IOException?) {
                        showMessage(STANDARD_MESSAGE_ERROR);
                    }
                })
            }
        })
    }

    private fun getParameters(): HashMap<String, String> {
        val parameters = HashMap<String, String>()
        parameters.put("name", child_personal_data_name.text.toString())
        parameters.put("id", child_personal_data_ID.text.toString())
        parameters.put("health_care_number", child_personal_data_health_care_number.text.toString())
        parameters.put("birthdate", child_personal_data_birthdate.text.toString())

        return parameters
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }
}