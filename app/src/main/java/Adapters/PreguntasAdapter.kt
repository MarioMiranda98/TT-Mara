package Adapters

import Models.ReactivoPruebaModel
import Paquetes.A027.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.isVisible

class PreguntasAdapter(var context: Context, items: ArrayList<ReactivoPruebaModel>): BaseAdapter() {
    var items: ArrayList<ReactivoPruebaModel>? = null

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
        var holder: PreguntasAdapter.ViewHolder? = null

        var vista: View? = p1

        if(vista == null) {
            vista = LayoutInflater.from(context).inflate(R.layout.preguntas_likert_template, null)
            holder = PreguntasAdapter.ViewHolder(vista)
            vista.tag = holder
        } else {
            holder = vista.tag as? PreguntasAdapter.ViewHolder
        }

        val item = getItem(p0) as ReactivoPruebaModel
        var contestada: Boolean
        if(item.status.equals("Contestada")) { contestada = true}
        else { contestada = false }

        pintarReactivos(holder, item)
        pintarRespuestas(holder, item, contestada)

        return vista!!
    }

    private fun pintarReactivos(holder: ViewHolder?, item: ReactivoPruebaModel) {
        holder?.pregunta?.text = item.pregunta
        val numOpciones = item.opciones.size

        if(numOpciones == 4) {
            holder?.respuesta1?.text = item.opciones.get(0)
            holder?.respuesta2?.text = item.opciones.get(1)
            holder?.respuesta3?.text = item.opciones.get(2)
            holder?.respuesta4?.text = item.opciones.get(3)
        } else if (numOpciones == 3) {
            holder?.respuesta1?.text = item.opciones.get(0)
            holder?.respuesta2?.text = item.opciones.get(1)
            holder?.respuesta3?.text = item.opciones.get(2)
            holder?.respuesta4?.isVisible = false
        } else if (numOpciones == 2) {
            holder?.respuesta1?.text = item.opciones.get(0)
            holder?.respuesta2?.text = item.opciones.get(1)
            holder?.respuesta3?.isVisible = false
            holder?.respuesta4?.isVisible = false
        }
    }

    private fun pintarRespuestas(holder: ViewHolder?, item: ReactivoPruebaModel, contestada: Boolean) {
        if (!contestada) return
        val numOpciones = item.opciones.size

        if(numOpciones == 4) {
            holder?.respuesta1?.isChecked = item.respuesta == item.valor.get(0)
            holder?.respuesta2?.isChecked = item.respuesta == item.valor.get(1)
            holder?.respuesta3?.isChecked = item.respuesta == item.valor.get(2)
            holder?.respuesta4?.isChecked = item.respuesta == item.valor.get(3)
        } else if (numOpciones == 3) {
            holder?.respuesta1?.isChecked = item.respuesta == item.valor.get(0)
            holder?.respuesta2?.isChecked = item.respuesta == item.valor.get(1)
            holder?.respuesta3?.isChecked = item.respuesta == item.valor.get(2)
        } else if (numOpciones == 2) {
            holder?.respuesta1?.isChecked = item.respuesta == item.valor.get(0)
            holder?.respuesta2?.isChecked = item.respuesta == item.valor.get(1)
        }
    }

    private class ViewHolder(vista: View) {
        var pregunta: TextView? = null
        var respuesta1: RadioButton? = null
        var respuesta2: RadioButton? = null
        var respuesta3: RadioButton? = null
        var respuesta4: RadioButton? = null
        var radioGroup: RadioGroup? = null

        init {
            pregunta = vista.findViewById(R.id.PreguntaTv)
            respuesta1 = vista.findViewById(R.id.radioRes1)
            respuesta2 = vista.findViewById(R.id.radioRes2)
            respuesta3 = vista.findViewById(R.id.radioRes3)
            respuesta4 = vista.findViewById(R.id.radioRes4)
            radioGroup = vista.findViewById(R.id.radioGroup)
        }
    }
}