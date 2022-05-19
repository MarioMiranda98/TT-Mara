package Adapters

import Models.ListaPruebasInvitadoModel
import Paquetes.A027.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PruebasInvitadoAdapter(var context: Context, items: ArrayList<ListaPruebasInvitadoModel>): BaseAdapter() {
    var items: ArrayList<ListaPruebasInvitadoModel>? = null

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
            vista = LayoutInflater.from(context).inflate(R.layout.pruebas_invitado_template, null)
            holder = ViewHolder(vista)
            vista.tag = holder
        } else {
            holder = vista.tag as? ViewHolder
        }

        val item = getItem(p0) as ListaPruebasInvitadoModel
        holder?.nombrePrueba?.text = item.prueba

        return vista!!
    }

    private class ViewHolder(vista: View) {
        var nombrePrueba: TextView? = null

        init {
            nombrePrueba = vista.findViewById(R.id.NombrePruebaInivitado)
        }
    }
}