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
    private var _binding: ImagedialogBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ImagedialogBinding.inflate(layoutInflater)
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