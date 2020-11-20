package andme.core.sysui.internal

import andme.core.R
import andme.core.sysui.AMSystemUI
import andme.core.sysui.FakeStatusBar
import andme.core.sysui.blendColor
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

/**
 * Created by Lucio on 2020-11-15.
 */
internal class SystemUI19 : AMSystemUI {

    private val fakeStatusBarId = R.id.am_id_fake_status_bar

    /**
     * 查找或添加一个新的模拟的状态栏
     */
    private fun findOrAddNewFakeStatusBar(activity: Activity): FakeStatusBar {
        val decorView = activity.window.decorView as ViewGroup
        var statusBarView: View? = decorView.findViewById(fakeStatusBarId)
        if (statusBarView == null || statusBarView !is FakeStatusBar) {
            statusBarView = FakeStatusBar(activity).apply {
                id = fakeStatusBarId
            }
            decorView.addView(statusBarView)
        }

        if (statusBarView.visibility != View.VISIBLE) {
            statusBarView.visibility = View.VISIBLE
        }
        return statusBarView
    }

    override fun setImmersiveStatusBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        doImmersiveStatusBar(activity, color1, color2, ratio)
    }

    override fun setStatusBarColor(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        doImmersiveStatusBar(activity, color1, color2, ratio)
        val rootView = (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rootView.fitsSystemWindows = true
    }

    /**
     * 4.4只支持透明状态栏，即状态栏覆盖内容布局，为了达到改变状态栏背景色的效果，所以增加一个与状态栏等高的View渲染背景
     */
    private fun doImmersiveStatusBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        //透明状态栏
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //查找或添加模拟的状态栏View
        val statusBar = findOrAddNewFakeStatusBar(activity)
        //计算显示的颜色
        val blendColor = blendColor(activity, color1, color2, ratio)
        statusBar.setBackgroundColor(blendColor)
    }

    override fun setImmersiveNavigationBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

    override fun setNavigationBarColor(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        //不支持，不过有人使用修改状态栏颜色的方式创建一个FakeNavigationBar来实现，经过考虑，我觉得这不是常态需求，如果真的需要实现这样的效果，在需要的时候单独针对性实现即可；
    }

    override fun setImmersiveSystemBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        setImmersiveStatusBar(activity, color1, color2, ratio)
        setImmersiveNavigationBar(activity, color1, color2, ratio)
    }

    override fun setSystemBarColor(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        setStatusBarColor(activity, color1, color2, ratio)
        setNavigationBarColor(activity, color1, color2, ratio)
    }

    override fun setStatusBarLightMode(activity: Activity) {
        //not support
    }

    override fun setStatusBarDarkMode(activity: Activity) {
        //not support
    }
}