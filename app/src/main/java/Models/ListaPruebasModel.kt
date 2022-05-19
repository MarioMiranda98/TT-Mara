package Models

import org.json.JSONObject

class ListaPruebasModel {
    var prueba: String
    var fechaLimite: String
    var status: String
    var creador: String

    constructor(dataJson: JSONObject) {
        prueba = dataJson.getString("prueba")
        fechaLimite = dataJson.getString("fecha_limite")
        status = dataJson.getString("status")
        creador = dataJson.getString("creador")
    }
}
