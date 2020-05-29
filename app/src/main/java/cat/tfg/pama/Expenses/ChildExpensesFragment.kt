package cat.tfg.pama.Expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cat.tfg.pama.PersonalData.ChildPersonalDataFragment
import cat.tfg.pama.R
import cat.tfg.pama.Sizes.ChildSizesDataFragment
import cat.tfg.pama.Vaccines.ChildVaccinesDataFragment
import kotlinx.android.synthetic.main.fragment_child_expenses.*

class ChildExpensesFragment : Fragment() {

    var currentMenuItem = -1
    lateinit var selectedFragment: Fragment

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.child_expenses, ChildExpensesListFragment())
        transaction.commit()

        currentMenuItem = R.id.child_personal_info

        return inflater.inflate(R.layout.fragment_child_expenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        activity!!.setTitle("Gastos")

        child_expenses_navigation.setOnNavigationItemSelectedListener { item ->

            val id = item.getItemId()

            when (id) {
                R.id.child_expenses_list -> selectedFragment = ChildExpensesListFragment()
                R.id.child_expenses_graphic -> selectedFragment = ChildExpensesGraphicFragment()
            }

            if(id != currentMenuItem){
                replaceFragmentToSelectedFragment(selectedFragment)
                currentMenuItem = id
            }

            true
        }
    }

    private fun replaceFragmentToSelectedFragment(selectedFragment: Fragment){

        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(
                R.id.child_expenses,
                selectedFragment
        ).addToBackStack("Child")
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        activity!!.setTitle("Gastos")
    }
}
