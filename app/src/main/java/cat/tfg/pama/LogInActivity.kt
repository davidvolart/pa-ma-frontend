package cat.tfg.pama

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_log_in.*
import okhttp3.*
import java.io.IOException
import kotlin.reflect.KClass

class LogInActivity : AppCompatActivity(), Helper {

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_LOGIN = "http://10.0.2.2:8000/api/auth/login"
    val URL_CHILD = "http://10.0.2.2:8000/api/child"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        logIn_singUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent);
        }

        logIn_signIn.setOnClickListener {

            OkHttpRequest.POST(URL_LOGIN, getParameters(), object : Callback {
                override fun onResponse(call: Call?, response: Response) {
                    when (response.code()) {
                        200 -> {
                            saveAcccesToken(OkHttpRequest, getResponseAccessToken(response))
                            //Check if this user has a family yet
                            checkUserHasAChildRegistered()
                            //If it has changeActivityToMainActivity()
                            //Else changeActivityToRegisterFamily()
                            //changeActivityToMainActivity()
                        }
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

    private fun changeActivityToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
    }

    private fun changeActivityToRegisterFamily() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
    }

    private fun saveAcccesToken(request: OkHttpRequest, access_token: String){
        request.setAccesToken(access_token);
    }


    private fun checkUserHasAChildRegistered(){
        OkHttpRequest.GET(URL_CHILD, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        if(hasChild(response) != null) {
                            changeActivityToMainActivity()
                        } else {
                            changeActivityToRegisterFamily()
                        }
                    }
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
