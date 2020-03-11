package cat.tfg.pama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.*
import java.io.IOException

class SignUpActivity : AppCompatActivity(), Helper {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL = "http://10.0.2.2:8000/api/auth/signup"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        singUp_save.setOnClickListener {

            val client = OkHttpClient()
            val request = OkHttpRequest(client)

            request.POST(URL, getParameters(), object : Callback {
                override fun onResponse(call: Call?, response: Response) {
                    when (response.code()) {
                        201 -> {
                            showMessage(getResponseMessage(response))
                            changeActivityToLogIn()
                        }
                        500 -> showMessage(STANDARD_MESSAGE_ERROR)
                        else -> showMessage(getResponseMessage(response))
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    showMessage(STANDARD_MESSAGE_ERROR);
                }
            })
        }
    }

    private fun getParameters(): HashMap<String, String> {
        val parameters = HashMap<String, String>()
        parameters.put("name", singUp_name.text.toString())
        parameters.put("password", singUp_password.text.toString())
        parameters.put("password_confirmation", singUp_rememberPassword.text.toString())
        parameters.put("email", singUp_email.text.toString())
        parameters.put("partner_email", "davidvolart1997@gmail.com")
        return parameters
    }

    private fun changeActivityToLogIn() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent);
    }

    private fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}

