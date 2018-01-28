package net.azarquiel.sqlitemetro.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.row_estacion.view.*
import net.azarquiel.sqlitemetro.R
import net.azarquiel.sqlitemetro.model.Estacion
import net.azarquiel.sqlitemetro.model.Linea
import net.azarquiel.sqlitemetro.model.RoundedImageView

/**
 * Created by jr on 11-Nov-17.
 */
class CustomAdapterEstaciones(val context: Context,
                              val layout: Int,
                              val dataList: ArrayList<Estacion>,
                              val linea: Linea) : RecyclerView.Adapter<CustomAdapterEstaciones.ViewHolder>() {

    lateinit var db : DBAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {

        fun bind(dataItem: Estacion, position: Int) {

            itemView.tvEstacion.text = dataItem.nombre
            itemView.cardViewEstacion.setCardBackgroundColor(Color.parseColor(linea.color))

            //Añadir imagen de lineas que pasan por esa estación
            //Se añaden a un Layout, hay que elminar su contenido por cada vuelta
            itemView.layoutLineasPaso.removeAllViews()

            val lp = getDimensionImagen()

            db = DBAdapter(context)
            val cursor = db.getLineasDePasoPorEstacion(dataItem.nombre, linea.id)
            for (i in 0..cursor.count -1) {
                val id = cursor.getString(0).toInt()
                val color = cursor.getString(2)
//                var iv = ImageView(context)
                var iv = ImageView(context)
                iv.layoutParams = lp

                var nombreimg = if (id in (51..53)) "l51" else "l$id"
                iv.setImageResource(context.resources.getIdentifier(nombreimg, "drawable", context.packageName))
                iv.setBackgroundColor(Color.parseColor(color))
                itemView.layoutLineasPaso.addView(iv)

                cursor.moveToNext()
            }
        }
    }

    private fun getDimensionImagen() : LinearLayout.LayoutParams {
        val height = (context.resources.getDimension(R.dimen.height)).toInt()
        val width = (context.resources.getDimension(R.dimen.width)).toInt()
        var lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams( width, height)
        lp.setMargins(0, 0, 5, 0)
        return lp
    }


}



