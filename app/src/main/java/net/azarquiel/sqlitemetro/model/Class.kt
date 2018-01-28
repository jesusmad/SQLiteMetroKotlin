package net.azarquiel.sqlitemetro.model

import java.io.Serializable

/**
 * Created by jr on 17-Nov-17.
 */
data class Linea(var id: Int = -1, var nombre: String = "", var color: String = "", var estaciones: ArrayList<Estacion> = ArrayList<Estacion>()) : Serializable
data class Estacion(var nombre: String = "") : Serializable