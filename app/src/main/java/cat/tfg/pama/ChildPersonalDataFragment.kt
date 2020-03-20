package cat.tfg.pama

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_child_personal_data.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChildPersonalDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_child_personal_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        birthdate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val datepicker = DatePickerDialog(
                    activity!!,
                    { view, year, monthOfYear, dayOfMonth ->
                        val selectedDate = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                        (birthdate as EditText).setText(selectedDate)
                    },
                    year, month, day)

                datepicker.datePicker.maxDate = c.getTimeInMillis()
                datepicker.show()
            }
        })

    }

}