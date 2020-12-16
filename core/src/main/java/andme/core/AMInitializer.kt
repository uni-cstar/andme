package andme.core

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.startup.Initializer

/**
 * Created by Lucio on 2020/12/16.
 */
class AMInitializer :Initializer<Unit>{
    override fun create(context: Context) {
        if(context is Application){
            Log.d("AMInitializer","初始化AMCore")
            initCore(context)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

}