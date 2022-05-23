package Paquetes.A027

import Adapters.PruebasAsignadasAdapter
import Adapters.PruebasPsicologoAdapter
import Helpers.NetworkConstants
import Models.ListaPruebasModel
import Models.ListaPsicologosPruebasModel
import Models.PruebaAbiertaModel
import Models.PruebaModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class psicologo_estadodepruebas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_psicologo_estadodepruebas)

        val listaAsignadas = findViewById<ListView>(R.id.ContenidoPiscologo)
        val usuario = Helpers.Helpers.obtenerDatosStorage(Helpers.Constantes.nombreUsuarioClave, this)
        val url: String = NetworkConstants.urlApi + NetworkConstants.asignacionesPsicologo + "?psico=$usuario"
        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(url, null, Response.Listener {
                response ->
            if(response.getInt("code") == 200) {
                var lista: ArrayList<ListaPsicologosPruebasModel> =
                    ArrayList<ListaPsicologosPruebasModel>()
                val json = JSONObject(response.toString())
                val asignaciones = json.getJSONArray("data")
                for (i in 0..asignaciones.length() - 1) {
                    lista.add(ListaPsicologosPruebasModel(asignaciones.getJSONObject(i)))
                }

                val adaptadorAsignaciones = PruebasPsicologoAdapter(this, lista)
                listaAsignadas.adapter = adaptadorAsignaciones
            }
        },
            Response.ErrorListener {
                    error -> Log.d("Pruebas asignadas", error.toString())
                val lista: ArrayList<String> = arrayListOf<String>("Sin Pruebas")
                val adaptadorAsignaciones = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, lista)

                listaAsignadas.adapter = adaptadorAsignaciones
            }
        )

        queue.add(jsonObjectRequest)
    }
}