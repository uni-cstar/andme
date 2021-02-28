package andme.tv

import android.app.Application

/**
 * Created by Lucio on 2021/2/27.
 */
object AMTV {

    /**
     * 是否支持触摸获取焦点（手机操作行为），默认不支持（即tv不支持触摸操作）
     */
    var isFocusableInTouchMode = false



    @JvmStatic
    fun init(application: Application){
        mApp = application
    }
}

internal lateinit var mApp:Application