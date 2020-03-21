package cat.tfg.pama

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_child_data.*

/**
 * A simple [Fragment] subclass.
 */
class ChildDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.content, ChildPersonalDataFragment())
        transaction.commit()

        return inflater.inflate(R.layout.fragment_child_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.setTitle("Info del niño")

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.getItemId()) {
                R.id.child_personal_info -> {
                    val transaction = fragmentManager!!.beginTransaction()
                    transaction.replace(R.id.content, ChildPersonalDataFragment())
                    transaction.commit()
                }
                R.id.child_sizes -> {
                    val transaction = fragmentManager!!.beginTransaction()
                    transaction.replace(R.id.content, ChildSizesDataFragment())
                    transaction.commit()
                }
                R.id.child_vaccines -> {
                    val transaction = fragmentManager!!.beginTransaction()
                    transaction.replace(R.id.content, ChildPersonalDataFragment())
                    transaction.commit()
                }
            }
            true
        }
    }

}
