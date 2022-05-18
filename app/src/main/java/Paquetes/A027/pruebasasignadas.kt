package Paquetes.A027

import Adapters.PruebasAsignadasAdapter
import Helpers.NetworkConstants
import Models.PruebaModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.sql.Struct

class pruebasasignadas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebasasignadas)

        val listaAsignaciones = findViewById<ListView>(R.id.Pruebas_Asignadas)
        val usuario = Helpers.Helpers.obtenerDatosStorage(Helpers.Constantes.nombreUsuarioClave, this)
        val url: String = NetworkConstants.urlApi + NetworkConstants.pruebasAsignadas + "?paci=$usuario"
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(url, null, Response.Listener {
                response ->
                    Log.d("respuesta", response.get("data").toString())
                    var lista: ArrayList<PruebaModel> = ArrayList<PruebaModel>()
                    val json = JSONObject(response.toString())
                    val asignaciones = json.getJSONArray("data")
                    for(i in 0..asignaciones.length() - 1) {
                        lista.add(PruebaModel(asignaciones.getJSONObject(i)))
                    }

                    //val adaptadorAsignaciones = ArrayAdapter<PruebaModel>(this, android.R.layout.simple_expandable_list_item_1, lista)
                    val adaptadorAsignaciones = PruebasAsignadasAdapter(this, lista)
                    listaAsignaciones.adapter = adaptadorAsignaciones
                    listaAsignaciones.onItemClickListener = AdapterView.OnItemClickListener {
                        parent, view, position, id ->
                            Toast.makeText(this, lista.get(position).prueba, Toast.LENGTH_LONG).show()
                    }
            },
            Response.ErrorListener {
                error -> Log.d("Pruebas asignadas", error.toString())
                val lista: ArrayList<String> = arrayListOf<String>("Sin Pruebas")
                val adaptadorAsignaciones = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, lista)

                listaAsignaciones.adapter = adaptadorAsignaciones
            }
        )

        queue.add(jsonObjectRequest)
    }
}