package Paquetes.A027

import Adapters.PruebasAsignadasAdapter
import Adapters.PruebasInvitadoAdapter
import Helpers.NetworkConstants
import Models.ListaPruebasInvitadoModel
import Models.ListaPruebasModel
import Models.PruebaModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class homeinvitado : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homeinvitado)

        val listasInivitado = findViewById<ListView>(R.id.ContenidoInvitado)
        val url: String = NetworkConstants.urlApi + NetworkConstants.pruebasInvitado
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(url, null, Response.Listener {
            response ->
                if(response.getInt("code") == 200) {
                    var lista: ArrayList<ListaPruebasInvitadoModel> = ArrayList<ListaPruebasInvitadoModel>()
                    val json = JSONObject(response.toString())
                    val asignaciones = json.getJSONArray("data")
                    for (i in 0..asignaciones.length() - 1) {
                        if (asignaciones.getJSONObject(i).getString("tipo").equals("Likert"))
                            lista.add(ListaPruebasInvitadoModel(asignaciones.getJSONObject(i)))
                    }

                    val adaptadorInvitados = PruebasInvitadoAdapter(this, lista)
                    listasInivitado.adapter = adaptadorInvitados
                    listasInivitado.onItemClickListener =
                        AdapterView.OnItemClickListener {
                                parent, view, position, id ->
                                val urlNueva: String = NetworkConstants.urlApi + NetworkConstants.findtrial + "?name=${lista.get(position).prueba }"
                                val jsonObjectRequest = JsonObjectRequest(urlNueva, null, Response.Listener {
                                    response ->  Log.d("Respuesta", response.toString())
                                    if(response.getInt("code") == 200) {
                                        val json = JSONObject(response.toString())
                                        val pruebaRequest = PruebaModel(
                                            json.getJSONArray("data").getJSONObject(0),
                                            "Pendiente"
                                        )
                                        val intent = Intent(this, PruebaInvitadoActivity::class.java)
                                        intent.putExtra("prueba", pruebaRequest)
                                        startActivity(intent)
                                    }
                                }, Response.ErrorListener {
                                    error -> Log.d("Error", error.toString())
                                })

                                queue.add(jsonObjectRequest)
                        }
                }
            },
                Response.ErrorListener {
                    error -> Log.d("Error", error.toString())
                }
            )
        queue.add(jsonObjectRequest)
    }
}