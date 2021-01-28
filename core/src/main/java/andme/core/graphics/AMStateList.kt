package andme.core.graphics

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ColorStateListDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


private fun PressedState():Array<IntArray>{
    return arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf()
    )
}
/**
 * 按下/普通 状态的ColorStateList
 * @param defColor 默认状态颜色
 * @param pressColor 按下状态颜色
 */
fun PressedColorStateList(@ColorInt defColor: Int, @ColorInt pressColor: Int): ColorStateList {
    return ColorStateList(PressedState(), intArrayOf(pressColor, defColor))
}

/**
 * @param defColor 默认状态颜色
 * @param pressColor 按下状态颜色
 */
fun PressedColorStateListDrawable(@ColorInt defColor: Int, @ColorInt pressColor: Int): Drawable {
    if (Build.VERSION.SDK_INT >= 29) {
        return ColorStateListDrawable(PressedColorStateList(defColor, pressColor))
    } else {
        val state = PressedState()
        return StateListDrawable().also {
            it.addState(state[0], ColorDrawable(pressColor))
            it.addState(state[1], ColorDrawable(defColor))
        }
    }
}

/**
 * @param defColorId 默认状态颜色资源id
 * @param pressColorId 按下状态颜色资源id
 */
fun PressedColorStateList(ctx: Context, @ColorRes defColorId: Int, @ColorRes pressColorId: Int): ColorStateList {
    return ColorStateList(PressedState(), intArrayOf(ContextCompat.getColor(ctx, pressColorId), ContextCompat.getColor(ctx, defColorId)))
}

/**
 * @param defColorId 默认状态颜色资源id
 * @param pressColorId 按下状态颜色资源id
 */
fun PressedColorStateListDrawable(ctx: Context, @ColorRes defColorId: Int, @ColorRes pressColorId: Int): Drawable {
    return PressedColorStateListDrawable(ContextCompat.getColor(ctx, defColorId), ContextCompat.getColor(ctx, pressColorId))
}