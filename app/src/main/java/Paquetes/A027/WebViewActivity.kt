package Paquetes.A027

import Helpers.NetworkConstants
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity: AppCompatActivity() {
    private var mSafeBrowsingIsInitialized = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view_layout)

        val curriculum = intent.getStringExtra("curriculum")
        val webView = findViewById<WebView>(R.id.WebView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(NetworkConstants.urlDrivePDFViewer + NetworkConstants.urlCVPsicologoBase + curriculum)
    }
}