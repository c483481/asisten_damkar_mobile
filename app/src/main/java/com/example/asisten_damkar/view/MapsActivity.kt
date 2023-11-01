package com.example.asisten_damkar.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.R
import com.example.asisten_damkar.databinding.ActivityMapsBinding
import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.ResponseList
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.MapsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener


class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    lateinit var gMap: GoogleMap
    lateinit var map: FrameLayout
    lateinit var topic: String
    val zoom: Int = 15
    lateinit var model: MapsViewModel
    lateinit var loginUtils: LoginUtils

    val PERMISSION_ID = 483481

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMapsBinding>(this, R.layout.activity_maps)

        if(!intent.hasExtra("topic")) {
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finish()
        }

        model = ViewModelProvider(this)[MapsViewModel::class.java]

        loginUtils = LoginUtils(this)

        topic = intent.getStringExtra("topic")!!

        map = findViewById(R.id.maps)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        when(topic) {
            "fire" -> {
                fireFunction()
            }
            "tracker" -> {
                posTracker()
            }
        }
    }

    private fun fireFunction() {
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)
        val latLng = LatLng(lat, lng)
        var activeMarker = gMap.addMarker(MarkerOptions().position(latLng))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toFloat()))

        gMap.setOnMapClickListener { location->
            activeMarker?.remove()
            val marker = gMap.addMarker(MarkerOptions().position(location))
            activeMarker = marker
        }
    }

    private fun posTracker() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    private fun getLastLocation() {
        binding.progressBarMaps.show()
        if(checkPermission()) {
            if(isLocationEnable()) {
                fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(listener: OnTokenCanceledListener) = CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                    .addOnSuccessListener {
                        if (it == null)
                            toast("cannot access gps")
                        else {
                            val lat = it.latitude
                            val lon = it.longitude
                            val latLng = LatLng(lat, lon)
                            val data = model.getPosList(loginUtils.getAccessToken()!!, latLng)

                            onResponseListPosOnTracker(data)

                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toFloat()))
                            addPos()
                        }
                    }
            } else {
                toast("Please enable your location service")
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        } else {
            requestPermission()
        }
    }

    private fun addPos() {
        var activeMarker: Marker? = null
        gMap.setOnMapClickListener { location->
            activeMarker?.remove()
            val marker = gMap.addMarker(MarkerOptions().position(location))
            activeMarker = marker
        }
    }

    private fun checkPermission(): Boolean {
        return (
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ID)
        toast("request permission")
    }

    private fun isLocationEnable(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_ID) {
            if(grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toast("permission granted")
            }
        }
    }

    fun onResponseListPosOnTracker(data: LiveData<ResponseList<PosResponse>>) {
        data.observe(this, Observer {
            binding.progressBarMaps.hide()
            for(pos in it.items) {
                gMap.addMarker(MarkerOptions().position(LatLng(pos.location.lat, pos.location.lng)).title(pos.name));
            }
        })
    }
}