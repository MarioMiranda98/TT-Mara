package Adapters

import Helpers.NetworkConstants
import Models.ListaPsicologosPruebasModel
import Paquetes.A027.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class PruebasPsicologoAdapter(var context: Context, items: ArrayList<ListaPsicologosPruebasModel>): BaseAdapter() {
    var items: ArrayList<ListaPsicologosPruebasModel>? = null
    var adapter: PruebasPsicologoAdapter? = null

    init {
        this.items = items
        this.adapter = this
    }

    override fun getCount(): Int {
        return items?.count()!!
    }

    override fun getItem(p0: Int): Any {
        return items?.get(p0)!!
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var holder: ViewHolder? = null

        var vista: View? = p1

        if(vista == null) {
            vista = LayoutInflater.from(context).inflate(R.layout.pruebas_psicologo_template, null)
            holder = ViewHolder(vista)
            vista.tag = holder
        } else {
            holder = vista.tag as? ViewHolder
        }

        val item = getItem(p0) as ListaPsicologosPruebasModel
        holder?.nombrePrueba?.text = item.prueba
        holder?.fechaPrueba?.text = item.fechaLimite
        holder?.creadorPrueba?.text = item.asignado
        holder?.status?.text = item.status

        holder?.btnEliminar?.setOnClickListener {
            it: View ->
                val url = NetworkConstants.urlApi + NetworkConstants.borrarPrueba + "?paci=${item.asignado}&trial=${item.prueba}"
                val queue = Volley.newRequestQueue(it.context)
                val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, Response.Listener {
                    response ->
                        if(response.getInt("code") == 200) {
                            Toast.makeText(it.context, "Prueba Eliminada con exito", Toast.LENGTH_LONG).show()
                            val itemSeleccionado = items?.get(p0)
                            items?.remove(itemSeleccionado)
                            adapter?.notifyDataSetChanged()
                        }
                }, Response.ErrorListener {
                    error -> Log.d("Error", error.toString())
                })

                queue.add(jsonObjectRequest)
        }

        return vista!!
    }

    private class ViewHolder(vista: View) {
        var nombrePrueba: TextView? = null
        var fechaPrueba: TextView? = null
        var creadorPrueba: TextView? = null
        var status: TextView? = null
        var btnEliminar: Button? = null

        init {
            nombrePrueba = vista.findViewById(R.id.NombrePrueba)
            fechaPrueba = vista.findViewById(R.id.FechaPrueba)
            creadorPrueba = vista.findViewById(R.id.Creador)
            status = vista.findViewById(R.id.StatusPrueba)
            btnEliminar = vista.findViewById(R.id.EliminarPrueba)
        }
    }
}