package Paquetes.A027

import Adapters.PreguntasAbiertasAdapter
import Models.PruebaAbiertaModel
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PruebaAbiertaActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_abierta)

        val prueba = intent.getSerializableExtra("prueba") as? PruebaAbiertaModel
        val nombrePrueba = findViewById<TextView>(R.id.NombrePruebaTextViewAbierta)
        val listaPreguntas = findViewById<ListView>(R.id.ContenidoPruebaAbierta)
        val adapatorPreguntas = PreguntasAbiertasAdapter(this, prueba?.reactivos!!)

        nombrePrueba.text = prueba?.nombrePrueba
        listaPreguntas.adapter = adapatorPreguntas
    }
}