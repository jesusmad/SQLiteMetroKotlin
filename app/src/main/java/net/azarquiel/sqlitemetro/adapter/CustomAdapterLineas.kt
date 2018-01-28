package net.azarquiel.sqlitemetro.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_linea.view.*
import net.azarquiel.sqlitemetro.R
import net.azarquiel.sqlitemetro.model.Linea
import net.azarquiel.sqlitemetro.view.ContentActivity

/**
 * Created by jr on 11-Nov-17.
 */
class CustomAdapterLineas(val context: Context,
                          val layout: Int,
                          val dataList: ArrayList<Linea>) : RecyclerView.Adapter<CustomAdapterLineas.ViewHolder>() {


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

        fun bind(dataItem: Linea, position: Int) {

            itemView.tvLinea.text = dataItem.nombre
            itemView.tvEstIni.text = dataItem.estaciones.first().nombre
            itemView.tvEstFin.text = dataItem.estaciones.last().nombre

            var img = if (dataItem.id in (51..53)) "l51" else "l${dataItem.id}"
            itemView.ivNumLinea.setImageResource(context.resources.getIdentifier(img, "drawable", context.packageName))

//            itemView.layoutLinea.setBackgroundColor(Color.parseColor(dataItem.color))
            itemView.cardViewLinea.setCardBackgroundColor(Color.parseColor(dataItem.color))
            itemView.ivNumLinea.setBackgroundColor(Color.parseColor(dataItem.color))


            itemView.setOnClickListener(View.OnClickListener{
                onItemClick(dataItem)
            })
        }

        private fun onItemClick(dataItem: Linea) {
            val intent = Intent(context as Activity, ContentActivity::class.java)
            intent.putExtra("linea", dataItem)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

    }

}
