package cat.tfg.pama.Calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.APIConnection.OkHttpRequest
import cat.tfg.pama.Adapter.ListAdapter
import cat.tfg.pama.R
import cat.tfg.pama.Tasks.Task
import cat.tfg.pama.Tasks.TaskDetailsFragment
import kotlinx.android.synthetic.main.fragment_calendar.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/*
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_TASKS = "http://10.0.2.2:8000/api/tasks"

    private val tasks_list: MutableList<Task> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date(calendar.date))
        sendGetRequest(currentDate)

        calendar.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            tasks_list.clear()
            val month_selected = month +1
            sendGetRequest("$year-$month_selected-$dayOfMonth")
        })
    }

    private fun addListAdapter(){
        activity?.runOnUiThread(Runnable {
            list_events_per_day.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ListAdapter(
                        tasks_list,
                        { task_item: Any -> taskItemClicked(task_item) })
            }
        })
    }

    private fun taskItemClicked(item : Any) {
        val task_item = item as Task;
        changeFragmentToTaskDetailFragment(task_item);
    }

    private fun changeFragmentToTaskDetailFragment(task_item: Task) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout, TaskDetailsFragment.newInstance(task_item.id, task_item.title, task_item.date, task_item.description, task_item.assigned_to))
                .addToBackStack("Tasks")
        transaction.commit()
    }

    private fun addTasksToList(tasks: JSONArray){

        for (i in 0 until tasks.length()) {
            val task_jsonObject = tasks.getJSONObject(i)
            val task = Task(
                    task_jsonObject.getInt("id"),
                    task_jsonObject.getString("name"),
                    task_jsonObject.getString("date"),
                    task_jsonObject.getString("description"),
                    task_jsonObject.getString("user_email"),
                    task_jsonObject.getString("color")
            )
            tasks_list.add(task)
        }

        tasks_list.sortByDescending { it.date }
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onResume() {
        super.onResume()
        tasks_list.clear()
        activity!!.setTitle("Calendario")
    }

    private fun sendGetRequest(selected_date:String){

        OkHttpRequest.GET("$URL_TASKS/$selected_date", object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        addTasksToList(getTasks(response));
                        addListAdapter()
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

    }
}
