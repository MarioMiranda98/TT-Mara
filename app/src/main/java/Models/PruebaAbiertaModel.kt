package Models

import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

class PruebaAbiertaModel: Serializable {
    var idPrueba: Int
    var nombrePrueba: String
    var reactivos: ArrayList<ReactivoPruebaAbiertaModel>
    var descripcion: String
    var tipo: String
    var clasificacion: String
    var nota: String
    var creador: String
    var analisisTratamiento: String

    constructor(dataJson: JSONObject, status: String) {
        this.idPrueba = dataJson.getInt("ID")
        this.nombrePrueba = dataJson.getString("nombre_prueba")
        this.tipo = dataJson.getString("tipo")
        this.clasificacion = dataJson.getString("clasif")
        if(status.equals("Pendiente")) {
            this.reactivos = parsearReactivoPruebaModel(dataJson.getString("reactivos"), status)
            this.descripcion = dataJson.getString("descripcion")
            this.nota = dataJson.getString("nota")
            this.creador = dataJson.getString("creador_prueba")
            this.analisisTratamiento = ""
        } else {
            this.reactivos = parsearReactivoPruebaModel(dataJson.getString("reactivos_respuestas"), status)
            this.descripcion = ""
            this.nota = ""
            this.creador = ""
            this.analisisTratamiento = dataJson.getString("analisis_tratamiento")
        }
    }

    private fun parsearReactivoPruebaModel(dataJson: String, status: String): ArrayList<ReactivoPruebaAbiertaModel> {
        var lista: ArrayList<ReactivoPruebaAbiertaModel> = ArrayList<ReactivoPruebaAbiertaModel>()
        val data = JSONArray(dataJson)

        for(i in 0..data.length() - 1) {
            lista.add(ReactivoPruebaAbiertaModel(data.getJSONObject(i), status))
        }

        return lista
    }
}