package Models

import org.json.JSONObject

class ListaPsicologosPruebasModel {
    var prueba: String
    var fechaLimite: String
    var status: String
    var asignado: String

    constructor(dataJson: JSONObject) {
        prueba = dataJson.getString("prueba")
        fechaLimite = dataJson.getString("fecha_limite")
        status = dataJson.getString("status")
        asignado = dataJson.getString("paciente")
    }
}
