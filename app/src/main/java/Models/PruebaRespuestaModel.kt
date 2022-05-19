package Models

import java.io.Serializable

class PruebaRespuestaModel: Serializable {
        var nombre_prueba: String
        var reactivos_respuestas: ArrayList<ReactivoRespuestaModel>
        var tipo: String
        var clasif: String
        var analisis_tratamiento: String
        var quien_respondio: String

        constructor(nombrePrueba: String, reactivosRespuesta: ArrayList<ReactivoRespuestaModel>, tipo: String, clasif: String, analisisTratamiento: String, quienRespondio: String) {
            this.nombre_prueba = nombrePrueba
            this.reactivos_respuestas = reactivosRespuesta
            this.analisis_tratamiento = analisisTratamiento
            this.quien_respondio = quienRespondio
            this.tipo = tipo
            this.clasif = clasif
        }

}