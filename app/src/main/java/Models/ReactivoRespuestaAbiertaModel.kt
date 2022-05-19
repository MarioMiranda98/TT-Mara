package Models

import java.io.Serializable

class ReactivoRespuestaAbiertaModel: Serializable{
    var pregunta: String
    var respuesta: String

    constructor(pregunta: String, respuesta: String) {
        this.pregunta = pregunta
        this.respuesta = respuesta
    }
}