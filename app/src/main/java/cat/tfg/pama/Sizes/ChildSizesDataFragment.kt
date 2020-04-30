package cat.tfg.pama.Sizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.APIConnection.OkHttpRequest
import cat.tfg.pama.R
import kotlinx.android.synthetic.main.fragment_child_sizes_data.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class ChildSizesDataFragment : Fragment(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val SUCCESSFUL_SAVE_MESSAGE = "Se ha guardado correctamente."
    val URL_GET_CHILD = "http://10.0.2.2:8000/api/child"
    val URL_SAVE_SIZE_DATA = "http://10.0.2.2:8000/api/sizedata"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_sizes_data, container, false)
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

        child_sizes_data_save.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                OkHttpRequest.POST(
                    URL_SAVE_SIZE_DATA,
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

    private fun getParameters(): HashMap<String, *> {
        val parameters = HashMap<String, Any>()

        if(child_sizes_shirt.text.toString() != ""){
            parameters.put("shirt_size", child_sizes_shirt.text.toString())
        }

        if(child_sizes_dress.text.toString() != ""){
            parameters.put("dress_size", child_sizes_dress.text.toString())
        }

        if(child_sizes_pants.text.toString() != ""){
            parameters.put("pants_size", child_sizes_pants.text.toString())
        }

        if(child_sizes_shoes.text.toString() != ""){
            parameters.put("shoes_size", child_sizes_shoes.text.toString())
        }

        if(child_sizes_height.text.toString() != ""){
            try{
                parameters.put("height", child_sizes_height.text.toString().toFloat())
            }catch (e: Exception){
                showMessage("")
            }
        }

        if(child_sizes_weight.text.toString() !=""){
            try{
                parameters.put("weight", child_sizes_weight.text.toString().toFloat())
            }catch (e: Exception){
                showMessage("")
            }
        }

        return parameters
    }

    private fun setContent(child: JSONObject){
        activity?.runOnUiThread(Runnable {

            if(child.getString("shirt_size") != "null"){
                child_sizes_shirt.setText(child.getString("shirt_size"));
            }

            if(child.getString("weight") != "null"){
                child_sizes_weight.setText(child.getString("weight"));
            }

            if(child.getString("dress_size") != "null"){
                child_sizes_dress.setText(child.getString("dress_size"));
            }

            if(child.getString("pants_size") != "null"){
                child_sizes_pants.setText(child.getString("pants_size"));
            }

            if(child.getString("height") != "null"){
                child_sizes_height.setText(child.getString("height"));
            }

            if(child.getString("shoes_size") != "null"){
                child_sizes_shoes.setText(child.getString("shoes_size"));
            }
        })
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

}