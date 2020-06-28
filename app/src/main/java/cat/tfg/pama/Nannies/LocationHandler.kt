package cat.tfg.pama.Nannies

import android.Manifest
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.fragment_nannies_search.*

/*
class LocationHandler(var context: Context): ActivityCompat.OnRequestPermissionsResultCallback {

    private val locationRequestCode = 1;
    private var fusedLocationClient: FusedLocationProviderClient

    private var wayLatitude: String = ""
    private var wayLongitude: String = ""

    init{
        fusedLocationClient = LocationServices.'getFusedLocationProviderClient'(context)
    }

    fun getLocation(): String{
        getLatLong()
        return wayLatitude
    }

    private fun getLatLong(){

        fusedLocationClient.lastLocation
            .addOnSuccessListener(activity,
                OnSuccessListener<Location> { location ->
                    if (location != null) {
                        wayLatitude = location.latitude.toString()
                        wayLongitude = location.longitude.toString()
                        //latitudeText.setText(location.latitude.toString()+"-"+location.longitude.toString())
                    }
                })
    }

    private fun requestPermission() {
        requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            locationRequestCode -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLatLong()
                }
            }
        }
    }
}
*/