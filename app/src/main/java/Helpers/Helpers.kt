package Helpers

import android.content.Context
import Requests.Network
import android.util.Log
import android.widget.Toast
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

class Helpers {
    companion object {
        fun validarConexionInternet(context: Context): Boolean {
            var hayConexion: Boolean = Network.redDisponible(context)

            if(!Network.redDisponible(context)) {
                Toast.makeText(context, "No hay conexión de red disponible", Toast.LENGTH_LONG).show()
            }

            return hayConexion
        }

        fun guardarDatosStorage(clave: String, valor: String, context: Context) {
            try {
                val fos: FileOutputStream =   context.applicationContext.openFileOutput(clave, Context.MODE_PRIVATE)
                fos.write(valor.toByteArray())
            } catch (e: Exception) {
                Log.d("Error Storage", "Error al guardar datos en el storage")
            }
        }

        fun obtenerDatosStorage(clave: String, context: Context): String {
            var fis: FileInputStream? = null
            fis = context.applicationContext.openFileInput(clave)

            var isr: InputStreamReader = InputStreamReader(fis)
            var bufferedReader: BufferedReader = BufferedReader(isr)
            val stringBuilder: StringBuilder = StringBuilder()
            var texto: String? = null

            while({texto = bufferedReader.readLine(); texto}() != null) {
                stringBuilder.append(texto)
            }

            return stringBuilder.toString()
        }
    }
}