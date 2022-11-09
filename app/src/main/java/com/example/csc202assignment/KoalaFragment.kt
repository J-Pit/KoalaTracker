package com.example.csc202assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.csc202assignment.databinding.KoalaFragmentBinding
import java.util.Date

class KoalaFragment : Fragment(){


    private lateinit var koala : Koala
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        koala = Koala(324234423, "Koala", "Date()", "Australia")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = KoalaFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}