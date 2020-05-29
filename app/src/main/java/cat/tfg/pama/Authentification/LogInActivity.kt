package cat.tfg.pama.Authentification

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cat.tfg.pama.*
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.APIConnection.OkHttpRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_log_in.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LogInActivity : AppCompatActivity(), APIResponseHandler {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_LOGIN = "http://10.0.2.2:8000/api/auth/login"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        logIn_singUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent);
            finish()
        }

        logIn_signIn.setOnClickListener {

            OkHttpRequest.POST(URL_LOGIN, getParameters(), object : Callback {
                override fun onResponse(call: Call?, response: Response) {
                    when (response.code()) {
                        200 -> {
                            val login_response =
                                    getResponseAccessToken(response)
                            saveCurrentUser(login_response)
                            //saveAcccesToken(OkHttpRequest, login_response.getString("access_token"))
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

        CurrentUser.user_name = login_response.getString("user_name")
        CurrentUser.user_email = login_response.getString("user_email")
        CurrentUser.family_code = login_response.getString("family_code")
        CurrentUser.access_token = login_response.getString("token_type")+" "+login_response.getString("access_token")

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

    /*
    private fun saveAcccesToken(request: OkHttpRequest, access_token: String) {
        OkHttpRequest.setAccesToken(access_token);
    }
     */

    private fun checkUserHasAFamilyRegistered() {
        if (CurrentUser.family_code != "null") {
            changeActivityToMainActivity()
        } else {
            changeActivityToRegisterFamily()
        }
/*
        OkHttpRequest.GET(URL_CHILD, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        if (getFamilyCode(response) != null) {
                            changeActivityToMainActivity()
                        } else {
                            changeActivityToRegisterFamily()
                        }
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
         */
    }

}


