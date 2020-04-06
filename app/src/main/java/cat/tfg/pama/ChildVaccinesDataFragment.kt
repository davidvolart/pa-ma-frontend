package cat.tfg.pama

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_child_vaccines_data.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import androidx.recyclerview.widget.DividerItemDecoration


data class Vaccine(var title: String, var date: String, var description: String)

/**
 * A simple [Fragment] subclass.
 */
class ChildVaccinesDataFragment : Fragment(), Helper {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_GET_CHILD = "http://10.0.2.2:8000/api/vaccines"

    private val vaccines_list: MutableList<Vaccine> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_vaccines_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)

        OkHttpRequest.GET(URL_GET_CHILD, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        addVaccinesToList(getVaccines(response));
                        addItemsDivider()
                        addListAdapter()
                    }
                    500 -> showMessage(STANDARD_MESSAGE_ERROR)
                    else -> showMessage(getResponseMessage(response))
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                showMessage(STANDARD_MESSAGE_ERROR);
            }
        })

        child_vaccines_data_save.setOnClickListener {
            changeFragmentToAddVaccineFragment();
        }
    }

    private fun changeFragmentToAddVaccineFragment() {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.content, AddVaccineFragment())
        transaction.commit()
    }

    private fun addItemsDivider(){
        activity?.runOnUiThread(Runnable {
            val itemDecor = DividerItemDecoration(context, HORIZONTAL)
            list_recycler_view.addItemDecoration(itemDecor)
        })
    }

    private fun addListAdapter(){
        activity?.runOnUiThread(Runnable {
            list_recycler_view.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ListAdapter(vaccines_list)
            }
        })
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun addVaccinesToList(vaccines: JSONArray){
        for (i in 0 until vaccines.length()) {
            var vaccine_jsonObject = vaccines.getJSONObject(i)
            var vaccine = Vaccine(vaccine_jsonObject.getString("name"),vaccine_jsonObject.getString("date"),"")
            vaccines_list.add(vaccine)
        }
    }
}