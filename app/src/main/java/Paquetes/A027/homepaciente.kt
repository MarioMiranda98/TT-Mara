package Paquetes.A027

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

class homepaciente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepaciente)

        val imageButtonPasign = findViewById<ImageButton>(R.id.imageButtonPasign)
        imageButtonPasign.setOnClickListener {
            val pasignadas = Intent(this, pruebasasignadas::class.java)
            startActivity(pasignadas)
        }

        val imageButtonPrespon = findViewById<ImageButton>(R.id.imageButtonPrespon)
        imageButtonPrespon.setOnClickListener {
            val prespondidas = Intent(this, pruebasrespondidas::class.java)
            startActivity(prespondidas)
        }

        val imageButtonReport = findViewById<ImageButton>(R.id.imageButtonReport)
        imageButtonReport.setOnClickListener {
            val dreportes = Intent(this, reportes::class.java)
            startActivity(dreportes)
        }
    }
}