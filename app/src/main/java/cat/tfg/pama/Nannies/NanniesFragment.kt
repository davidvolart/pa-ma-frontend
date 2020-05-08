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
import kotlinx.android.synthetic.main.fragment_nannies.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import android.content.Intent
import android.net.Uri

data class Nannie(var id: String, var name: String, var age: Int, var stars: Int, var slug: String, var image: String)

class NanniesFragment : Fragment(), APIResponseHandler {

    private val nannies_list: MutableList<Nannie> = mutableListOf()
    private val NO_NANNIES_FOUND_MESSAGE = "No hemos encontrado nannies en un radio de 20km."

    companion object {
        fun newInstance(nannies: String): NanniesFragment {
            val fragment = NanniesFragment()
            val args = Bundle()
            args.putString("nannies", nannies)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nannies, container, false)
    }

    override fun onStart() {
        super.onStart()

        addNanniesToList(arguments!!.getString("nannies",""));
        addListAdapter()
    }

    private fun addNanniesToList(nannies_json: String){

        val jsonArray = JSONArray(nannies_json)

        for (i in 0 until jsonArray.length()) {
            val nannie_jsonObject = jsonArray.getJSONObject(i)
            val nannie = Nannie(
                nannie_jsonObject.getString("id"),
                nannie_jsonObject.getString("name"),
                nannie_jsonObject.getInt("age_diggest"),
                nannie_jsonObject.getInt("stars"),
                nannie_jsonObject.getString("slug"),
                nannie_jsonObject.getString("image")
            )
            nannies_list.add(nannie)
        }

        if(jsonArray.length() == 0){
            showMessage(NO_NANNIES_FOUND_MESSAGE);
        }
    }

    private fun addListAdapter(){
        activity?.runOnUiThread(Runnable {
            list_nannies.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ListAdapter(
                    nannies_list,
                    { nannie_item: Any -> nannieItemClicked(nannie_item) })
            }
        })
    }

    private fun nannieItemClicked(item : Any) {
        val nannie_item = item as Nannie;
        redirectToNannyfy(nannie_item.slug)
    }

    private fun redirectToNannyfy(slug: String){
        val uri = Uri.parse("https://nannyfy.com/v2/family/home#/search/details-nanny/"+slug)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
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
}