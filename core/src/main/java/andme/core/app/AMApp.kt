package andme.core.app

import andme.core.appManagerAM
import andme.core.initCore
import android.app.Application

/**
 * Created by Lucio on 2020-11-01.
 */
abstract class AMApp : Application(), AMAppManager by appManagerAM {

    override fun onCreate() {
        super.onCreate()
        initCore(this)
    }
}