package andme.core.sysui.internal

import andme.core.sysui.AMSystemUI
import andme.core.sysui.blendColor
import android.annotation.TargetApi
import android.app.Activity
import android.view.View
import android.view.WindowManager

/**
 * Created by Lucio on 2020-11-15.
 * Android 5.1 以上 提供了设置System Bar颜色的方法，但是必须配合标志位使用才行；
 * 可以通过代码实现，也可以通过Style实现；
 *
 * 核心原理：
 * Window添加WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS标志位，并移除WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS标志位
 * 这样设置之后，System Bar所在的区域会变成透明，并且会使用{@link Window#getStatusBarColor()} 和 {@link Window#getNavigationBarColor()}填充对应区域的颜色
 */
@TargetApi(21)
internal open class SystemUI21 : AMSystemUI {

    override fun setImmersiveStatusBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        //更改状态栏颜色
        setStatusBarColor(activity, color1, color2, ratio)
        //使用沉浸式状态栏模式
        activity.window.decorView.apply {
            val newSysUIFlag = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (systemUiVisibility != newSysUIFlag) {
                systemUiVisibility = newSysUIFlag
            }
        }
    }

    /**
     *  代码设置状态栏颜色
     *  getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
     *  getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
     *  getWindow().setStatusBarColor(ContextCompat.getColor(this,android.R.color.holo_blue_dark));
     *  */
    /**
     * 通过主题Style设置，添加以下属性（因为是android5.0新添加的属性，所以在添加到values-v21文件夹下的主题中）；
     * <item name="android:windowTranslucentStatus">false</item>
     * <item name="android:windowDrawsSystemBarBackgrounds">true</item>
     * <item name="android:statusBarColor">@android:color/holo_blue_dark</item>
     */
    override fun setStatusBarColor(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        val blendColor = blendColor(activity, color1, color2, ratio)
        activity.window.apply {
            //添加关键标志位--状态栏和NavigationBar颜色设置的核心
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //必须清除这个标志才能使设置的状态栏颜色生效
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置状态栏颜色
            statusBarColor = blendColor
        }
    }

    /**
     * 设置NavigationBar的颜色；具体用法参见[android.view.Window.setNavigationBarColor]说明
     */
    override fun setNavigationBarColor(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        val blendColor = blendColor(activity, color1, color2, ratio)
        //5.1以上透明状态栏设置的
        activity.window.apply {
            //添加关键标志位--状态栏和NavigationBar颜色设置的核心
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //必须清除这个标志才能使设置的NavigationBarColor生效
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            navigationBarColor = blendColor
        }
    }

    override fun setImmersiveNavigationBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        setNavigationBarColor(activity, color1, color2, ratio)
        activity.window.decorView.apply {
            val newSysUIFlag = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            if (systemUiVisibility != newSysUIFlag) {
                systemUiVisibility = newSysUIFlag
            }
        }
    }

    override fun setImmersiveSystemBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        setImmersiveStatusBar(activity, color1, color2, ratio)
        setImmersiveNavigationBar(activity, color1, color2, ratio)
    }

    override fun setSystemBarColor(activity: Activity, color1: Int, color2: Int, ratio: Float) {
        val blendColor = blendColor(activity, color1, color2, ratio)
        activity.window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            statusBarColor = blendColor
            navigationBarColor = blendColor
        }
    }


    override fun setStatusBarLightMode(activity: Activity) {
        //not support
    }

    override fun setStatusBarDarkMode(activity: Activity) {
        //not support
    }
}