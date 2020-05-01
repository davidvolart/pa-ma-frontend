package cat.tfg.pama.PersonalData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.fragment.app.Fragment
import cat.tfg.pama.Vaccines.ChildVaccinesDataFragment
import cat.tfg.pama.R
import cat.tfg.pama.Sizes.ChildSizesDataFragment
import kotlinx.android.synthetic.main.fragment_child_data.*

/**
 * A simple [Fragment] subclass.
 */
class ChildDataFragment : Fragment() {

    var currentMenuItem = -1
    lateinit var selectedFragment: Fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.content, ChildPersonalDataFragment())
        transaction.commit()

        currentMenuItem = R.id.child_personal_info

        return inflater.inflate(R.layout.fragment_child_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        activity!!.setTitle("Info del niño")

        navigation.setOnNavigationItemSelectedListener { item ->

            val id = item.getItemId()

            when (id) {
                R.id.child_personal_info -> selectedFragment = ChildPersonalDataFragment()
                R.id.child_sizes -> selectedFragment = ChildSizesDataFragment()
                R.id.child_vaccines ->  selectedFragment = ChildVaccinesDataFragment()
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
            R.id.content,
            selectedFragment
        ).addToBackStack("Child")
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        activity!!.setTitle("Info del niño")
    }
}
