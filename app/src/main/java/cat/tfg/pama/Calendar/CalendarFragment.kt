package cat.tfg.pama.Calendar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.APIConnection.OkHttpRequest
import cat.tfg.pama.Adapter.ListAdapter
import cat.tfg.pama.R
import cat.tfg.pama.Tasks.Task
import cat.tfg.pama.Tasks.TaskDetailsFragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_calendar.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

/*
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_TASKS = "http://10.0.2.2:8000/api/tasks"
    var okHttpRequest: OkHttpRequest? = null


    private val tasks_list: MutableList<Task> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        okHttpRequest = OkHttpRequest.getInstance(context)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        sendGetRequest(currentDate)

        val current_date_parts = currentDate.split("-")
        updateDate(current_date_parts[1].toInt() - 1 ,current_date_parts[0].toInt())
        getTasksForAMonth(current_date_parts[0],current_date_parts[1])

        calendar.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                tasks_list.clear()
                sendGetRequest(sdf.format(dateClicked))
            }
            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                calendar.removeAllEvents()
                tasks_list.clear()

                val calendar: Calendar = GregorianCalendar()
                calendar.time = firstDayOfNewMonth

                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH] + 1

                updateDate(month - 1,year)
                getTasksForAMonth(year.toString(),month.toString())
                sendGetRequest(sdf.format(firstDayOfNewMonth))
            }
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
        transaction.replace(R.id.frame_layout, TaskDetailsFragment.newInstance(task_item.id, task_item.title, task_item.date, task_item.description, task_item.assigned_to, task_item.calendar_provider_event_id))
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
                    task_jsonObject.getString("color"),
                    task_jsonObject.getString("calendar_provider_event_id")
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

        okHttpRequest?.GET("$URL_TASKS/$selected_date", object : Callback {
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

    private fun getTasksForAMonth(selected_year:String, selected_month:String){

        okHttpRequest?.GET("$URL_TASKS/$selected_year/$selected_month", object : Callback {
            override fun onResponse(call: Call?, response: Response){
                when (response.code()) {
                    200 -> {
                         addEventsToCalendar(getTaskMilis(getTasks(response)))
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

    private fun getTaskMilis(tasks: JSONArray): MutableList<Long>{

        val tasksMilis: MutableList<Long> = ArrayList()
        for (i in 0 until tasks.length()) {
            val task_jsonObject = tasks.getJSONObject(i)
            val date = task_jsonObject.getString("date")
            val current_date_parts = date.split("-")

            val cal = Calendar.getInstance()
            cal[Calendar.YEAR] = current_date_parts[0].toInt()
            cal[Calendar.MONTH] = current_date_parts[1].toInt() - 1
            cal[Calendar.DAY_OF_MONTH] = current_date_parts[2].toInt()
            val dateRepresentation = cal.time

            tasksMilis.add(dateRepresentation.time)
        }
       return tasksMilis
    }

    private fun addEventsToCalendar(tasks: MutableList<Long>){
        for (task in tasks) {
            val ev1 = Event(Color.GREEN, task, "")
            calendar.addEvent(ev1)
        }
    }

    private fun updateDate(current_month: Int, current_year: Int){
        activity?.runOnUiThread(Runnable {
            date.text = getMonthName(current_month)+" "+current_year
        })
    }

    private fun getMonthName(month: Int): String{
        val spanish = Locale("es", "ES")
        val months = DateFormatSymbols(spanish).months;
        return months[month].capitalize()
    }
}
