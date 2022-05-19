package Paquetes.A027

import Adapters.PruebasAsignadasAdapter
import Adapters.PruebasInvitadoAdapter
import Helpers.NetworkConstants
import Models.ListaPruebasInvitadoModel
import Models.ListaPruebasModel
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
                                    Toast.makeText(this, "Registrese para acceder a las pruebas", Toast.LENGTH_LONG).show()
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