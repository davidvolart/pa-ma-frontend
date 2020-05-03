package cat.tfg.pama.Nannies

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
import kotlinx.android.synthetic.main.fragment_child_vaccines_data.*
import kotlinx.android.synthetic.main.fragment_nannies.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

data class Nannie(var id: String, var name: String, var age: Int, var stars: Int)

class NanniesFragment : Fragment(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_NANNIES = "https://nannyfy.com/api/search"

    private val nannies_list: MutableList<Nannie> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nannies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity!!.setTitle("Nannies")

        OkHttpRequest.POSTNannyfy(URL_NANNIES, getParameters(), object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        addNanniesToList(response);
                        addListAdapter()
                    }
                    500 -> {
                        showMessage(STANDARD_MESSAGE_ERROR)
                    }
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
        parameters.put("day", "17-05-2020")
        parameters.put("start", "1700")
        parameters.put("end", "1900")
        parameters.put("postal", "")
        parameters.put("lat", "41.403706")
        parameters.put("long", "2.173504")
        parameters.put("languages", "")
        parameters.put("skills", "")
        parameters.put("can_kid", "1")
        parameters.put("can_teen", "1")
        parameters.put("can_baby", "1")
        parameters.put("distance", "20")

        return parameters
    }

    private fun addNanniesToList(nannies: Response){

        val nannies_json = nannies.body()?.string()?.toString();
        val jsonArray = JSONArray(nannies_json)

        for (i in 0 until jsonArray.length()) {
            var nannie_jsonObject = jsonArray.getJSONObject(i)
            var nannie = Nannie(
                nannie_jsonObject.getString("id"),
                nannie_jsonObject.getString("name"),
                nannie_jsonObject.getInt("age_diggest"),
                nannie_jsonObject.getInt("stars")
            )
            nannies_list.add(nannie)
        }
    }

    private fun addListAdapter(){
        activity?.runOnUiThread(Runnable {
            list_nannies.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ListAdapter(
                    nannies_list,
                    { expenditure_item: Any -> expenditureItemClicked(expenditure_item) })
            }
        })
    }

    private fun expenditureItemClicked(item : Any) {
        //AGAFAR EL SLUG DEL ITEM I ENVIARLO A LA URL DE NANNYFY
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onResume() {
        super.onResume()
        activity!!.setTitle("Nannies")
    }

    override fun onStop() {
        nannies_list.clear()
        super.onStop()
    }
}