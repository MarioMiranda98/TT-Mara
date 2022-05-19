package Paquetes.A027

import Adapters.PreguntasAdapter
import Models.PruebaModel
import Models.PruebaRespuestaModel
import Models.ReactivoRespuestaModel
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.gson.Gson

class PruebaActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba)

        val usuario = Helpers.Helpers.obtenerDatosStorage(Helpers.Constantes.nombreUsuarioClave, this)
        val prueba = intent.getSerializableExtra("prueba") as? PruebaModel
        val mostrarResultados = intent.getBooleanExtra("resultados", false)
        val nombrePrueba = findViewById<TextView>(R.id.NombrePruebaTextView)
        val layoutResultados = findViewById<LinearLayout>(R.id.ResultadosLayout)
        var analisisTratamiento = findViewById<TextView>(R.id.AnalisisTratamientotextView)
        val listaPreguntas = findViewById<ListView>(R.id.ContenidoPrueba)
        val adapatorPreguntas = PreguntasAdapter(this, prueba?.reactivos!!)
        val botonEnviar = findViewById<Button>(R.id.btnEnviar)
        nombrePrueba.text = prueba?.nombrePrueba
        listaPreguntas.adapter = adapatorPreguntas

        if(prueba?.reactivos.get(0).status.equals("Contestada")) {
            botonEnviar.isVisible = false
            layoutResultados.isVisible = false
            analisisTratamiento.isVisible = false
        }

        if(prueba?.reactivos.get(0).status.equals("Pendiente")) {
            analisisTratamiento.isVisible = false
        }

        if(mostrarResultados) {
            botonEnviar.isVisible = false
            layoutResultados.isVisible = true
            analisisTratamiento.isVisible = true
            val aux = analisisTratamiento.text.toString()
            analisisTratamiento.text = aux + " " + prueba.analisisTratamiento
        }

        botonEnviar.setOnClickListener {
            it: View ->
                val items = adapatorPreguntas.items
                var reactivosRespuesta: ArrayList<ReactivoRespuestaModel> = ArrayList<ReactivoRespuestaModel>()
                var aux: String = ""

            if (items != null) {
                for (i in 0..(items.size - 1)) {
                    reactivosRespuesta.add(ReactivoRespuestaModel(
                            prueba.reactivos.get(i).pregunta,
                            prueba.reactivos.get(i).opciones,
                            prueba.reactivos.get(i).valor.get(i),
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
                usuario
            )
            val gson = Gson()
            val jsonPruebaRes: String = gson.toJson(pruebaRespuestaModel)
            Log.d("Json Prueba", jsonPruebaRes)
        }
    }
}