package cat.tfg.pama.Calendar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.tfg.pama.R

/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

/*
    override fun onStart() {
        redirectToGoogleCalendar()
        super.onStart()
    }

    private fun redirectToGoogleCalendar(){
        val uri = Uri.parse("https://calendar.google.com/calendar/r")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
*/
    override fun onResume() {
        super.onResume()
        activity!!.setTitle("Calendario")
    }
}
