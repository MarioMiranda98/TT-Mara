package Models

import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

class PruebaModel: Serializable {
    var idPrueba: Int
    var nombrePrueba: String
    var reactivos: ArrayList<ReactivoPruebaModel>
    var descripcion: String
    var tipo: String
    var clasificacion: String
    var nota: String
    var creador: String
    var analisisTratamiento: String
    var usuarioRespondio: String

    constructor(dataJson: JSONObject, status: String) {
        this.idPrueba = dataJson.getInt("ID")
        this.nombrePrueba = dataJson.getString("nombre_prueba")
        if(status.equals("Contestada")) {
            this.reactivos = parsearReactivoPruebaModel(dataJson.getString("reactivos_respuestas"), status)
            this.analisisTratamiento = dataJson.getString("analisis_tratamiento")
            this.usuarioRespondio = dataJson.getString("quien_respondio")
            this.nota = ""
            this.descripcion = ""
            this.creador = ""
        } else {
            this.reactivos = parsearReactivoPruebaModel(dataJson.getString("reactivos"), status)
            this.descripcion = dataJson.getString("descripcion")
            this.nota = dataJson.getString("nota")
            this.creador = dataJson.getString("creador_prueba")
            this.analisisTratamiento = ""
            this.usuarioRespondio = ""
        }
        this.tipo = dataJson.getString("tipo")
        this.clasificacion = dataJson.getString("clasif")
    }

    private fun parsearReactivoPruebaModel(dataJson: String, status: String): ArrayList<ReactivoPruebaModel> {
        var lista: ArrayList<ReactivoPruebaModel> = ArrayList<ReactivoPruebaModel>()
        val data = JSONArray(dataJson)

        for(i in 0..data.length() - 1) {
            lista.add(ReactivoPruebaModel(data.getJSONObject(i), status))
        }

        return lista
    }
}