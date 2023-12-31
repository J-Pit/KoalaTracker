package com.example.csc202assignment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.location.Location
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import java.util.UUID
private const val ARG_KOALA_ID = "koala_id"
class KoalaFragment : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        _binding = KoalaFragmentBinding.inflate(
                layoutInflater, container,
                false)
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
            koalaMap.setOnClickListener{findNavController().navigate(
                KoalaFragmentDirections.actionKoalaFragmentToMapFragment()
            )}

            updatePhoto(koala.photoFileName)
            koalaDelete.setOnClickListener {
                koalaViewModel.deleteKoala(koala)
                findNavController().navigate(KoalaFragmentDirections.toList())
            }
            koalaPhoto.setOnClickListener {
                findNavController().navigate(
                    KoalaFragmentDirections.actionKoalaFragmentToImageDialog(
                        koala.photoFileName
                    )
                )
            }
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                    fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken(){
                        override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
                        override fun isCancellationRequested() = false
                    }).addOnSuccessListener{location : Location? ->
                            location?.let {
                                koala.latitude = location.latitude
                                koala.longitude = location.longitude
                                koalaLocation.text =
                                    koala.latitude.toString() + " " + koala.longitude.toString()
                            }
                        }
            }
            koalaShare.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Koala found at " + koala.place + "on " + koala.date.toString())
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                       "Koala Found"
                    )
                }
                val chooserIntent = Intent.createChooser(
                    reportIntent,
                   "Send koala report")

                startActivity(chooserIntent)
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





