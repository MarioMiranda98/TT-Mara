package Models

import org.json.JSONObject

class OpcionesRespuestaModel {
    var text: String
    var value: Int

    constructor(dataJson: JSONObject) {
        this.text = dataJson.getString("text")
        this.value = dataJson.getInt("value")
    }

    constructor(text: String, value: Int) {
        this.text = text
        this.value = value
    }
}