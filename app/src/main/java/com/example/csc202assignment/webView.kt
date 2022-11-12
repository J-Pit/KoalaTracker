package com.example.csc202assignment

import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.csc202assignment.databinding.HelpFragmentBinding


class webView :Fragment() {
    private lateinit var webView: WebView
    private lateinit var binding: HelpFragmentBinding


    inner class koalaWebViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler?, error: SslError) {
            handler?.proceed()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HelpFragmentBinding.inflate(inflater, container, false)
        webView = binding.webview
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = koalaWebViewClient()
        webView.loadUrl("https://wildlifewarriors.org.au/conservation-projects/koala-conservation")
        return binding.root
    }

}