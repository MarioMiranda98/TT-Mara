package Helpers

import android.content.Context
import Requests.Network
import android.widget.Toast

class Helpers {
    companion object {
        fun validarConexionInternet(context: Context): Boolean {
            var hayConexion: Boolean = Network.redDisponible(context)

            if(!Network.redDisponible(context)) {
                Toast.makeText(context, "No hay conexión de red disponible", Toast.LENGTH_LONG).show()
            }

            return hayConexion
        }
    }
}