package Paquetes.A027

import Adapters.PreguntasAbiertasAdapter
import Adapters.PreguntasAdapter
import Helpers.NetworkConstants
import Models.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class PruebaAbiertaActivity: AppCompatActivity() {
    var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_abierta)

        val usuario = Helpers.Helpers.obtenerDatosStorage(Helpers.Constantes.nombreUsuarioClave, this)
        val prueba = intent.getSerializableExtra("prueba") as? PruebaAbiertaModel
        val mostrarResultados = intent.getBooleanExtra("resultados", false)
        val nombrePrueba = findViewById<TextView>(R.id.NombrePruebaTextViewAbierta)
        val listaPreguntas = findViewById<ListView>(R.id.ContenidoPruebaAbierta)
        var adaptadorPreguntas = PreguntasAbiertasAdapter(this, extraerPreguntas(prueba?.reactivos!!, index, (index + 4)))
        val botonEnviar = findViewById<Button>(R.id.btnEnviarAbierta)
        val botonSiguiente = findViewById<Button>(R.id.btnSiguienteAbierta)
        val botonAnterior = findViewById<Button>(R.id.btnAnteriorAbierta)
        val botonResultado = findViewById<Button>(R.id.btnResultadosAbierta)
        nombrePrueba.text = prueba?.nombrePrueba
        listaPreguntas.adapter = adaptadorPreguntas
        botonEnviar.isVisible = false

        if(prueba?.reactivos.get(0).status.equals("Contestada")) {
            botonEnviar.isVisible = false
            botonResultado.isVisible = true
        }

        if(prueba?.reactivos.get(0).status.equals("Pendiente")) {
            botonResultado.isVisible = false
            botonEnviar.isVisible = false
        }

        botonEnviar.setOnClickListener {
                it: View ->
            val items = prueba?.reactivos.size
            var reactivosRespuesta: ArrayList<ReactivoRespuestaAbiertaModel> = ArrayList<ReactivoRespuestaAbiertaModel>()
            var aux: String = ""

            if(items != null) {
                for(i in 0..items - 1) {
                    reactivosRespuesta.add(
                        ReactivoRespuestaAbiertaModel(
                            prueba.reactivos.get(i).pregunta,
                            prueba.reactivos.get(i).respuesta
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

            val urlNueva = NetworkConstants.urlApi + NetworkConstants.responderPruebaAbierta + "?name=${pruebaRespuestaAbiertaModel.nombre_prueba}&paci=${pruebaRespuestaAbiertaModel.quien_respondio}&type=${pruebaRespuestaAbiertaModel.tipo}&clasif=${pruebaRespuestaAbiertaModel.clasif}&trial=${jsonPruebaRes}"
            val queue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlNueva, null, Response.Listener {
                        response -> Log.d("Respuesta", response.toString())
                        if(response.getInt("code") == 201) {
                            val urlStatus = NetworkConstants.urlApi + NetworkConstants.statusPrueba + "?trial=${pruebaRespuestaAbiertaModel.nombre_prueba}&paci=${pruebaRespuestaAbiertaModel.quien_respondio}"
                            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, urlStatus,
                                null, Response.Listener { response ->
                                    if(response.getInt("code") == 200) {
                                        Log.d("Status cambiado", "El status ha sido cambiado")
                                        val i = Intent(this, homepaciente::class.java)
                                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(i)
                                    }
                                },
                                Response.ErrorListener {
                                        error -> Log.d("Error", "Error al actualizar las respuestas del formulario")
                                    startActivity(Intent(this, pruebasasignadas::class.java))
                                }
                            )

                            queue.add(jsonObjectRequest)
                        }
                },
                Response.ErrorListener {
                        error -> Log.d("Error", "Error al enviar las respuestas del formulario")
                }
            )

           queue.add(jsonObjectRequest)
        }

        botonResultado.setOnClickListener {
                it: View ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Resultado")
            builder.setMessage(prueba.analisisTratamiento)
            builder.setIcon(android.R.drawable.ic_dialog_info)
            builder.setPositiveButton("Aceptar"){dialogInterface , which -> }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }


        botonSiguiente.setOnClickListener {
                it: View ->
            if(index + 5 < prueba?.reactivos.size) {
                val items = adaptadorPreguntas.items

                if(items != null) {
                    for(i in 0..items?.size!! - 1) {
                        prueba?.reactivos?.get((index) + i).respuesta = items.get(i).respuesta
                    }
                }

                index += 5
                adaptadorPreguntas = PreguntasAbiertasAdapter(this, extraerPreguntas(prueba?.reactivos!!, index, (index + 4)))
                listaPreguntas.adapter = adaptadorPreguntas

            } else {
                if(prueba?.reactivos.get(0).status.equals("Contestada")) {
                    botonEnviar.isVisible = false
                } else {
                    botonEnviar.isVisible = true
                }
            }
        }

        botonAnterior.setOnClickListener {
                it: View ->
            if ((index) > 0) {
                val items = adaptadorPreguntas.items

                if(items != null) {
                    for(i in 0..items?.size!! - 1) {
                        prueba?.reactivos?.get((index) + i).respuesta = items.get(i).respuesta
                    }
                }

                index -= 5
                adaptadorPreguntas = PreguntasAbiertasAdapter(this, extraerPreguntas(prueba?.reactivos!!, index, (index + 4)))
                listaPreguntas.adapter = adaptadorPreguntas
            }

            botonEnviar.isVisible = false
        }
    }

    private fun extraerPreguntas(items: ArrayList<ReactivoPruebaAbiertaModel>, indexInicio: Int, indexFinal: Int): ArrayList<ReactivoPruebaAbiertaModel> {
        var final: ArrayList<ReactivoPruebaAbiertaModel> = ArrayList()
        for(i in indexInicio..indexFinal) {
            final.add(items?.get(i))
        }
        return final
    }
}