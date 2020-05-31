package cat.tfg.pama

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.APIConnection.OkHttpRequest
import cat.tfg.pama.Authentification.LogInActivity
import cat.tfg.pama.Calendar.CalendarFragment
import cat.tfg.pama.Chat.ChatFragment
import cat.tfg.pama.Expenses.ChildExpensesFragment
import cat.tfg.pama.Expenses.ChildExpensesListFragment
import cat.tfg.pama.Nannies.NanniesSearchFragment
import cat.tfg.pama.PersonalData.ChildDataFragment
import cat.tfg.pama.Tasks.TasksFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    APIResponseHandler {

    lateinit var calendarFragment: CalendarFragment
    lateinit var selectedFragment: Fragment

    val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    val URL_LOGOUT = "http://10.0.2.2:8000/api/auth/logout"

    var currentMenuItem = -1
    var okHttpRequest: OkHttpRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        okHttpRequest = OkHttpRequest.getInstance(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolBar)

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle (
            this,
            drawerLayout,
            toolBar,
            (R.string.open),
            R.string.close
        ){

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        calendarFragment = CalendarFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, calendarFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        currentMenuItem = R.id.calendar
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when(id){
            R.id.child_info -> selectedFragment = ChildDataFragment()
            R.id.expenses -> selectedFragment = ChildExpensesFragment()
            R.id.calendar -> selectedFragment = CalendarFragment()
            R.id.nannies -> selectedFragment = NanniesSearchFragment()
            R.id.tasks -> selectedFragment = TasksFragment()
            R.id.chat -> selectedFragment = ChatFragment()
            R.id.signOut -> {
                signOut()
                clearSession()
            }
        }

        if(id == currentMenuItem || id == R.id.signOut){
            drawerLayout.closeDrawer(GravityCompat.START)
            return false
        }else{
            replaceFragmentToSelectedFragment(selectedFragment)
            currentMenuItem = id
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
    }

     fun onBackStackChanged() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            val fragment = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().backStackEntryCount - 2)
            currentMenuItem = fragment.id
        }
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            onBackStackChanged()
            super.onBackPressed()
        }
    }

    private fun replaceFragmentToSelectedFragment(selectedFragment: Fragment){

        val tag_for_fragmet_id = getTagOfFragment(selectedFragment)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, selectedFragment)
            .addToBackStack(tag_for_fragmet_id)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


    private fun getTagOfFragment(selectedFragment: Fragment): String{

        when(selectedFragment){
            is CalendarFragment -> return "Calendar"
            is ChildDataFragment -> return "Child"
            is ChildExpensesListFragment -> return "Expenses"
            is TasksFragment -> return "Tasks"
            is ChatFragment -> return "Chat"
        }
        return ""
    }

    private fun signOut(){

        okHttpRequest?.GET(URL_LOGOUT, object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> changeActivityToLogIn()
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

    private fun changeActivityToLogIn(){
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent);
        finish()
    }

    private fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearSession(){
        val sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedpreferences.edit()
        editor.clear()
        editor.commit()
    }
}
