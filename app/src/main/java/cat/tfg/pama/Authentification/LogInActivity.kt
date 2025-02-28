package cat.tfg.pama.Authentification

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.APIConnection.OkHttpRequest
import cat.tfg.pama.Session.Session
import cat.tfg.pama.MainActivity
import cat.tfg.pama.R
import kotlinx.android.synthetic.main.activity_log_in.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LogInActivity : AppCompatActivity(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_LOGIN = "http://10.0.2.2:8000/api/auth/login"
    var okHttpRequest: OkHttpRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        okHttpRequest = OkHttpRequest.getInstance(this)

        logIn_singUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent);
            finish()
        }

        logIn_signIn.setOnClickListener {

            okHttpRequest?.POST(URL_LOGIN, getParameters(), object : Callback {
                override fun onResponse(call: Call?, response: Response) {
                    when (response.code()) {
                        200 -> {
                            val login_response = getResponseAccessToken(response)
                            saveCurrentUser(login_response)
                            checkUserHasAFamilyRegistered()
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

    private fun saveCurrentUser(login_response: JSONObject) {
        val session = Session.getInstance(this)
        session?.setUseName(login_response.getString("user_name"))
        session?.setUserEmail(login_response.getString("user_email"))
        session?.setFamilyCode(login_response.getString("family_code"))
        session?.setAccessToken(login_response.getString("token_type")+" "+login_response.getString("access_token"))
        session?.setCalendarPermissionFirstTime(true)
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

    private fun changeActivityToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
        finish()
    }

    private fun changeActivityToRegisterFamily() {
        val intent = Intent(this, RegisterFamilyActivity::class.java)
        startActivity(intent);
    }

    private fun checkUserHasAFamilyRegistered() {
        if (Session.getInstance(this)?.getFamilyCode() != null) {
            changeActivityToMainActivity()
        } else {
            changeActivityToRegisterFamily()
        }
    }

}
