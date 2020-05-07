package cat.tfg.pama.Nannies

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import cat.tfg.pama.APIConnection.APIResponseHandler
import cat.tfg.pama.R
import kotlinx.android.synthetic.main.fragment_nannies_search.*
import java.util.*
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.common.ConnectionResult
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.util.Log
import androidx.annotation.Nullable
import cat.tfg.pama.APIConnection.OkHttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class NanniesSearchFragment : Fragment(), APIResponseHandler{

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val URL_NANNIES = "http://10.0.2.2:8000/api/nannies"
    private val STANDARD_MESSAGE_ERROR = "Ha ocurrido un error. Vuelve a interarlo."
    private val locationRequestCode = 1;
    private var wayLatitude = 0.0
    private var wayLongitude = 0.0
    private var message_location_permision_denied = "Lo sentimos, sin tu ubicación no podemos mostar las nannies más cercanas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nannies_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity!!.setTitle("Nannies")
        nannies_search_title.setText("Filtro")

        nannies_search_date.setOnClickListener(object : View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            override fun onClick(v: View) {

                val datepicker = DatePickerDialog(
                    activity!!,
                    { view, year, monthOfYear, dayOfMonth ->
                        val selectedDate =
                            dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                        (nannies_search_date as EditText).setText(selectedDate)
                    },
                    year, month, day
                )

                datepicker.datePicker.minDate = System.currentTimeMillis() - 1000
                datepicker.show()
            }
        })

        nannies_search_start_time.setOnClickListener(View.OnClickListener {

            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                (nannies_search_start_time as EditText).setText(SimpleDateFormat("HH:mm").format(cal.time))
            }

            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        })

        nannies_search_end_time.setOnClickListener(View.OnClickListener {

            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                (nannies_search_end_time as EditText).setText(SimpleDateFormat("HH:mm").format(cal.time))
            }

            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        })

        nannies_search_save.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                if(wayLatitude != 0.0 && wayLongitude != 0.0){
                    getNannies()
                }else{
                    showMessage(message_location_permision_denied)
                    requestPermission()
                }
            }
        })
    }

    private fun getNannies(){

        OkHttpRequest.POST(URL_NANNIES, getParameters(), object : Callback {
            override fun onResponse(call: Call?, response: Response) {
                when (response.code()) {
                    200 -> {
                        replaceFragmentToNanniesFragment(response.body()?.string().toString())
                    }
                    500 -> {
                        showMessage(STANDARD_MESSAGE_ERROR)
                    }else -> {
                        val message = getResponseMessage(response);
                        if (message != null) {
                            showMessage(message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                showMessage(STANDARD_MESSAGE_ERROR);
            }
        })
    }

    private fun getParameters(): HashMap<String, String> {

        val parameters = HashMap<String, String>()
        parameters.put("day", nannies_search_date.text.toString().replace('/','-'))
        parameters.put("start", nannies_search_start_time.text.toString())
        parameters.put("end", nannies_search_end_time.text.toString())
        parameters.put("lat", wayLatitude.toString())
        parameters.put("long", wayLongitude.toString())

        return parameters
    }

    private fun replaceFragmentToNanniesFragment(nannies: String){

        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout, NanniesFragment.newInstance(nannies))
            .addToBackStack("Nannies")
        transaction.commit()
    }

    private fun showMessage(message: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })
    }

    override fun onResume() {
        super.onResume()
        activity!!.setTitle("Nannies")
    }

    override fun onStart() {
        super.onStart()

        if (ActivityCompat.checkSelfPermission(context!!, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        } else {
            getLatLong()
        }
    }

    private fun getLatLong(){

        fusedLocationClient.lastLocation
            .addOnSuccessListener(activity!!,
                OnSuccessListener<Location> { location ->
                    if (location != null) {
                        wayLatitude = location.latitude
                        wayLongitude = location.longitude
                        latitudeText.setText(location.latitude.toString()+"-"+location.longitude.toString())
                    }
                })
    }

    private fun requestPermission() {
       requestPermissions(arrayOf(ACCESS_FINE_LOCATION), locationRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            locationRequestCode -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLatLong()
                }
            }
        }
    }
}