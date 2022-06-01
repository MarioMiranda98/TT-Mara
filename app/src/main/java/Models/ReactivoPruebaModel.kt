package Models

import com.google.gson.Gson
import org.json.JSONObject
import org.json.JSONArray
import java.io.Serializable

class ReactivoPruebaModel: Serializable {
    var pregunta: String
    var opciones: ArrayList<String>
    var valor: ArrayList<Int>
    var tipo: String
    var respuesta: Int
    var status: String
    var respondida: Boolean

    constructor(dataJson: JSONObject, status: String) {
        this.pregunta = dataJson.getString("pregunta")
        this.tipo = dataJson.getString("tipo")
        if(status.equals("Contestada")) {
            this.respuesta = dataJson.getInt("respuesta")
            this.opciones = parsearOpciones(dataJson.getJSONArray("opciones"))
        }
        else {
            this.respuesta = -1
            this.opciones = parsearOpciones(dataJson.getJSONArray("opciones"))
        }
        this.valor = parsearValores(dataJson.getJSONArray("opciones"))
        this.status = status
        this.respondida = false
    }

    private fun parsearOpciones(data: JSONArray): ArrayList<String> {
        var listaOpciones: ArrayList<String> = ArrayList<String>()

        for(i in 0..data.length() -1) {
            listaOpciones.add(data.getJSONObject(i).getString("text"))
        }

        return listaOpciones
    }

    private fun parsearValores(data: JSONArray): ArrayList<Int> {
        var listaValores: ArrayList<Int> = ArrayList<Int>()

        for(i in 0..data.length() -1) {
            val valor: Int = data.getJSONObject(i).getInt("value")
            listaValores.add(valor)
        }

        return listaValores
    }
}