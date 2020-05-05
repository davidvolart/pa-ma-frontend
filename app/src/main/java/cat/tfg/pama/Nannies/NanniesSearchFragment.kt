package cat.tfg.pama.Nannies

import android.Manifest
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
import androidx.annotation.NonNull
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.util.Log
import androidx.annotation.Nullable


class NanniesSearchFragment : Fragment(), APIResponseHandler, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    protected var googleApiClient: GoogleApiClient? = null

    private val RequestPermissionCode = 1;
    //private val locationRequestCode = 1000
    private var wayLatitude = 0.0
    private var wayLongitude = 0.0
    private var message_location_permision_denied = "Lo sentimos, sin tu ubicación no podemos mostar las nannies más cercanas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

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

                if(fildsAreValid()){
                    if(wayLatitude != 0.0 && wayLongitude != 0.0){
                        replaceFragmentToNanniesFragment()
                    }else{
                        showMessage(message_location_permision_denied)
                        requestPermission()
                    }
                }
            }
        })
    }

    private fun replaceFragmentToNanniesFragment(){

        val end_time = nannies_search_end_time.text.toString().replace(":","")
        val arrival_time = nannies_search_start_time.text.toString().replace(":","")
        val date = nannies_search_date.text.toString().replace("/","-")
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout, NanniesFragment.newInstance(date, arrival_time, end_time, wayLatitude.toString(), wayLongitude.toString()))
            .addToBackStack("Nannies")
        transaction.commit()
    }

    private fun fildsAreValid(): Boolean{

        val end_time = nannies_search_end_time.text.toString()
        val arrival_time = nannies_search_start_time.text.toString()
        val date = nannies_search_date.text.toString()

        if(checkDatesAreNonEmpty(date,arrival_time, end_time)){

            val arrival_hour = arrival_time.substring(0,2).toInt()
            val arrival_minutes = arrival_time.substring(3).toInt()

            val end_hour = end_time.substring(0,2).toInt()
            val end_minutes = end_time.substring(3).toInt()

            if(checkTimesAreValid(arrival_hour,arrival_minutes,end_hour,end_minutes)){
                return true
            }
            return false
        }
        return false
    }

    private fun checkTimesAreValid(arrival_hour:Int,arrival_minutes:Int,end_hour: Int,end_minutes:Int): Boolean{

        if(end_hour < arrival_hour){
            showMessage("El campo hora de fin ha de ser posterior a la de inicio")
            return false
        }else if(end_hour == arrival_hour && end_minutes < arrival_minutes ){
            showMessage("El campo hora de fin ha de ser posterior a la de inicio")
            return false
        }
        return true
    }

    private fun checkDatesAreNonEmpty(date:String, arrival_time: String, end_time:String):Boolean{

        if(date.equals("")){
            showMessage("El campo fecha es obligatorio")
            return false
        }else if(arrival_time.equals("")){
            showMessage("El campo hora de inicio es obligatorio")
            return false
        }else if(end_time.equals("")){
            showMessage("El campo hora de fin es obligatorio")
            return false
        }
        return true
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
        googleApiClient!!.connect()
    }

    override fun onStop() {
        if (googleApiClient!!.isConnected()) {
            googleApiClient!!.disconnect()
        }
        super.onStop()
    }

    override fun onConnected(@Nullable bundle: Bundle?) {

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
       requestPermissions(arrayOf(ACCESS_FINE_LOCATION), RequestPermissionCode)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e("MainActivity", "Connection failed: " + connectionResult.errorCode)
    }

    override fun onConnectionSuspended(i: Int) {
        Log.e("MainActivity", "Connection suspendedd")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.i("requestCode",requestCode.toString())
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RequestPermissionCode -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLatLong()
                }
            }
        }
    }

}