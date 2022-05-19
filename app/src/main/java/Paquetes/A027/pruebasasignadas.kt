package Paquetes.A027

import Adapters.PruebasAsignadasAdapter
import Helpers.NetworkConstants
import Models.ListaPruebasModel
import Models.PruebaAbiertaModel
import Models.PruebaModel
import android.content.Intent
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
                    //Log.d("respuesta", response.get("data").toString())
                if(response.getInt("code") == 200) {
                    var lista: ArrayList<ListaPruebasModel> = ArrayList<ListaPruebasModel>()
                    val json = JSONObject(response.toString())
                    val asignaciones = json.getJSONArray("data")
                    for (i in 0..asignaciones.length() - 1) {
                        lista.add(ListaPruebasModel(asignaciones.getJSONObject(i)))
                    }

                    //val adaptadorAsignaciones = ArrayAdapter<PruebaModel>(this, android.R.layout.simple_expandable_list_item_1, lista)
                    val adaptadorAsignaciones = PruebasAsignadasAdapter(this, lista)
                    listaAsignaciones.adapter = adaptadorAsignaciones
                    listaAsignaciones.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, view, position, id ->
                            var urlNueva: String = ""
                            if (lista.get(position).status.equals("Pendiente")) {
                                urlNueva =
                                    NetworkConstants.urlApi + NetworkConstants.findtrial + "?name=${
                                        lista.get(position).prueba
                                    }"
                            } else {
                                urlNueva =
                                    NetworkConstants.urlApi + NetworkConstants.findtrialRespondido + "?trial=${
                                        lista.get(position).prueba
                                    }&paci=$usuario"
                            }
                            val jsonObjectRequest =
                                JsonObjectRequest(urlNueva, null, Response.Listener { response ->
                                    Log.d("respuesta trial", response.toString())
                                    if (response.getInt("code") == 200) {
                                        val json = JSONObject(response.toString())

                                        //Log.d("Prueba Model", pruebaRequest.reactivos.get(0).pregunta.toString())
                                        if (json.getJSONArray("data").getJSONObject(0)
                                                .getString("tipo")
                                                .equals("Likert")
                                        ) {
                                            val pruebaRequest: PruebaModel = PruebaModel(
                                                json.getJSONArray("data").getJSONObject(0),
                                                lista.get(position).status
                                            )
                                            val intent = Intent(this, PruebaActivity::class.java)
                                            intent.putExtra("prueba", pruebaRequest)
                                            intent.putExtra("resultados", false)
                                            startActivity(intent)
                                        } else {
                                            val intent =
                                                Intent(this, PruebaAbiertaActivity::class.java)
                                            val pruebaRequest: PruebaAbiertaModel =
                                                PruebaAbiertaModel(
                                                    json.getJSONArray("data").getJSONObject(0),
                                                    lista.get(position).status
                                                )
                                            intent.putExtra("prueba", pruebaRequest)
                                            intent.putExtra("resultados", false)
                                            startActivity(intent)
                                        }
                                    } else {
                                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                                    }
                                },
                                    Response.ErrorListener { error ->
                                        Log.d("Error", "Error buscar formulario")
                                    }
                                )
                            queue.add(jsonObjectRequest)
                        }
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