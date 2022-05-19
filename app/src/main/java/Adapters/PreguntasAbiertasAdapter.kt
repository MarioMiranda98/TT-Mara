package Adapters

import Models.ReactivoPruebaAbiertaModel
import Paquetes.A027.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PreguntasAbiertasAdapter(var context: Context, items: ArrayList<ReactivoPruebaAbiertaModel>): BaseAdapter() {
    var items: ArrayList<ReactivoPruebaAbiertaModel>? = null

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
        var holder: PreguntasAbiertasAdapter.ViewHolder? = null

        var vista: View? = p1

        if(vista == null) {
            vista = LayoutInflater.from(context).inflate(R.layout.preguntas_abiertas_template, null)
            holder = PreguntasAbiertasAdapter.ViewHolder(vista)
            vista.tag = holder
        } else {
            holder = vista.tag as? PreguntasAbiertasAdapter.ViewHolder
        }

        val item = getItem(p0) as ReactivoPruebaAbiertaModel
        holder?.pregunta?.text = item.pregunta


        return vista!!
    }

    private class ViewHolder(vista: View) {
        var pregunta: TextView? = null

        init {
            pregunta = vista.findViewById(R.id.preguntaTv)
        }
    }
}