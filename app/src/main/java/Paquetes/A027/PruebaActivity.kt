package Paquetes.A027

import Adapters.PreguntasAdapter
import Models.PruebaModel
import android.os.Bundle
import android.text.Layout
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class PruebaActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba)

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
    }
}