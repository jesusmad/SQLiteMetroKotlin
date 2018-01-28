package net.azarquiel.sqlitemetro.debug
import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by pacopulido on 6/11/17.
 */
class Stetho : Application() {
    override fun onCreate() {
        super.onCreate()
        // depura almacenamiento local BD, share, etc
        Stetho.initializeWithDefaults(this)
    }
}
