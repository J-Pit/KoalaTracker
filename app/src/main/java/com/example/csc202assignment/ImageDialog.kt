package com.example.csc202assignment

import android.app.Dialog
import android.os.Bundle
import androidx.core.view.doOnLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.csc202assignment.databinding.ImagedialogBinding
import java.io.File

class ImageDialog : DialogFragment() {
    private val args: ImageDialogArgs by navArgs()
    private lateinit var binding: ImagedialogBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ImagedialogBinding.inflate(layoutInflater)
        val photoFile = args.image?.let {
            File(requireContext().applicationContext.filesDir, it)
        }
        println(photoFile)
        binding.image.doOnLayout { measuredView ->
            val scaledBitmap = photoFile?.let {
                getScaledBitmap(
                    photoFile.path,
                    measuredView.width,
                    measuredView.height
                )
            }
            binding.image.setImageBitmap(scaledBitmap)
        }
        return super.onCreateDialog(savedInstanceState)
    }
}