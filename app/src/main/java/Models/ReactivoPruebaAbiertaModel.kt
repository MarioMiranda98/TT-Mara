package Models

import org.json.JSONObject
import org.json.JSONArray
import java.io.Serializable

class ReactivoPruebaAbiertaModel: Serializable {
    var pregunta: String
    var respuesta: String

    constructor(dataJson: JSONObject) {
        this.pregunta = dataJson.getString("pregunta")
        this.respuesta = dataJson.getString("respuesta")
    }
}