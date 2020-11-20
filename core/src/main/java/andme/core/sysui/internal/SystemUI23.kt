package andme.core.sysui.internal

import android.annotation.TargetApi
import android.app.Activity
import android.view.View
import android.view.WindowManager

/**
 * Created by Lucio on 2020-11-15.
 * Android 6.1开始提供了可以控制状态栏模式，以解决状态栏颜色为亮色时的体验（即状态栏背景为白色、状态栏文字也为白色时无法看清楚文字的问题；）
 */
@TargetApi(23)
internal class SystemUI23 : SystemUI21() {

    /**
     * 确保状态栏模式生效：DecorView的setSystemUiVisibility方法添加View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR标志要生效的
     * 前提是：
     * Window添加WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS标志位，并移除WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS标志位
     */
    private fun ensureModeFlagEnable(activity: Activity) {
        activity.window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    override fun setStatusBarLightMode(activity: Activity) {
        super.setStatusBarLightMode(activity)
        ensureModeFlagEnable(activity)
        activity.window.decorView.apply {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun setStatusBarDarkMode(activity: Activity) {
        super.setStatusBarDarkMode(activity)
        activity.window.decorView.apply {
            systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}