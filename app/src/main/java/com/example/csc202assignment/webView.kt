package com.example.csc202assignment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.csc202assignment.databinding.HelpFragmentBinding
import com.example.csc202assignment.databinding.ImagedialogBinding

class webView :Fragment() {
    private var _binding: HelpFragmentBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = HelpFragmentBinding.inflate(layoutInflater)
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl("https://wildlifewarriors.org.au/conservation-projects/koala-conservation")
        super.onCreate(savedInstanceState)
    }
}