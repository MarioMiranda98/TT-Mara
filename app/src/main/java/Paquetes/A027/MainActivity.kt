package Paquetes.A027

import Helpers.NetworkConstants
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest
import java.lang.Error

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var conexionDisponible: Boolean = Helpers.Helpers.validarConexionInternet(this)
        val buttonrecord = findViewById<Button>(R.id.buttonrecord)
        val buttonfrgpassword = findViewById<Button>(R.id.buttonfrgpassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttoninvited = findViewById<Button>(R.id.buttoninvited)
        val buttonPsicologo = findViewById<Button>(R.id.sesion_psicologo)

        buttonrecord.setOnClickListener {
            startActivity(Intent(this, registro::class.java))
        }

        buttonfrgpassword.setOnClickListener {
            startActivity(Intent(this, olvidodecontrasena::class.java))
        }

        buttonLogin.setOnClickListener {
            val userText = findViewById<EditText>(R.id.ingr_user)
            val passText = findViewById<EditText>(R.id.ingr_password)

            val url: String = NetworkConstants.urlApi + NetworkConstants.loginPaciente + "?email=" + userText.getText().trim() + "&pass=" + passText.getText().trim()
            val queue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(url,null,Response.Listener {
                    response ->
                        Log.d("Login Paciente", response.toString())
                        if(response.get("success").toString().toBoolean()){
                            limpiarCampos(userText, passText)
                            startActivity(Intent(this, homepaciente::class.java))
                        } else{
                            Toast.makeText(this, "${response.get("message").toString()}", Toast.LENGTH_SHORT).show()
                        }
                },
                    Response.ErrorListener {
                        error -> Log.d("Login Paciente", error.toString())
                    }
            )

            queue.add(jsonObjectRequest)
        }

        buttonPsicologo.setOnClickListener {
            val userText = findViewById<EditText>(R.id.ingr_user)
            val passText = findViewById<EditText>(R.id.ingr_password)

            val url: String = NetworkConstants.urlApi + NetworkConstants.loginPsicologo + "?email=" + userText.getText().trim() + "&pass=" + passText.getText().trim()
            val queue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(url,null,Response.Listener {
                    response ->
                Log.d("Login Paciente", response.toString())
                if(response.get("success").toString().toBoolean()){
                    limpiarCampos(userText, passText)
                    startActivity(Intent(this, psicologo_estadodepruebas::class.java))
                } else{
                    Toast.makeText(this, "${response.get("message").toString()}", Toast.LENGTH_SHORT).show()
                }
            },
                Response.ErrorListener {
                        error -> Log.d("Login Paciente", error.toString())
                }
            )

            queue.add(jsonObjectRequest)
        }

        buttoninvited.setOnClickListener {
            startActivity(Intent(this, homeinvitado::class.java))
        }

        if(!conexionDisponible) { deshabilitarControles(buttonrecord, buttonfrgpassword,
            buttonLogin, buttoninvited) }
    }

    private fun deshabilitarControles(buttonrecord: Button, buttonfrgpassword: Button,
                              buttonLogin: Button, buttoninvited: Button) {
        buttonrecord.isEnabled = false
        buttonLogin.isEnabled = false
        buttonfrgpassword.isEnabled = false
        buttoninvited.isEnabled = false
    }

    private fun limpiarCampos(userText: EditText, passText: EditText) {
        userText.setText("")
        passText.setText("")
    }
}
