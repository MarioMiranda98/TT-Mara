package Models

import org.json.JSONObject
import org.json.JSONArray
import java.io.Serializable

class ReactivoPruebaAbiertaModel: Serializable {
    var pregunta: String
    var respuesta: String
    var status: String

    constructor(dataJson: JSONObject, status: String) {
        this.pregunta = dataJson.getString("pregunta")
        this.respuesta = dataJson.getString("respuesta")
        this.status = status
    }
}