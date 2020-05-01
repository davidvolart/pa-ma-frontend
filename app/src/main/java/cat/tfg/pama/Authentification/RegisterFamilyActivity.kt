package cat.tfg.pama.Authentification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.MainActivity
import cat.tfg.pama.APIConnection.OkHttpRequest
import cat.tfg.pama.R
import kotlinx.android.synthetic.main.activity_register_family.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.HashMap

class RegisterFamilyActivity : AppCompatActivity(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val SUCCESS_MESSAGE = "Familia creada. Se ha mandado un correo a tu (ex)pareja."
    val URL_REGISTER_FAMILY = "http://10.0.2.2:8000/api/family"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_family)

        family_create.setOnClickListener {

            OkHttpRequest.POST(
                URL_REGISTER_FAMILY,
                getParameters(),
                object : Callback {
                    override fun onResponse(call: Call?, response: Response) {
                        when (response.code()) {
                            200 -> {
                                showMessage(SUCCESS_MESSAGE)
                                changeActivityToMainActivity()
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
                        runOnUiThread {
                            showMessage(STANDARD_MESSAGE_ERROR);
                        }
                    }
                })
        }
    }

    private fun changeActivityToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
        finish()
    }

    private fun getParameters(): HashMap<String, *>{
        val parameters = HashMap<String, Any>()

        if(family_child_name.text.toString() != ""){
            parameters.put("child_name", family_child_name.text.toString())
        }

        if(family_partner_email.text.toString() != ""){
            parameters.put("email", family_partner_email.text.toString())
        }

        return parameters;
    }

    private fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}
