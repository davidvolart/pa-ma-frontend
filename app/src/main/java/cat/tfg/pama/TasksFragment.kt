package cat.tfg.pama

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tasks.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException




data class Task(var id: Int, var title: String, var date: String, var description: String, var assigned_to: String)

/**
 * A simple [Fragment] subclass.
 */
class TasksFragment : Fragment(),Helper {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_TASKS = "http://10.0.2.2:8000/api/tasks"

    private val tasks_list: MutableList<Task> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity!!.setTitle("Tareas")

        OkHttpRequest.GET(URL_TASKS, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        addTasksToList(getTasks(response));
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

        tasks_add.setOnClickListener {
            changeFragmentToAddTaskFragment();
        }
    }

    private fun changeFragmentToAddTaskFragment() {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout, AddTaskFragment())
        transaction.commit()
    }

    private fun addListAdapter(){
        activity?.runOnUiThread(Runnable {
            list_tasks_recycler_view.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ListAdapter(tasks_list, { task_item : Any -> taskItemClicked(task_item) })
            }
        })
    }

    private fun taskItemClicked(item : Any) {
        var task_item = item as Task;
        changeFragmentToTaskDetailFragment(task_item);
    }

    private fun changeFragmentToTaskDetailFragment(task_item: Task) {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout, TaskDetailsFragment.newInstance(task_item.id, task_item.title, task_item.date, task_item.description, task_item.assigned_to))
        transaction.commit()
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun addTasksToList(tasks: JSONArray){

        for (i in 0 until tasks.length()) {
            var task_jsonObject = tasks.getJSONObject(i)
            var task = Task(task_jsonObject.getInt("id"),task_jsonObject.getString("name"), task_jsonObject.getString("date"),task_jsonObject.getString("description"),task_jsonObject.getString("user_email"))
            tasks_list.add(task)
        }

        tasks_list.sortByDescending { it.date }
    }

}
