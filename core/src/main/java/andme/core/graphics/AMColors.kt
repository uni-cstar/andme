package andme.core.graphics

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.graphics.ColorUtils

/**
 * Created by Lucio on 2020-11-15.
 */

/**
 * 根据特定的比率混合两种颜色，如果[ratio]值为0，则返回[color1]，
 * 如果[ratio]值为1则返回[color2],其他情况则会根据[ratio]混合[color1]和[color2]，比如当比率为0.5时均匀混合两种颜色
 * @param color1
 * @param color2
 * @return 混合之后的颜色
 */
inline fun blendARGB(@ColorInt color1: Int, @ColorInt color2: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int {
    return ColorUtils.blendARGB(color1, color2, ratio)
}

/**
 * 计算alpha色值
 *
 * @param color 状态栏颜色值
 * @param alpha 状态栏透明度
 */
fun blendARGB(@ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
    if (alpha == 255) {
        return color
    }

    val ratio = alpha / 255f
    val a = Color.alpha(color)
    val r = Color.red(color)
    val g = Color.green(color)
    val b = Color.blue(color)
    return Color.argb((a * ratio).toInt(), (r * ratio).toInt(), (g * ratio).toInt(), (b * ratio).toInt())
}
