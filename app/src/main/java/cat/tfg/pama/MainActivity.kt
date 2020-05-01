package cat.tfg.pama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import cat.tfg.pama.Calendar.CalendarFragment
import cat.tfg.pama.Expenses.ChildExpensesFragment
import cat.tfg.pama.PersonalData.ChildDataFragment
import cat.tfg.pama.Tasks.TasksFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var calendarFragment: CalendarFragment
    lateinit var selectedFragment: Fragment
    var currentMenuItem = -1

    override fun onCreate(savedInstanceState: Bundle?) {

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
            R.id.tasks -> selectedFragment = TasksFragment()
        }

        if(id == currentMenuItem){
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
            is ChildExpensesFragment -> return "Expenses"
            is TasksFragment -> return "Tasks"
        }
        return ""
    }
}
