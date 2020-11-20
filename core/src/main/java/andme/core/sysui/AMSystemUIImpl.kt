package andme.core.sysui

import andme.core.sysui.internal.DefaultSystemUI
import andme.core.sysui.internal.SystemUI19
import andme.core.sysui.internal.SystemUI21
import andme.core.sysui.internal.SystemUI23
import android.app.Activity
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils


/**
 * 混合颜色
 */
internal fun blendColor(activity: Activity, @ColorInt color1: Int, @ColorInt color2: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int {
    return ColorUtils.blendARGB(color1, color2, ratio)
}


private val impl: AMSystemUI by lazy {
    val sdkInt = Build.VERSION.SDK_INT
    when {
        sdkInt >= 23 -> SystemUI23()
        sdkInt >= 21 -> SystemUI21()
        sdkInt >= 19 -> SystemUI19()
        else -> DefaultSystemUI()
    }
}

/**
 * Created by Lucio on 2019/6/6.
 */

internal object AMSystemUIImpl : AMSystemUI by impl
