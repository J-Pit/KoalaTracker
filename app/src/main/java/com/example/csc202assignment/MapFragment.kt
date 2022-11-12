package com.example.csc202assignment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.csc202assignment.databinding.ImagedialogBinding
import com.example.csc202assignment.databinding.MapFragmentBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: MapFragmentBinding
    private var mMap: GoogleMap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapFragmentBinding.inflate(layoutInflater, container, false)
        val mapFragment = binding.googleMap
            as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return binding.root
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }
}