package com.example.csc202assignment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.csc202assignment.databinding.ImagedialogBinding
import java.io.File

class ImageDialog : DialogFragment() {
    private val args: ImageDialogArgs by navArgs()
    private lateinit var imageView: ImageView
    private lateinit var binding: ImagedialogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ImagedialogBinding.inflate(layoutInflater, container, false)
        val photoFile = args.image?.let {
            File(requireContext().applicationContext.filesDir, it)
        }
        imageView = binding.image
        if (photoFile?.exists() == true) {

            imageView.setImageBitmap(getScaledBitmap(photoFile.toString(), 1000, 1000))

        }
        return binding.root

    }
}