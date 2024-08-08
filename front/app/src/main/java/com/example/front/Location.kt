package com.example.front

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class Location(context: Context) {
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationCallback: LocationCallback
    @SuppressLint("MissingPermission")
    fun getLocation(callback: (longitude: Double, latitude: Double) -> Unit) {
        // 먼저 마지막 위치를 시도
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { currentLocation: Location? ->
                if (currentLocation != null) {
                    callback(currentLocation.longitude, currentLocation.latitude)
                } else {
                    // 현재 위치도 가져올 수 없을 때
                    callback(128.9113418, 35.0976711)
                }
            }
            .addOnFailureListener {
                callback(128.9113418, 35.0976711)
            }
    }
}