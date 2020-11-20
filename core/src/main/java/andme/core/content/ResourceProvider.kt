package andme.core.content

import andme.core.mApp
import andme.core.sysui.FakeStatusBar

/**
 * Created by Lucio on 2020-11-09.
 */

/**
 * 屏幕宽度
 */
inline val screenWidth: Int get() = mApp.resources.displayMetrics.widthPixels

/**
 * 屏幕高度
 */
inline val screenHeight: Int get() = mApp.resources.displayMetrics.heightPixels

/**
 * 状态栏高度
 */
val statusBarHeight: Int by lazy {
    FakeStatusBar.getAvailableStatusBarHeight(mApp)
}



/**
 * 同步资源值：用于换肤或者资源动态更改的情况
 */
fun syncResourcesValues(){

}