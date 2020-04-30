package cat.tfg.pama.Tasks

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
import kotlinx.android.synthetic.main.fragment_add_task.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class TaskDetailsFragment() : Fragment(), APIResponseHandler {

    private val URL_STORE_TASK = "http://10.0.2.2:8000/api/task"
    private var URL_DELETE_TASK = "http://10.0.2.2:8000/api/tasks/"
    private val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    private val SUCCESSFUL_UPDATE_MESSAGE = "Se ha actualizado correctamente."
    private val SUCCESSFUL_DELETE_MESSAGE = "Se ha eliminado correctamente."
    private val ASSIGNED_TO = "Assignado a "
    private val BUTTON_SAVE_TEXT = "Guardar"
    private val BUTTON_DELETE_TEXT = "Eliminar"

    companion object {
        fun newInstance(id: Int, title: String, date: String, description: String, assigned_to: String): TaskDetailsFragment {
            val fragment = TaskDetailsFragment()
            val args = Bundle()
            args.putInt("id", id)
            args.putString("title", title)
            args.putString("date", date)
            args.putString("description", description)
            args.putString("assigned_to", assigned_to)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val args = arguments
        add_task_title.setText(args?.getString("title", ""));
        var task_date = this.getDateInEuropeanFormat(args!!.getString("date", ""))
        add_task_date.setText(task_date);

        val assigned_to = args.getString("assigned_to")
        if(assigned_to != "null"){
            add_task_assigned_to.setVisibility(View.VISIBLE);
            add_task_assigned_to.setText(ASSIGNED_TO + assigned_to);
        }

        val description = args.getString("description")
        if(description != "null"){
            add_task_description.setText(description);
        }

        add_task_create.setText(BUTTON_SAVE_TEXT)
        add_task_cancel.setText(BUTTON_DELETE_TEXT)

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
            deleteTask();
        }

        add_task_create.setOnClickListener {
            updateTask();
        }
    }

    private fun getDateInEuropeanFormat(expenditure_date: String): String{
        var birthdate_parts = expenditure_date.split("-", ignoreCase = true, limit  = 0)
        return birthdate_parts[2]+'/'+birthdate_parts[1]+'/'+birthdate_parts[0]
    }


    private fun deleteTask(){
        URL_DELETE_TASK = URL_DELETE_TASK + arguments!!.getInt("id").toString()
        OkHttpRequest.DELETE(URL_DELETE_TASK, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        showMessage(SUCCESSFUL_DELETE_MESSAGE)
                        changeFragmentToChildTasksFragment();
                    };
                    500 -> showMessage("error 500")
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

    private fun updateTask(){
        OkHttpRequest.POST(URL_STORE_TASK, getParameters(), object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    201 -> {
                        showMessage(SUCCESSFUL_UPDATE_MESSAGE)
                        changeFragmentToChildTasksFragment();
                    };
                    500 -> showMessage("error 500")
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

        parameters.put("id", arguments!!.getInt("id").toString())
        parameters.put("name", add_task_title.text.toString())
        parameters.put("date", add_task_date.text.toString())
        parameters.put("assigne_me", add_task_assign_me.isChecked.toString())

        if(add_task_description.text.toString() != ""){
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
        transaction.replace(R.id.frame_layout, TasksFragment()).addToBackStack(null)
        transaction.commit()
    }
}