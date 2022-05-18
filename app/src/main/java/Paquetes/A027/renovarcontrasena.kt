package Paquetes.A027

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.Toast

class renovarcontrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_renovarcontrasena)

        val buttonconfnewcontra = findViewById<Button>(R.id.buttonconfnewcontra)
        buttonconfnewcontra.setOnClickListener {
        val newcontrasena = Intent(this, MainActivity::class.java)
        startActivity(newcontrasena)
        val Toastnewcontra = Toast.makeText(this,"Contraseña modificada", Toast.LENGTH_LONG).show()

        }
    }
}