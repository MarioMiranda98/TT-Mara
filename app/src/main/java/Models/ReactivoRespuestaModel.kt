package Models

import com.android.volley.toolbox.StringRequest
import org.json.JSONArray
import org.json.JSONObject

class ReactivoRespuestaModel {
    var pregunta: String
    var opciones: ArrayList<String>
    var valor: Int
    var tipo: String
    var respuesta: Int

    constructor(pregunta: String, opciones: ArrayList<String>, valor: Int, tipo: String, respuesta: Int) {
        this.pregunta = pregunta
        this.opciones = opciones
        this.valor = valor
        this.tipo = tipo
        this.respuesta = respuesta
    }
}