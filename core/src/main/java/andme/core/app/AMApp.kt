package andme.core.app

import andme.core.app.internal.AppManagerImpl
import andme.core.appManagerAM
import android.app.Application

/**
 * Created by Lucio on 2020-11-01.
 */
abstract class AMApp : Application(), AMAppManager by appManagerAM {

    override fun onCreate() {
        super.onCreate()
        //初始化管理器
        AppManagerImpl.init(this)
    }

}