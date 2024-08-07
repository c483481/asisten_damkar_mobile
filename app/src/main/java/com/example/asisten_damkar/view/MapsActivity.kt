package com.example.asisten_damkar.view

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.asisten_damkar.R
import com.example.asisten_damkar.databinding.ActivityMapsBinding
import com.example.asisten_damkar.listener.MapsUpdateSocketListener
import com.example.asisten_damkar.response.PosResponse
import com.example.asisten_damkar.response.ResponseList
import com.example.asisten_damkar.utils.LoginUtils
import com.example.asisten_damkar.utils.SocketMapsUtils
import com.example.asisten_damkar.utils.hide
import com.example.asisten_damkar.utils.show
import com.example.asisten_damkar.utils.toast
import com.example.asisten_damkar.view_model.MapsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var gMap: GoogleMap
    lateinit var map: FrameLayout
    lateinit var topic: String
    val zoom: Int = 15
    lateinit var model: MapsViewModel
    lateinit var loginUtils: LoginUtils

    val PERMISSION_ID = 483481

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var socketMapsUtils: SocketMapsUtils

    lateinit var binding: ActivityMapsBinding

    private var globalMarker: Marker? = null

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
            "fireX" -> {
                fireX()
            }
            "fireLocation" -> {
                socketMapsUtils = SocketMapsUtils()
                fireLocation()
            }
            "supervise" -> {
                socketMapsUtils = SocketMapsUtils()
                val fireLocation = intent.getStringExtra("fireLocationXid")!!
                socketMapsUtils.connect(fireLocation)
                supervise()
            }
        }
    }

    private fun supervise() {
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)
        val latLng = LatLng(lat, lng)
        var activeMarker: Marker? = null
        val listener: MapsUpdateSocketListener = object: MapsUpdateSocketListener {
            override fun onConnected(lat: Double, lng: Double) {
                runOnUiThread {
                    val location = LatLng(lat, lng)
                    activeMarker?.remove()
                    val marker = gMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pos)))
                    activeMarker = marker
                }
            }
        }

        socketMapsUtils.socketEventLimit(listener)

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toFloat()))

        gMap.addMarker(MarkerOptions()
            .position(latLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fire)))
    }

    private fun fireLocation() {
        binding.timerContainer.visibility = View.VISIBLE
        val createdAt = intent.getIntExtra("createdAt",0)
        val now = System.currentTimeMillis() / 1000

        val differenceInSeconds = now - createdAt

        val minutes = differenceInSeconds / 60

        if(minutes > 15) {
            binding.timer.text = "Late"
        } else {
            val totalTimeInMillis: Long = (60000 * 15) - (differenceInSeconds * 1000) // Total waktu dalam milidetik (60 detik)

            val countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    val second = secondsRemaining % 60
                    val minutes = secondsRemaining / 60
                    val combineString = "$minutes:$second"
                    binding.timer.text = combineString
                }

                override fun onFinish() {
                    binding.timer.text = "Late"
                }
            }

            // Mulai timer
            countDownTimer.start()
        }

        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)
        val latLng = LatLng(lat, lng)

        val mDialog = Dialog(this)

        binding.buttonMaps.text = "Selesai"

        binding.buttonMaps.setOnClickListener {
            val fireLocationXid = intent.getStringExtra("fireLocationXid")!!
            val updated = model.updateFireLocation(token = loginUtils.getAccessToken()!!, xid = fireLocationXid)
            updated.observe(this, Observer {
                if(it) {
                    mDialog.setContentView(R.layout.pop_up_maps)
                    mDialog.findViewById<Button>(R.id.agree).setOnClickListener {

                        val i = Intent(this, HomePemadamActivity::class.java)
                        startActivity(i)
                    }
                    mDialog.show()
                    return@Observer
                }
                toast("Failed to clear activity, please login again")
            })

        }

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toFloat()))

        gMap.addMarker(MarkerOptions()
            .position(latLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fire)))
        var activeMarker: Marker? = null
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    handleLocationUpdate(location)
                }
            }
        }

        val fireLocationXid = intent.getStringExtra("fireLocationXid")!!

        socketMapsUtils.connect(fireLocationXid)

        updateLocation()
    }

    private fun updateLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
        locationRequest.smallestDisplacement = 170f
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if(checkPermission()) {
            if(isLocationEnable()) {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            handleLocationUpdate(location)
                        }
                    }
            } else {
                toast("Please enable your location service")
            }
        } else {
            requestPermission()
        }


    }

    private fun fireFunction() {
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)
        val latLng = LatLng(lat, lng)

        val data = model.getPosList(loginUtils.getAccessToken()!!, latLng)

        onResponseListPosOnTracker(data)

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toFloat()))
        addFireFunction(latLng)
    }

    private fun fireX() {
        val latLng = LatLng(1.4748, 124.8421)
        val data = model.getPosList(loginUtils.getAccessToken()!!, latLng)

        onResponseListPosOnTracker(data)

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toFloat()))
        addFireFunction(null)
    }

    private fun posTracker() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding.buttonMaps.text = "tambah"
        getLastLocation()
        gMap.setOnMarkerClickListener { marker ->
            val xid = marker.title

            val i = Intent(this, PosDetailActivity::class.java)
            i.putExtra("posXid", xid)
            startActivity(i)
            finish()

            true
        }
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
                            addPosFunction()
                        }
                    }
            } else {
                toast("Please enable your location service")
            }
        } else {
            requestPermission()
        }
    }

    private fun addFireFunction(loc: LatLng?) {
        var activeMarker: Marker? = if (loc != null) gMap.addMarker(MarkerOptions()
            .position(loc)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fire))) else null
        var loc: LatLng? = loc
        binding.buttonMaps.setOnClickListener {
            if(loc == null) {
                toast("add marker before add fire location")
                return@setOnClickListener
            }
            val i = Intent(this, PosDetailFireActivity::class.java)
            i.putExtra("lat", loc!!.latitude)
            i.putExtra("lng", loc!!.longitude)
            startActivity(i)
        }

        gMap.setOnMapClickListener { location->
            loc = location
            activeMarker?.remove()
            val marker = gMap.addMarker(MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_fire)))
            activeMarker = marker
        }
    }

    private fun addPosFunction() {
        var activeMarker: Marker? = null
        var loc: LatLng? = null
        binding.buttonMaps.setOnClickListener {
            if(loc == null) {
                toast("add marker before add pos")
                return@setOnClickListener
            }
            val i = Intent(this, AddPosActivity::class.java)
            i.putExtra("lat", loc!!.latitude)
            i.putExtra("lng", loc!!.longitude)
            startActivity(i)
        }

        gMap.setOnMapClickListener { location->
            loc = location
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

    private fun onResponseListPosOnTracker(data: LiveData<ResponseList<PosResponse>>) {
        data.observe(this, Observer {
            binding.progressBarMaps.hide()
            for(pos in it.items) {
                gMap.addMarker(MarkerOptions()
                    .position(LatLng(pos.location.lat, pos.location.lng))
                    .title(pos.xid)
                    .snippet(pos.name)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pos))
                );
            }
        })
    }

    private fun handleLocationUpdate(location: Location) {
        val fireLocationXid = intent.getStringExtra("fireLocationXid")!!
        socketMapsUtils.updatePemadamLocation(location.latitude, location.longitude, fireLocationXid)

        globalMarker?.remove()
        val marker = gMap.addMarker(MarkerOptions()
            .position(LatLng(location.latitude, location.longitude))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_truck_map)))
        globalMarker = marker
    }
}