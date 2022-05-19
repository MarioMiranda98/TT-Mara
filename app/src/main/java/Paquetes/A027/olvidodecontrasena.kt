package Paquetes.A027

import Helpers.NetworkConstants
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class olvidodecontrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvidodecontrasena)

        val buttonconfcorreo = findViewById<Button>(R.id.buttonconfcorreo)
        val buttoncancelarcontra = findViewById<Button>(R.id.buttoncancelarcontra)

        buttonconfcorreo.setOnClickListener {
            val usrEmail = findViewById<EditText>(R.id.correocontrasena)
            val email = usrEmail.getText().trim()
            val url: String = NetworkConstants.urlApi + NetworkConstants.resetPassPaciente + "?email=" + email
            val queue = Volley.newRequestQueue(this)

            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, Response.Listener {
                    response ->
                Log.d("Reset pass", response.toString())
                if(response.get("code").toString().toInt() == 201) {
                    startActivity(Intent(this, renovarcontrasena::class.java))
                    Toast.makeText(this,"Se ha enviado un mensaje a su correo para restablecer la contraseña", Toast.LENGTH_LONG).show()
                } else{
                    Toast.makeText(this, "${response.get("message").toString()}", Toast.LENGTH_SHORT).show()
                }
            },
                Response.ErrorListener {
                        error -> Log.d("Reset pass", error.toString())
                }
            )

            queue.add(jsonObjectRequest)
        }

        buttoncancelarcontra.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}