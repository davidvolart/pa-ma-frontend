package cat.tfg.pama.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cat.tfg.pama.Authentification.LogInActivity
import cat.tfg.pama.Authentification.RegisterFamilyActivity
import cat.tfg.pama.Session
import cat.tfg.pama.MainActivity
import cat.tfg.pama.R
import cat.tfg.pama.Session2

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if(isSessionStarted()){
                if(hasAFamilyRegistered()){
                    startActivity(Intent(this, MainActivity::class.java))
                }else {
                    startActivity(Intent(this, RegisterFamilyActivity::class.java))
                }
            }else{
                startActivity(Intent(this, LogInActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun isSessionStarted(): Boolean{
        //return Session.user_name != null
        val session = Session2.getInstance(this)
        return session?.getUseName() != null
    }

    private fun hasAFamilyRegistered(): Boolean{
        //return Session.family_code != null
        val session = Session2.getInstance(this)
        return session?.getFamilyCode() != null
    }
}
