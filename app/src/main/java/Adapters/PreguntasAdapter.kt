package Adapters

import Models.ReactivoPruebaModel
import Paquetes.A027.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.core.view.get
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
        if(contestada) { pintarRespuestas(holder, item) }

        if(!contestada) {
            if(item.respuesta != -1) {
                Log.d("resp", item.respuesta.toString())
                pintarRespuestas2(holder, item)
            }

            val radioMap: HashMap<Int, Int> = guardarTags(holder, item, p0)
            holder?.radioGroup?.setTag(radioMap)

            holder?.radioGroup?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group: RadioGroup?, i: Int ->
                if(holder?.radioGroup?.checkedRadioButtonId != -1) {
                    val pos: Int = (i + (item.opciones.size * p0))
                    val data = group!!.tag as HashMap<Int, Int>
                    item.respuesta = data.get(pos)!!
                }
            })
        }
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

    private fun guardarTags(holder: ViewHolder?, item: ReactivoPruebaModel, posicion: Int): HashMap<Int, Int> {
        val radioMap = HashMap<Int, Int>()
        val numOpciones = item.opciones.size

        if(numOpciones == 4) {
            radioMap[(holder?.respuesta1?.id!! + (numOpciones * posicion))] = item.valor.get(0)
            radioMap[(holder?.respuesta2?.id!! + (numOpciones * posicion))] = item.valor.get(1)
            radioMap[(holder?.respuesta3?.id!! + (numOpciones * posicion))] = item.valor.get(2)
            radioMap[(holder?.respuesta4?.id!! + (numOpciones * posicion))] = item.valor.get(3)
        } else if(numOpciones == 3) {
            radioMap[(holder?.respuesta1?.id!! + (numOpciones * posicion))] = item.valor.get(0)
            radioMap[(holder?.respuesta2?.id!! + (numOpciones * posicion))] = item.valor.get(1)
            radioMap[(holder?.respuesta3?.id!! + (numOpciones * posicion))] = item.valor.get(2)
        } else if(numOpciones == 2) {
            radioMap[(holder?.respuesta1?.id!! + (numOpciones * posicion))] = item.valor.get(0)
            radioMap[(holder?.respuesta2?.id!! + (numOpciones * posicion))] = item.valor.get(1)
        }

        return radioMap
    }

    private fun pintarRespuestas(holder: ViewHolder?, item: ReactivoPruebaModel) {
        val numOpciones = item.opciones.size

        if(numOpciones == 4) {
            holder?.respuesta1?.isChecked = item.respuesta == item.valor.get(0)
            holder?.respuesta1?.isEnabled = false

            holder?.respuesta2?.isChecked = item.respuesta == item.valor.get(1)
            holder?.respuesta2?.isEnabled = false

            holder?.respuesta3?.isChecked = item.respuesta == item.valor.get(2)
            holder?.respuesta3?.isEnabled = false

            holder?.respuesta4?.isChecked = item.respuesta == item.valor.get(3)
            holder?.respuesta4?.isEnabled = false
        } else if (numOpciones == 3) {
            holder?.respuesta1?.isChecked = item.respuesta == item.valor.get(0)
            holder?.respuesta1?.isEnabled = false

            holder?.respuesta2?.isChecked = item.respuesta == item.valor.get(1)
            holder?.respuesta2?.isEnabled = false

            holder?.respuesta3?.isChecked = item.respuesta == item.valor.get(2)
            holder?.respuesta3?.isEnabled = false
        } else if (numOpciones == 2) {
            holder?.respuesta1?.isChecked = item.respuesta == item.valor.get(0)
            holder?.respuesta1?.isEnabled = false

            holder?.respuesta2?.isChecked = item.respuesta == item.valor.get(1)
            holder?.respuesta2?.isEnabled = false
        }
    }

    private fun pintarRespuestas2(holder: ViewHolder?, item: ReactivoPruebaModel) {
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