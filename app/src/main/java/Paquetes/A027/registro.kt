package Paquetes.A027

import Helpers.NetworkConstants
import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.File
import java.io.IOException


class registro : AppCompatActivity() {
    private var usuarioPsicologo: ArrayList<String> = ArrayList<String>()
    private var curriculumPsicologo: ArrayList<String> = ArrayList<String>()
    private val pickImage = 100
    lateinit var uriImagen: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val nombreUsuarioEditText = findViewById<EditText>(R.id.editTextNombre)
        val apellidosUsuarioEditText = findViewById<EditText>(R.id.editTextApellidos)
        val correoUsuarioEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val edadUsuarioEditText = findViewById<EditText>(R.id.editTextNumberEdad)
        val domicilioUsuarioEditText = findViewById<EditText>(R.id.EditTextDomicilio)
        val propositoUsuarioEditText = findViewById<EditText>(R.id.editTextProposito)
        val nombreUserEditText = findViewById<EditText>(R.id.editTextUser)
        val passwordUserEditText = findViewById<EditText>(R.id.editTextTextPassword)
        val confirmPasswordUser = findViewById<EditText>(R.id.editTextTextPassword2)
        val spinner = findViewById<Spinner>(R.id.spinnergenero)
        val listagenero = resources.getStringArray(R.array.genero)
        val buttoncancelar = findViewById<Button>(R.id.buttoncancelar)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,listagenero)
        val spinner2 = findViewById<Spinner>(R.id.spinnerpsicologo)
        val listapsicologo = resources.getStringArray(R.array.psicologos)

        val buttonCrearcuenta = findViewById<Button>(R.id.buttonCrearcuenta)
        val buttonFoto = findViewById<Button>(R.id.buttonfoto)
        val botonCV = findViewById<Button>(R.id.buttonCV)

        val url: String = NetworkConstants.urlApi + NetworkConstants.psicologos
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(url,null, Response.Listener {
                response ->
            //Log.d("Listar Psicologos", response.toString())
            if(response.get("code").toString().toInt() == 200){
                var lista: ArrayList<String> = ArrayList<String>()
                val json = JSONObject(response.toString())
                val psicologos = json.getJSONArray("data")
                for (i in 0..psicologos.length() - 1) {
                    val nombrePsicologo = psicologos.getJSONObject(i).getString("nombre")
                    val apellidosPsicologo = psicologos.getJSONObject(i).getString("apellidos")

                    lista.add(nombrePsicologo + " " + apellidosPsicologo)
                    usuarioPsicologo.add(psicologos.getJSONObject(i).getString("nombre_usuario"))
                    curriculumPsicologo.add(psicologos.getJSONObject(i).getString("curriculum"))
                }

                val adaptador2 = ArrayAdapter(this,android.R.layout.simple_spinner_item, lista)
                spinner2.adapter = adaptador2
            } else{
                Toast.makeText(this, "${response.get("message").toString()}", Toast.LENGTH_SHORT).show()
                val adaptador2 = ArrayAdapter(this,android.R.layout.simple_spinner_item, listapsicologo)
                spinner2.adapter = adaptador2
            }
        },
            Response.ErrorListener {
                    error -> Log.d("Login Paciente", error.toString())
            }
        )

        queue.add(jsonObjectRequest)

        spinner.adapter = adaptador


        buttonCrearcuenta.setOnClickListener {
            //Log.d("selected psico", usuarioPsicologo[spinner2.selectedItemPosition])
            //startActivity(Intent(this, MainActivity::class.java))
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Creando cuenta de Paciente")
            progressDialog.setMessage("Por favor, espere un momento, se estan registrando sus datos en el sistema")
            progressDialog.show()

            val nombreUsuario = nombreUsuarioEditText.getText().trim()
            val apellidosUsuario = apellidosUsuarioEditText.getText().trim()
            val correoUsuario = correoUsuarioEditText.getText().trim()
            val edadUsuario = edadUsuarioEditText.getText().trim()
            val domicilioUsuario = domicilioUsuarioEditText.getText().trim()
            val propositoUsuario = propositoUsuarioEditText.getText().trim()
            val nombreUser = nombreUserEditText.getText().trim()
            val passwordUser = passwordUserEditText.getText().trim()
            val confirmPassword = confirmPasswordUser.getText().trim()
            val psicologoElegido = usuarioPsicologo[spinner2.selectedItemPosition]
            var genero = ""
            if(spinner.getSelectedItem().toString().equals("Masculino")) { genero = "M" }
            else { genero = "F" }

            //Log.d("URI Imagen", uriImagen)
            val client = OkHttpClient()
            val url = NetworkConstants.urlApi + NetworkConstants.crearPaciente + "?user=$nombreUser&pass=$passwordUser&name=$nombreUsuario&lastname=$apellidosUsuario&age=$edadUsuario&gender=$genero&email=$correoUsuario&home=$domicilioUsuario&prod=$propositoUsuario&psico=$psicologoElegido"
            val fileImagen = File(uriImagen)
            if(fileImagen.exists()) Log.d("Existe", "Existe")

            val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("img", fileImagen.name,
                RequestBody.create("image/png".toMediaTypeOrNull(), fileImagen))
                .addFormDataPart("img", "img")
                .build()

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Fallo", e.toString())
                    progressDialog.hide()
                    Toast.makeText(applicationContext, "La cuenta no pudo ser creada, intentelo más tarde", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call, response: okhttp3.Response) {
                    Log.d("A mimir", response.toString())
                    progressDialog.hide()
                    Toast.makeText(applicationContext, "La cuenta ha sido creada", Toast.LENGTH_LONG).show()
                }
            })
        }

        buttoncancelar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this,"No registró una cuenta", Toast.LENGTH_LONG).show()
        }

        buttonFoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                val REQUEST_CODE_PERMISSION_STORAGE = 100
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                for (str in permissions) {
                    if (checkSelfPermission(str!!) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(permissions, REQUEST_CODE_PERMISSION_STORAGE)
                    }
                }
            }

            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        botonCV.setOnClickListener {
            val curriculum = curriculumPsicologo[spinner2.selectedItemPosition]
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("curriculum", curriculum)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imagenPerfil = findViewById<ImageView>(R.id.imagenPerfil)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            //Log.d("imagen", data?.data.toString())
            imagenPerfil.setImageURI(data?.data)
            uriImagen = getRealPathFromURI(applicationContext, data?.data!!)!!
        }
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null)
            val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }
}