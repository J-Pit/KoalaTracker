package com.example.csc202assignment

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings.System.DATE_FORMAT
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.csc202assignment.databinding.KoalaFragmentBinding
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import java.util.UUID
private const val ARG_KOALA_ID = "koala_id"
class KoalaFragment : Fragment() {

    private val args: KoalaFragmentArgs by navArgs()
    val koalaViewModel: KoalaViewModel by viewModels {
        KoalaViewModel.KoalaDetailViewModelFactory(args.koalaId)
    }

    companion object {
        fun newInstance(koalaID: UUID): KoalaFragment {
            val args = Bundle().apply { putSerializable(ARG_KOALA_ID, koalaID) }
            return KoalaFragment().apply { arguments = args }
        }
    }

    private var _binding: KoalaFragmentBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            KoalaFragmentBinding.inflate(
                layoutInflater, container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            koalaTitle.doOnTextChanged { text, _, _, _ ->
                koalaViewModel.updateKoala { oldKoala ->
                    oldKoala.copy(title = text.toString())
                }
            }
            koalaPlace.doOnTextChanged { text, _, _, _ ->
                koalaViewModel.updateKoala { oldKoala ->
                    oldKoala.copy(place = text.toString())
                }
            }
            koalaCamera.setOnClickListener {
                photoName = "IMG_${Date()}.JPG"
                val photoFile = File(requireContext().applicationContext.filesDir, photoName)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.csc202assignment.fileprovider",
                    photoFile
                )
                takePhoto.launch(photoUri)
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    koalaViewModel.koala.collect { koala ->
                        koala?.let { updateUi(it) }
                    }
                }
            }

            setFragmentResultListener(
                DatePickerFragment.REQUEST_KEY_DATE
            ) { _, bundle ->
                val newDate =
                    bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
                koalaViewModel.updateKoala { it.copy(date = newDate) }
            }


        }
    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto: Boolean ->
        if (didTakePhoto && photoName != null) {
            koalaViewModel.updateKoala { oldKoala ->
                oldKoala.copy(photoFileName = photoName)
            }
        }
    }
    private var photoName: String? = null



    private fun updateUi(koala: Koala) {
        binding.apply {
            if (koalaTitle.text.toString() != koala.title) {
                koalaTitle.setText(koala.title)
            }
            if (koalaPlace.text.toString() != koala.place) {
                koalaPlace.setText(koala.place)
            }


            koalaDate.text = koala.date.toString()
            koalaDate.setOnClickListener {
                findNavController().navigate(
                    KoalaFragmentDirections.selectDate(koala.date)
                )

            }
            koalaLat.text = koala.latitude.toString()
            koalaLong.text = koala.longitude.toString()
            updatePhoto(koala.photoFileName)
            koalaDelete.setOnClickListener{
                koalaViewModel.deleteKoala(koala)
                findNavController().navigate(KoalaFragmentDirections.toList())
            }

        }
    }
    private fun updatePhoto(photoFileName: String?) {
        if (binding.koalaPhoto.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }
            if (photoFile?.exists() == true) {
                binding.koalaPhoto.doOnLayout { measuredView ->
                    val scaledBitmap = getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.koalaPhoto.setImageBitmap(scaledBitmap)
                    binding.koalaPhoto.tag = photoFileName

                }
            } else {
                binding.koalaPhoto.setImageBitmap(null)
                binding.koalaPhoto.tag = null
            }
        }
    }
}





