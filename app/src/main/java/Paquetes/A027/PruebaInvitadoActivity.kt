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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray

class PruebaInvitadoActivity: AppCompatActivity() {
    var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_responder_pruebas_invitado)

        val prueba = intent.getSerializableExtra("prueba") as? PruebaModel
        val nombrePrueba = findViewById<TextView>(R.id.NombrePruebaInvitado)
        var adapatorPreguntas = PreguntasAdapter(this, extraerPreguntas(prueba?.reactivos!!, index, (index + 1)))
        val listaPreguntas = findViewById<ListView>(R.id.ContenidoPruebaInvitado)
        val botonEnviar = findViewById<Button>(R.id.BtnEnviarPruebaInvitado)
        val botonSiguiente = findViewById<Button>(R.id.btnSiguienteInv)
        val botonAnterior = findViewById<Button>(R.id.btnAnteriorInv)
        botonEnviar.isVisible = false
        nombrePrueba.text = prueba?.nombrePrueba
        listaPreguntas.adapter = adapatorPreguntas


        botonEnviar.setOnClickListener {
            it: View ->
            val items = prueba?.reactivos.size
            var reactivosRespuesta: ArrayList<ReactivoRespuestaModel> = ArrayList<ReactivoRespuestaModel>()

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
                            val json = JSONArray(response.getString("data"))

                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Resultado")
                            builder.setMessage(json.get(0).toString())
                            builder.setIcon(android.R.drawable.ic_dialog_info)
                            builder.setPositiveButton("Aceptar"){dialogInterface , which ->
                                val i = Intent(this, MainActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(i)
                            }

                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                },
                Response.ErrorListener {
                        error -> Log.d("Error", "Error al enviar las respuestas del formulario")
                }
            )

            queue.add(jsonObjectRequest)
        }

        botonSiguiente.setOnClickListener {
            it: View ->
                if(index + 2 < prueba?.reactivos.size) {
                    val items = adapatorPreguntas.items

                    if(items != null) {
                        for(i in 0..items?.size!! - 1) {
                            prueba?.reactivos?.get((index) + i).respuesta = items.get(i).respuesta
                        }
                    }

                    index += 2
                    adapatorPreguntas = PreguntasAdapter(this, extraerPreguntas(prueba?.reactivos!!, index, (index + 1)))
                    listaPreguntas.adapter = adapatorPreguntas

                } else {
                    botonEnviar.isVisible = true
                }
        }

        botonAnterior.setOnClickListener {
                it: View ->
                    if ((index) > 0) {
                        val items = adapatorPreguntas.items

                        if(items != null) {
                            for(i in 0..items?.size!! - 1) {
                                prueba?.reactivos?.get((index) + i).respuesta = items.get(i).respuesta
                            }
                        }

                        index -= 2
                        adapatorPreguntas = PreguntasAdapter(this, extraerPreguntas(prueba?.reactivos!!, index, (index + 1)))
                        listaPreguntas.adapter = adapatorPreguntas
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