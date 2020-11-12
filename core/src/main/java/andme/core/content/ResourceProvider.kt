package andme.core.content

import andme.core.mApp

/**
 * Created by Lucio on 2020-11-09.
 */

val screenWidth: Int by lazy {
    mApp.resources.displayMetrics.widthPixels
}

val screenHeight: Int by lazy {
    mApp.resources.displayMetrics.heightPixels
}

//var colorPrimary:Int

/**
 * 同步资源值：用于换肤或者资源动态更改的情况
 */
fun syncResourcesValues(){

}