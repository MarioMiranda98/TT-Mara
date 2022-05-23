package Models

class ReactivoRespuestaModel {
    var pregunta: String
    var opciones: ArrayList<OpcionesRespuestaModel>
    var tipo: String
    var respuesta: Int

    constructor(pregunta: String, opciones: ArrayList<OpcionesRespuestaModel>, tipo: String, respuesta: Int) {
        this.pregunta = pregunta
        this.opciones = opciones
        this.tipo = tipo
        this.respuesta = respuesta
    }
}