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

    lateinit var childDataFragment: ChildDataFragment
    lateinit var calendarFragment: CalendarFragment
    lateinit var childExpensesFragment: ChildExpensesFragment;
    lateinit var tasksFragment: TasksFragment;
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
            .addToBackStack(null)
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

    override fun onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }

    }

    private fun replaceFragmentToSelectedFragment(selectedFragment: Fragment){

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, selectedFragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
