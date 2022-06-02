package Paquetes.A027

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
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class PruebaActivity: AppCompatActivity() {
        var index: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba)

        val usuario = Helpers.Helpers.obtenerDatosStorage(Helpers.Constantes.nombreUsuarioClave, this)
        val prueba = intent.getSerializableExtra("prueba") as? PruebaModel
        val mostrarResultados = intent.getBooleanExtra("resultados", false)
        val nombrePrueba = findViewById<TextView>(R.id.NombrePruebaTextView)
        val listaPreguntas = findViewById<ListView>(R.id.ContenidoPrueba)
        var adaptadorPreguntas = PreguntasAdapter(this, extraerPreguntas(prueba?.reactivos!!, index, (index + 1)))
        val botonEnviar = findViewById<Button>(R.id.btnEnviar)
        val botonResultado = findViewById<Button>(R.id.btnResultados)
        val botonSiguiente = findViewById<Button>(R.id.botonSiguiente)
        val botonAnterior = findViewById<Button>(R.id.botonAnterior)
        botonEnviar.isVisible = false
        nombrePrueba.text = prueba?.nombrePrueba
        listaPreguntas.adapter = adaptadorPreguntas

        if(prueba?.reactivos.get(0).status.equals("Contestada")) {
            botonEnviar.isVisible = false
            botonResultado.isVisible = true
        }

        if(prueba?.reactivos.get(0).status.equals("Pendiente")) {
            botonEnviar.isVisible = true
            botonResultado.isVisible = false
        }

        botonEnviar.setOnClickListener {
            it: View ->
            val items = prueba?.reactivos.size
            var reactivosRespuesta: ArrayList<ReactivoRespuestaModel> = ArrayList<ReactivoRespuestaModel>()
            var aux: String = ""

            var opcionesRespuestaModel: ArrayList<OpcionesRespuestaModel> = ArrayList<OpcionesRespuestaModel>()

            if (items != null) {
                for (i in 0..(items - 1)) {
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
                        prueba.reactivos.get(i).respuesta
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
                usuario
            )
            val gson = Gson()
            val jsonPruebaRes: String = gson.toJson(pruebaRespuestaModel.reactivos_respuestas)
            Log.d("Json Prueba", jsonPruebaRes)

            val urlNueva = NetworkConstants.urlApi + NetworkConstants.responderPrueba + "?name=${pruebaRespuestaModel.nombre_prueba}&paci=${pruebaRespuestaModel.quien_respondio}&type=${pruebaRespuestaModel.tipo}&clasif=${pruebaRespuestaModel.clasif}&trial=$jsonPruebaRes"
            val queue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlNueva, null, Response.Listener {
                    response -> Log.d("Respuesta", response.toString())
                    if(response.getInt("code") == 201) {
                        val urlStatus = NetworkConstants.urlApi + NetworkConstants.statusPrueba + "?trial=${pruebaRespuestaModel.nombre_prueba}&paci=${pruebaRespuestaModel.quien_respondio}"
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
                if(index + 2 < prueba?.reactivos.size) {
                    val items = adaptadorPreguntas.items

                    if(items != null) {
                        for(i in 0..items?.size!! - 1) {
                            prueba?.reactivos?.get((index) + i).respuesta = items.get(i).respuesta
                        }
                    }

                    index += 2
                    adaptadorPreguntas = PreguntasAdapter(this, extraerPreguntas(prueba?.reactivos!!, index, (index + 1)))
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

                    index -= 2
                    adaptadorPreguntas = PreguntasAdapter(this, extraerPreguntas(prueba?.reactivos!!, index, (index + 1)))
                    listaPreguntas.adapter = adaptadorPreguntas
                }

                botonEnviar.isVisible = false
            }
        }

    private fun extraerPreguntas(items: ArrayList<ReactivoPruebaModel>, indexInicio: Int, indexFinal: Int): ArrayList<ReactivoPruebaModel> {
        var final: ArrayList<ReactivoPruebaModel> = ArrayList()
        for(i in indexInicio..indexFinal) {
            final.add(items?.get(i))
        }
        return final
    }
}