package net.azarquiel.sqlitemetro.adapter

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import net.azarquiel.sqlitemetro.model.Estacion
import java.io.*

/**
 * Created by jr on 11-Nov-17.
 */
class DBAdapter(private val context: Context) {
    companion object {
        private val DATABASE = "MetroMadrid.sqlite"
        private val VERSION = 1

        private val CREATE_TABLE_ESTACION = "CREATE TABLE estacion (ne integer PRIMARY KEY , nb varchar(100), l1 integer)"
        private val CREATE_TABLE_ESTACIONSERVICIO = "CREATE TABLE estacionservicio (e integer, c integer, Primary Key (e, c))"
        private val CREATE_TABLE_LINEA = "CREATE TABLE linea (id integer PRIMARY KEY , n varchar(50), c varchar(50))"
        private val CREATE_TABLE_SERVICIO = "CREATE TABLE servicio ( id integer PRIMARY KEY , n varchar(100), d varchar(1000))"
    }

    private var db: SQLiteDatabase? = null
    private val DBHelper: DatabaseHelper

    private class DatabaseHelper internal constructor(context: Context) : SQLiteOpenHelper(context, DATABASE, null, VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            try {
                db.execSQL(CREATE_TABLE_ESTACION)
                db.execSQL(CREATE_TABLE_ESTACIONSERVICIO)
                db.execSQL(CREATE_TABLE_LINEA)
                db.execSQL(CREATE_TABLE_SERVICIO)
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS estacion")
            db.execSQL("DROP TABLE IF EXISTS estacionservicio")
            db.execSQL("DROP TABLE IF EXISTS linea")
            db.execSQL("DROP TABLE IF EXISTS servicio")
            onCreate(db)
        }
    }

    init {
        DBHelper = DatabaseHelper(context)
        importar()
    }

    fun importar() {
        val ruta = "/data/data/" + context.packageName + "/databases"
        var fichero = File(ruta + "/" + DATABASE)

        if (!fichero.exists()) {
            fichero = File(ruta)
            fichero.mkdir()
            copiar()
        }
    }
    fun copiar() {
        val ruta = ("/data/data/" + context.packageName
                + "/databases/" + DATABASE)
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {
            `in` = context.assets.open(DATABASE)
            out = FileOutputStream(ruta)
            copyFile(`in`, out)
            `in`!!.close()
            out!!.close()
        } catch (e: IOException) {
            Log.e("**Jesus**", "Fallo en la copia de la BD del asset", e)
        }
    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream?, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read= `in`!!.read(buffer)
        while (read!= -1) {
            out.write(buffer, 0, read)
            read= `in`!!.read(buffer)
        }
    }


    // OPEN DB
    @Throws(SQLException::class)
    fun openWrite() {
        db = DBHelper.writableDatabase
    }

    @Throws(SQLException::class)
    fun openRead() {
        db = DBHelper.readableDatabase
    }

    // CLOSE DB
    fun close() {
        DBHelper.close()
    }


    fun getLineas(): Cursor {
        openRead()
        val cursor = db!!.query("linea", arrayOf("id", "n", "c"), null, null, null, null, "id", null)
        cursor.moveToFirst()
        close()
        return cursor
    }

    fun getEstaciones(idLinea: Int) : ArrayList<Estacion> {
        var estaciones = ArrayList<Estacion>()
        openRead()
        val cursor = db!!.query("estacion", arrayOf("nb"), "l1 = " + idLinea, null, null, null, "ne", null)
        cursor.moveToFirst()
        var estacion: Estacion
        for (i in 0..cursor.count -1) {
            estacion = Estacion()
            estacion.nombre = cursor.getString(0)
            estaciones.add(estacion)
            cursor.moveToNext()
        }
        close()
        return estaciones
    }

    fun getLineasDePasoPorEstacion(estacion: String, idLinea: Int) : Cursor {
        val sql = "SELECT * FROM linea WHERE id IN (SELECT l1 FROM estacion WHERE nb = '" + estacion + "' AND l1 != " + idLinea + ")"
        openRead()
        val cursor = db!!.rawQuery(sql, null)
        cursor.moveToFirst()
        close()
        return cursor
    }

    fun getPrimeraYUltimaEstacion(idLinea: Int) : Cursor {
        val sql = "SELECT nb FROM estacion WHERE ne = " +
                " (SELECT MAX(ne) FROM estacion WHERE l1 = " + idLinea +
                " OR ne = (SELECT MIN(ne) FROM estacion WHERE l1 = " + idLinea

        openRead()
        val cursor = db!!.rawQuery(sql, null)
        cursor.moveToFirst()
        close()
        return cursor
    }

}