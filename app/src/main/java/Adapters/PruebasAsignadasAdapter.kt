package Adapters

import Models.PruebaModel
import Paquetes.A027.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PruebasAsignadasAdapter(var context: Context, items: ArrayList<PruebaModel>): BaseAdapter() {
    var items: ArrayList<PruebaModel>? = null

    init {
        this.items = items
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
            vista = LayoutInflater.from(context).inflate(R.layout.pruebas_asignadas_template, null)
            holder = ViewHolder(vista)
            vista.tag = holder
        } else {
            holder = vista.tag as? ViewHolder
        }

        val item = getItem(p0) as PruebaModel
        holder?.nombrePrueba?.text = item.prueba
        holder?.fechaPrueba?.text = item.fechaLimite
        holder?.creadorPrueba?.text = item.creador
        holder?.status?.text = item.status

       return vista!!
    }

    private class ViewHolder(vista: View) {
        var nombrePrueba: TextView? = null
        var fechaPrueba: TextView? = null
        var creadorPrueba: TextView? = null
        var status: TextView? = null

        init {
            nombrePrueba = vista.findViewById(R.id.NombrePrueba)
            fechaPrueba = vista.findViewById(R.id.FechaPrueba)
            creadorPrueba = vista.findViewById(R.id.Creador)
            status = vista.findViewById(R.id.StatusPrueba)
        }
    }
}