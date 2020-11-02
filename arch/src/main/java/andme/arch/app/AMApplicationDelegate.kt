package andme.arch.app

import android.app.Application

/**
 * Created by Lucio on 2020-11-01.
 */

class AMApplicationDelegate {

    internal lateinit var app: Application

    var isDebuggable: Boolean = true
        set(value) {
            field = value
        }

    fun onCreate(app: Application, debuggable: Boolean) {
        this.app = app
        this.isDebuggable = debuggable
    }
}