package Paquetes.A027

import Adapters.PreguntasAdapter
import Helpers.NetworkConstants
import Models.OpcionesRespuestaModel
import Models.PruebaModel
import Models.PruebaRespuestaModel
import Models.ReactivoRespuestaModel
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.w3c.dom.Text

class PruebaInvitadoActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_responder_pruebas_invitado)

        val prueba = intent.getSerializableExtra("prueba") as? PruebaModel
        val nombrePrueba = findViewById<TextView>(R.id.NombrePruebaInvitado)
        val adapatorPreguntas = PreguntasAdapter(this, prueba?.reactivos!!)
        val listaPreguntas = findViewById<ListView>(R.id.ContenidoPruebaInvitado)
        val botonEnviar = findViewById<Button>(R.id.BtnEnviarPruebaInvitado)
        val resultadoTextView = findViewById<TextView>(R.id.TextViewResultadoInvitado)
        nombrePrueba.text = prueba?.nombrePrueba
        listaPreguntas.adapter = adapatorPreguntas


        botonEnviar.setOnClickListener {
            it: View ->
            val items = adapatorPreguntas.items
            var reactivosRespuesta: ArrayList<ReactivoRespuestaModel> = ArrayList<ReactivoRespuestaModel>()

            if (items != null) {
                for (i in 0..(items.size - 1)) {
                    var opcionesRespuestaModel: ArrayList<OpcionesRespuestaModel> = ArrayList<OpcionesRespuestaModel>()
                    for(j in 0..prueba.reactivos.get(0).opciones.size - 1) {
                        opcionesRespuestaModel.add(
                            OpcionesRespuestaModel(
                                prueba.reactivos.get(i).opciones.get(j),
                                prueba.reactivos.get(i).valor.get(j)
                            )
                        )
                    }
                    reactivosRespuesta.add(ReactivoRespuestaModel(
                        prueba.reactivos.get(i).pregunta,
                        opcionesRespuestaModel,
                        prueba.reactivos.get(i).tipo,
                        items.get(i).respuesta
                    )
                    )
                }
            }

            val pruebaRespuestaModel = PruebaRespuestaModel(
                prueba.nombrePrueba,
                reactivosRespuesta,
                prueba.tipo,
                prueba.clasificacion,
                "",
                "invitado"
            )
            val gson = Gson()
            val jsonPruebaRes: String = gson.toJson(pruebaRespuestaModel.reactivos_respuestas)
            Log.d("Json Pruebas", jsonPruebaRes)

            val urlNueva = NetworkConstants.urlApi + NetworkConstants.responderPruebaInvitados + "?name=${prueba.nombrePrueba}&paci=invitado&type=${prueba.tipo}&clasif=${prueba.clasificacion}&trial=$jsonPruebaRes"
            val queue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlNueva, null, Response.Listener {
                        response -> Log.d("Respuesta", response.toString())
                        if(response.getInt("code") == 201) {
                            var resultado: String = resultadoTextView.text.toString()
                            val json = JSONArray(response.getString("data"))
                            resultado += " " + json.get(0).toString()
                            resultadoTextView.text = resultado
                        }
                },
                Response.ErrorListener {
                        error -> Log.d("Error", "Error al enviar las respuestas del formulario")
                }
            )

            queue.add(jsonObjectRequest)
        }
    }
}