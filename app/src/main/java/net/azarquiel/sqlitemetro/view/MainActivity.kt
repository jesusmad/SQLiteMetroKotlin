package net.azarquiel.sqlitemetro.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.animation.OvershootInterpolator
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import net.azarquiel.sqlitemetro.model.Linea
import net.azarquiel.sqlitemetro.R
import net.azarquiel.sqlitemetro.adapter.CustomAdapterLineas
import net.azarquiel.sqlitemetro.adapter.DBAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var db: DBAdapter
    private lateinit var lineas: ArrayList<Linea>
    private lateinit var adapterRV: CustomAdapterLineas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBAdapter(this)

        loadLineas()
        loadAdapter()

    }

    private fun loadLineas() {
        lineas = ArrayList()
        var cursor = db.getLineas()
        var linea: Linea
        for (i in 0..cursor.count - 1) {
            linea = Linea()
            linea.id = cursor.getString(0).toInt()
            linea.nombre = cursor.getString(1)
            linea.color = cursor.getString(2)
            linea.estaciones = db.getEstaciones(linea.id)
            lineas.add(linea)
            cursor.moveToNext()
        }
    }

    private fun loadAdapter() {
        rvLineas.layoutManager = LinearLayoutManager(this)
        adapterRV = CustomAdapterLineas(this, R.layout.row_linea, lineas)


        val scale = ScaleInAnimationAdapter(adapterRV)
        scale.setInterpolator(OvershootInterpolator())
        scale.setFirstOnly(false)
        scale.setDuration(500)
        rvLineas.setAdapter(scale)
    }

}
