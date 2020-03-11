package cat.tfg.pama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_log_in.*
import okhttp3.*
import java.io.IOException

class LogInActivity : AppCompatActivity(), Helper {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL = "http://10.0.2.2:8000/api/auth/login"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        logIn_singUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent);
        }

        logIn_signIn.setOnClickListener {

            val client = OkHttpClient()
            val request = OkHttpRequest(client)

            request.POST(URL, getParameters(), object : Callback {
                override fun onResponse(call: Call?, response: Response) {
                    when (response.code()) {
                        200 -> changeActivityToCalendar()
                        500 -> showMessage(STANDARD_MESSAGE_ERROR)
                        else -> showMessage(getResponseMessage(response))
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

    private fun getParameters(): HashMap<String, String> {
        val parameters = HashMap<String, String>()
        parameters.put("email", logIn_email.text.toString())
        parameters.put("password", logIn_password.text.toString())
        parameters.put("remember_me", true.toString())
        return parameters
    }

    private fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeActivityToCalendar() {
        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent);
    }
}
