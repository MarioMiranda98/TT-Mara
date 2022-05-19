package Paquetes.A027

import Adapters.PreguntasAbiertasAdapter
import Helpers.NetworkConstants
import Models.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class PruebaAbiertaActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_abierta)

        val usuario = Helpers.Helpers.obtenerDatosStorage(Helpers.Constantes.nombreUsuarioClave, this)
        val prueba = intent.getSerializableExtra("prueba") as? PruebaAbiertaModel
        val mostrarResultados = intent.getBooleanExtra("resultados", false)
        val nombrePrueba = findViewById<TextView>(R.id.NombrePruebaTextViewAbierta)
        val listaPreguntas = findViewById<ListView>(R.id.ContenidoPruebaAbierta)
        val adapatorPreguntas = PreguntasAbiertasAdapter(this, prueba?.reactivos!!)
        val botonEnviar = findViewById<Button>(R.id.btnEnviarAbierta)
        val resultadoPruebaAbierta = findViewById<TextView>(R.id.ResultadoPruebaAbierta)

        nombrePrueba.text = prueba?.nombrePrueba
        listaPreguntas.adapter = adapatorPreguntas

        if(prueba?.reactivos.get(0).status.equals("Contestada")) {
            botonEnviar.isVisible = false
            resultadoPruebaAbierta.isVisible = false
        }

        if(prueba?.reactivos.get(0).status.equals("Pendiente")) {
            resultadoPruebaAbierta.isVisible = false
        }

        if(mostrarResultados) {
            botonEnviar.isVisible = false
            resultadoPruebaAbierta.isVisible = true
            val aux = resultadoPruebaAbierta.text.toString()
            resultadoPruebaAbierta.text = aux + " " + prueba
        }

        botonEnviar.setOnClickListener {
                it: View ->
            val items = adapatorPreguntas.items
            var reactivosRespuesta: ArrayList<ReactivoRespuestaAbiertaModel> = ArrayList<ReactivoRespuestaAbiertaModel>()
            var aux: String = ""

            if(items != null) {
                for(i in 0..items.size - 1) {
                    reactivosRespuesta.add(
                        ReactivoRespuestaAbiertaModel(
                            prueba.reactivos.get(i).pregunta,
                            items.get(i).respuesta
                        )
                    )
                }
            }

            val pruebaRespuestaAbiertaModel = PruebaAbiertaRespuestaModel(
                prueba.nombrePrueba,
                reactivosRespuesta,
                prueba.tipo,
                prueba.clasificacion,
            "",
                usuario
            )

            val gson = Gson()
            val jsonPruebaRes: String = gson.toJson(pruebaRespuestaAbiertaModel.reactivos_respuestas)
            Log.d("Json Prueba", jsonPruebaRes)

            val urlNueva = NetworkConstants.urlApi + NetworkConstants.responderPrueba + "?name=${pruebaRespuestaAbiertaModel.nombre_prueba}&paci=${pruebaRespuestaAbiertaModel.quien_respondio}&type=${pruebaRespuestaAbiertaModel.tipo}&clasif=${pruebaRespuestaAbiertaModel.clasif}&trial=${jsonPruebaRes}"
            val queue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlNueva, null, Response.Listener {
                        response -> Log.d("Respuesta", response.toString())
                },
                Response.ErrorListener {
                        error -> Log.d("Error", "Error al enviar las respuestas del formulario")
                }
            )

            queue.add(jsonObjectRequest)
        }
    }
}