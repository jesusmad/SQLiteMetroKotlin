package net.azarquiel.sqlitemetro.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.animation.OvershootInterpolator
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.activity_content.*
import net.azarquiel.sqlitemetro.R
import net.azarquiel.sqlitemetro.model.Linea
import net.azarquiel.sqlitemetro.adapter.CustomAdapterEstaciones


class ContentActivity : AppCompatActivity() {

    lateinit var linea : Linea
    lateinit var adapterRV: CustomAdapterEstaciones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        linea = intent.getSerializableExtra("linea") as Linea

        loadAdapter()
        loadDatos()
    }

    private fun loadDatos() {
        tvLineaEstacion.text = linea.nombre
        tvEstIniEstacion.text = linea.estaciones.first().nombre
        tvEstFinEstacion.text = linea.estaciones.last().nombre
        var color = Color.parseColor(linea.color)
        layTopEstaciones.setBackgroundColor(color)
        layoutEstaciones.setBackgroundColor(color)
        layoutEstaciones.background.alpha = 128
    }

    private fun loadAdapter() {
        rvEstaciones.layoutManager = LinearLayoutManager(this)
        adapterRV = CustomAdapterEstaciones(this, R.layout.row_estacion, linea.estaciones, linea)

        val alpha = AlphaInAnimationAdapter(adapterRV)
        alpha.setInterpolator(OvershootInterpolator())
        alpha.setFirstOnly(false)
        rvEstaciones.setAdapter(alpha)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}
