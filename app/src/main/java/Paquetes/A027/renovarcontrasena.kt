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

class renovarcontrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_renovarcontrasena)

        val buttonconfnewcontra = findViewById<Button>(R.id.buttonconfnewcontra)
        val editTextPass = findViewById<EditText>(R.id.ingr_newpassword)
        val editTextPassConf = findViewById<EditText>(R.id.ingr_newpassword2)
        val editTextCodigo = findViewById<EditText>(R.id.codigoVerficiacion)
        val editTextEmail = findViewById<EditText>(R.id.email)

        val email = editTextEmail.getText().trim()
        val pass = editTextPass.getText().trim()
        val passConf = editTextPassConf.getText().trim()
        val codigo = editTextCodigo.getText().trim()

        buttonconfnewcontra.setOnClickListener {
            val url: String = NetworkConstants.urlApi + NetworkConstants.renovarPass + "?email=$email&pass_token$codigo&password=$pass"
            val queue = Volley.newRequestQueue(this)

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, null, Response.Listener {
                    response ->
                Log.d("Renovar pass", response.toString())
                if(response.get("code").toString().toInt() == 201) {
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this,"Contraseña modificada", Toast.LENGTH_LONG).show()
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
    }
}