package Requests

import Helpers.NetworkConstants
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class Network {
    companion object {
        @RequiresApi(Build.VERSION_CODES.M)
        fun redDisponible(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

            val red = connectivityManager.activeNetwork ?: return false
            val redActual = connectivityManager.getNetworkCapabilities(red)?: return false

            return when {
                redActual.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                redActual.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                redActual.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

                else -> false
            }
        }
    }
}