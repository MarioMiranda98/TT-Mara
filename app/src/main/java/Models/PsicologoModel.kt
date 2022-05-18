package Models

import org.json.JSONObject

class PsicologoModelRegistro {
    var nombreUsuario: String
    var nombre: String
    var apellidos: String

    constructor(dataFromJson: JSONObject) {
        this.nombreUsuario = dataFromJson.getString("nombre_usuario")
        this.nombre = dataFromJson.getString("nombre")
        this.apellidos = dataFromJson.getString("apellidos")
    }
}