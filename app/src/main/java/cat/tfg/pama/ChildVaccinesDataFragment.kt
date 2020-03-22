package cat.tfg.pama

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_child_vaccines_data.*


data class Vaccine(var title: String, var date: String, var description: String)

/**
 * A simple [Fragment] subclass.
 */
class ChildVaccinesDataFragment : Fragment() {

    private val nicCageMovies = listOf(
        Vaccine("Vacuna 1", "02/02/2019", ""),
        Vaccine("Vacuna 2", "02/02/2019", ""),
        Vaccine("Vacuna 3", "02/02/2019", ""),
        Vaccine("Vacuna 4", "02/02/2019", ""),
        Vaccine("Vacuna 5", "02/02/2019", ""),
        Vaccine("Vacuna 6", "02/02/2019", ""),
        Vaccine("Vacuna 7", "02/02/2019", ""),
        Vaccine("Vacuna 8", "02/02/2019", "")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_child_vaccines_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ListAdapter(nicCageMovies)
        }
    }

}