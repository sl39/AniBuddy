package com.example.front

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class Permission(private val context: Context){
    private val REQUEST_LOCATION = 1

    @RequiresApi(Build.VERSION_CODES.Q)
    private val permissionsLocationUpApi29Impl = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    /** 위치 권한 SDK 버전 29 이하**/
    @TargetApi(Build.VERSION_CODES.P)
    private val permissionsLocationDownApi29Impl = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun requestLocation() {
        if (Build.VERSION.SDK_INT >= 29) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationUpApi29Impl[0]
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationUpApi29Impl[1]
                ) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationUpApi29Impl[2]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissionsLocationUpApi29Impl,
                    REQUEST_LOCATION
                )
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationDownApi29Impl[0]
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    context,
                    permissionsLocationDownApi29Impl[1]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissionsLocationDownApi29Impl,
                    REQUEST_LOCATION
                )
            }
        }
    }


}