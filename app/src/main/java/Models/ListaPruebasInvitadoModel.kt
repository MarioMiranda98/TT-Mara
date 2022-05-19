package Models

import org.json.JSONObject

class ListaPruebasInvitadoModel {
    var prueba: String

    constructor(dataJson: JSONObject) {
        prueba = dataJson.getString("nombre_prueba")
    }
}