package Models

import org.json.JSONArray
import org.json.JSONObject

class PruebaAbiertaRespuestaModel {
    var nombre_prueba: String
    var reactivos_respuestas: ArrayList<ReactivoRespuestaAbiertaModel>
    var tipo: String
    var clasif: String
    var analisis_tratamiento: String
    var quien_respondio: String

    constructor(nombrePrueba: String, reactivosRespuesta: ArrayList<ReactivoRespuestaAbiertaModel>, tipo: String, clasificacion: String, analisisTratamiento: String, usuario: String) {
        this.nombre_prueba = nombrePrueba
        this.reactivos_respuestas = reactivosRespuesta
        this.tipo = tipo
        this.clasif = clasificacion
        this.quien_respondio = usuario
        this.analisis_tratamiento = analisisTratamiento
    }
}