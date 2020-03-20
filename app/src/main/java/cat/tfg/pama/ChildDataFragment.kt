package cat.tfg.pama

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.fragment.app.Fragment


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
        super.onViewCreated(view, savedInstanceState);
        activity!!.setTitle("Info del niño");
    }
}
