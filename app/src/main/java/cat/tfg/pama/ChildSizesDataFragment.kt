package cat.tfg.pama

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_child_sizes_data.*

/**
 * A simple [Fragment] subclass.
 */
class ChildSizesDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_child_sizes_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
/*
        child_sizes_data_save.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                //Make a post request to backend to save data

            }
        })
 */
    }

}