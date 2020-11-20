package andme.core.sysui

import android.app.Activity
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils

/**
 * Created by Lucio on 2020-11-15.
 */

interface AMSystemUI {

    /**
     * 设置沉浸式状态栏模式（即状态栏会覆盖布局内容），并且状态栏背景透明
     */
    fun setImmersiveStatusBar(activity: Activity) = setImmersiveStatusBar(activity, Color.TRANSPARENT)

    /**
     * 设置沉浸式状态栏模式（即状态栏会覆盖布局内容）
     * @param color 状态栏背景色
     */
    fun setImmersiveStatusBar(activity: Activity, @ColorInt color: Int) = setImmersiveStatusBar(activity, color, Color.TRANSPARENT, 0f)

    /**
     * 设置沉浸式状态栏模式（即状态栏会覆盖布局内容），并根据给定比率混合两种颜色，将得到的颜色应用到状态栏背景，如果比率为0，则使用[color1],如果比率为0.5,则均匀混合两种颜色，如果比率为1，则使用[color2]
     * @param color1 状态栏背景色1
     * @param color2 状态栏背景色2
     * @param ratio 比率
     */
    fun setImmersiveStatusBar(activity: Activity, @ColorInt color1: Int, @ColorInt color2: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float)

    /**
     * 设置状态栏颜色：状态栏不会覆盖内容布局
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    fun setStatusBarColor(activity: Activity, @ColorInt color: Int) = setStatusBarColor(activity, color, Color.TRANSPARENT, 0f)

    /**
     * 设置状态栏颜色：使用给定的比例在两种ARGB颜色之间进行混合；
     * 如果比率为0，则使用[color1],如果比率为0.5,则均匀混合两种颜色，如果比率为1，则使用[color2]
     * @param activity      需要设置的activity
     * @param color1    颜色1
     * @param color2    颜色2
     * @param ratio     比率，0-1
     */
    fun setStatusBarColor(activity: Activity, @ColorInt color1: Int, @ColorInt color2: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float)

    /**
     * 设置沉浸式导航栏（即导航栏会覆盖内容布局），并且导航栏背景透明
     */
    fun setImmersiveNavigationBar(activity: Activity) = setImmersiveNavigationBar(activity, Color.TRANSPARENT)

    /**
     * 设置沉浸式导航栏（即导航栏会覆盖内容布局）
     * @param color 导航栏背景色
     */
    fun setImmersiveNavigationBar(activity: Activity, @ColorInt color: Int) = setImmersiveNavigationBar(activity, color, Color.TRANSPARENT, 0f)

    /**
     * 设置沉浸式导航栏（即导航栏会覆盖内容布局），并根据给定比率混合两种颜色，将得到的颜色应用到状态栏背景，如果比率为0，则使用[color1],如果比率为0.5,则均匀混合两种颜色，如果比率为1，则使用[color2]
     * @param color1 导航栏背景色1
     * @param color2 导航栏背景色2
     * @param ratio 比率
     */
    fun setImmersiveNavigationBar(activity: Activity, @ColorInt color1: Int, @ColorInt color2: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float)

    /**
     * 设置导航栏颜色
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    fun setNavigationBarColor(activity: Activity, @ColorInt color: Int) = setNavigationBarColor(activity, color, Color.TRANSPARENT, 0f)

    /**
     * 设置Navigation Bar颜色
     * see [setStatusBarColor]
     */
    fun setNavigationBarColor(activity: Activity, @ColorInt color1: Int, @ColorInt color2: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float)

    /**
     * 设置沉浸式系统栏（即状态栏和导航栏会覆盖布局内容），并且状态栏和导航栏背景透明
     */
    fun setImmersiveSystemBar(activity: Activity) = setImmersiveSystemBar(activity, Color.TRANSPARENT)

    /**
     * 设置沉浸式系统栏（即状态栏和导航栏会覆盖布局内容）
     * @param color 系统栏的背景色
     */
    fun setImmersiveSystemBar(activity: Activity, @ColorInt color: Int) = setImmersiveSystemBar(activity, color, Color.TRANSPARENT, 0f)

    /**
     * 设置沉浸式系统栏（即状态栏和导航栏会覆盖布局内容），并根据给定比率混合两种颜色，将得到的颜色应用到状态栏和导航栏背景，如果比率为0，则使用[color1],如果比率为0.5,则均匀混合两种颜色，如果比率为1，则使用[color2]
     * @param color1
     * @param color2
     * @param ratio 比率
     */
    fun setImmersiveSystemBar(activity: Activity, @ColorInt color1: Int, @ColorInt color2: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float)


    /*
     * 设置System Bar的颜色：即同时设置Status Bar和Navigation Bar的背景颜色
     */
    fun setSystemBarColor(activity: Activity, @ColorInt color: Int) = setSystemBarColor(activity, color, Color.TRANSPARENT, 0f)

    /**
     * 设置System Bar的颜色：即同时设置Status Bar和Navigation Bar的背景颜色
     */
    fun setSystemBarColor(activity: Activity, @ColorInt color1: Int, @ColorInt color2: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float)

    /**
     * 设置状态栏为浅色模式：即状态栏背景为浅色、文字为深色（比如黑色）
     */
    fun setStatusBarLightMode(activity: Activity)

    /**
     * 设置状态栏为深色模式：即状态栏背景为深色、文字为浅色（比如白色）
     */
    fun setStatusBarDarkMode(activity: Activity)


}