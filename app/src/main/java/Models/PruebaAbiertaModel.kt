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
    var status: String

    constructor(dataJson: JSONObject) {
        this.idPrueba = dataJson.getInt("ID")
        this.nombrePrueba = dataJson.getString("nombre_prueba")
        this.reactivos = parsearReactivoPruebaModel(dataJson.getString("reactivos"))
        this.descripcion = dataJson.getString("descripcion")
        this.tipo = dataJson.getString("tipo")
        this.clasificacion = dataJson.getString("clasif")
        this.nota = dataJson.getString("nota")
        this.creador = dataJson.getString("creador_prueba")
        this.status = ""
    }

    private fun parsearReactivoPruebaModel(dataJson: String): ArrayList<ReactivoPruebaAbiertaModel> {
        var lista: ArrayList<ReactivoPruebaAbiertaModel> = ArrayList<ReactivoPruebaAbiertaModel>()
        val data = JSONArray(dataJson)

        for(i in 0..data.length() - 1) {
            lista.add(ReactivoPruebaAbiertaModel(data.getJSONObject(i)))
        }

        return lista
    }
}