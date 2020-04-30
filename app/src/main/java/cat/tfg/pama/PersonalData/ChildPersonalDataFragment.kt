package cat.tfg.pama.PersonalData

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.APIConnection.OkHttpRequest
import cat.tfg.pama.R
import kotlinx.android.synthetic.main.fragment_child_personal_data.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChildPersonalDataFragment : Fragment(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val SUCCESSFUL_SAVE_MESSAGE = "Se ha guardado correctamente."
    val URL_SAVE_PERSONAL_DATA = "http://10.0.2.2:8000/api/personaldata"
    val URL_GET_CHILD = "http://10.0.2.2:8000/api/child"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_personal_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        OkHttpRequest.GET(URL_GET_CHILD, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        var child = getChild(response)
                        setContent(child)
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

        child_personal_data_birthdate.setOnClickListener(object : View.OnClickListener {
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
                        (child_personal_data_birthdate as EditText).setText(selectedDate)
                    },
                    year, month, day
                )

                datepicker.datePicker.maxDate = c.getTimeInMillis()
                datepicker.show()
            }
        })

        child_persona_data_save.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                OkHttpRequest.POST(
                    URL_SAVE_PERSONAL_DATA,
                    getParameters(),
                    object : Callback {
                        override fun onResponse(call: Call?, response: Response) {
                            when (response.code()) {
                                200 -> showMessage(SUCCESSFUL_SAVE_MESSAGE)
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
            }
        })
    }

    private fun setContent(child: JSONObject){
        activity?.runOnUiThread(Runnable {
            child_personal_data_name.setText(child.getString("name"));
            if(child.getString("id_card") != "null"){
                child_personal_data_ID.setText(child.getString("id_card"));
            }

            if(child.getString("health_care_number") != "null"){
                child_personal_data_health_care_id.setText(child.getString("health_care_number"));
            }

            if(child.getString("birthdate") != "null"){
                var birthdate = this.getDateInEuropeanFormat(child.getString("birthdate"))
                child_personal_data_birthdate.setText(birthdate);
            }
        })
    }

    private fun getDateInEuropeanFormat(birthdate: String): String{
        var birthdate_parts = birthdate.split("-", ignoreCase = true, limit  = 0)
        return birthdate_parts[2]+'/'+birthdate_parts[1]+'/'+birthdate_parts[0]
    }

    private fun getParameters(): HashMap<String, String> {
        val parameters = HashMap<String, String>()

        if(child_personal_data_name.text.toString() != ""){
            parameters.put("name", child_personal_data_name.text.toString())
        }

        if(child_personal_data_ID.text.toString() != ""){
            parameters.put("id_card", child_personal_data_ID.text.toString())
        }

        if(child_personal_data_health_care_id.text.toString() != ""){
            parameters.put("health_care_number", child_personal_data_health_care_id.text.toString())
        }

        if(child_personal_data_birthdate.text.toString() != ""){
            parameters.put("birthdate", child_personal_data_birthdate.text.toString())
        }

        return parameters
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }
}