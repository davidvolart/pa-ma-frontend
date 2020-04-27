package cat.tfg.pama

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_add_task.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class AddTaskFragment() : Fragment(), Helper {

    private val URL_STORE_EXPENDITURE = "http://10.0.2.2:8000/api/task"
    private val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    private val SUCCESSFUL_MESSAGE = "Se ha creado correctamente."

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        add_task_date.setOnClickListener(object : View.OnClickListener {
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
                        (add_task_date as EditText).setText(selectedDate)
                    },
                    year, month, day
                )

                datepicker.show()
            }
        })

        add_task_cancel.setOnClickListener {
            changeFragmentToChildTasksFragment();
        }

        add_task_create.setOnClickListener {
            storeTask();
        }
    }

    private fun storeTask() {
        OkHttpRequest.POST(URL_STORE_EXPENDITURE, getParameters(), object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    201 -> {
                        showMessage(SUCCESSFUL_MESSAGE)
                        changeFragmentToChildTasksFragment();
                    };
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

    private fun getParameters(): HashMap<String, String> {
        val parameters = HashMap<String, String>()
        parameters.put("name", add_task_title.text.toString())
        parameters.put("date", add_task_date.text.toString())
        parameters.put("assigne_me", add_task_assign_me.isChecked.toString())

        if (add_task_description.text.toString() != "") {
            parameters.put("description", add_task_description.text.toString())
        }

        return parameters
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun changeFragmentToChildTasksFragment() {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout, TasksFragment())
        transaction.commit()
    }
}