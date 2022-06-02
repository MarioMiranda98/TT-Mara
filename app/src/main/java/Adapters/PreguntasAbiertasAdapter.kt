package Adapters

import Models.ReactivoPruebaAbiertaModel
import Paquetes.A027.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
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

        if(item.status.equals("Contestada")) {
            holder?.campo?.setText(item.respuesta)
            holder?.campo?.isEnabled = false
        }

        if(item.respuesta.length > 0) {
            Log.d("resp", item.respuesta.toString())
            holder?.campo?.setText(item.respuesta)
        }

        holder?.campo?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                item.respuesta = holder?.campo?.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                return
            }

        })

        return vista!!
    }

    private class ViewHolder(vista: View) {
        var pregunta: TextView? = null
        var campo: EditText? = null

        init {
            pregunta = vista.findViewById(R.id.preguntaTv)
            campo = vista.findViewById(R.id.campoEditText)
        }
    }
}