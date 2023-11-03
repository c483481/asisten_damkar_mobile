package com.example.asisten_damkar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.asisten_damkar.R
import com.example.asisten_damkar.databinding.ActivitySearchKebakaranBinding
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class SearchKebakaranActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchKebakaranBinding
    lateinit var autoCompleteFragment: AutocompleteSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivitySearchKebakaranBinding>(this, R.layout.activity_search_kebakaran)

        Places.initialize(applicationContext, getString(R.string.api_key))
        autoCompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autoCompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))
        autoCompleteFragment.setOnPlaceSelectedListener(object: PlaceSelectionListener{
            override fun onError(p0: Status) {
                var str = ""

                if(p0.statusMessage.isNullOrEmpty()) {
                    str = "Isi tempat kebakaran"
                } else {
                    str = "Error : " + p0.statusMessage
                }

                Toast.makeText(baseContext, str, Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(p0: Place) {
                val i = Intent(baseContext, MapsActivity::class.java)
                val latLng = p0.latLng

                i.putExtra("lat", latLng.latitude)
                i.putExtra("lng", latLng.longitude)
                i.putExtra("topic", "fire")

                startActivity(i)
            }
        })

        binding.pilihSendiri.setOnClickListener{
            val i = Intent(baseContext, MapsActivity::class.java)
            i.putExtra("topic", "fireX")

            startActivity(i)
        }
    }
}