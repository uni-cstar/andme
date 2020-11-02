package andme.arch.app

import andme.arch.BuildConfig
import android.app.Application

/**
 * Created by Lucio on 2020-11-01.
 */
abstract class AMApp : Application() {

    private val _delegate: AMApplicationDelegate = AMApplicationDelegate()

    var isDebuggable: Boolean = BuildConfig.DEBUG
        set(value) {
            _delegate.isDebuggable = value
            field = value
        }

    override fun onCreate() {
        super.onCreate()
        _delegate.onCreate(this,isDebuggable)
    }
}