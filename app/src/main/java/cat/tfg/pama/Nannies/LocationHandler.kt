package cat.tfg.pama.Nannies

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.fragment_nannies_search.*

/*
class LocationHandler(context: Context): GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


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

        if (ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
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
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationRequestCode)
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
            locationRequestCode -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLatLong()
                }
            }
        }
    }
}
*/