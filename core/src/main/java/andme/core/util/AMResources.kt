package andme.core.util

import andme.lang.orDefault
import android.content.Context
import android.view.View
import androidx.annotation.DimenRes

/**
 * Created by Lucio on 2020/12/23.
 */

//returns dip(dp) dimension value in pixels
fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

//return sp dimension value in pixels
fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()

//converts px value into dip or sp
fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity

fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)


//the same for the views
inline fun View.dip(value: Int): Int = context.dip(value)
inline fun View.dip(value: Float): Int = context.dip(value)
inline fun View.sp(value: Int): Int = context.sp(value)
inline fun View.sp(value: Float): Int = context.sp(value)
inline fun View.px2dip(px: Int): Float = context.px2dip(px)
inline fun View.px2sp(px: Int): Float = context.px2sp(px)
inline fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)

//the same for Fragments
inline fun androidx.fragment.app.Fragment.dip(value: Int): Int = activity?.dip(value).orDefault()
inline fun androidx.fragment.app.Fragment.dip(value: Float): Int = activity?.dip(value).orDefault()
inline fun androidx.fragment.app.Fragment.sp(value: Int): Int = activity?.sp(value).orDefault()
inline fun androidx.fragment.app.Fragment.sp(value: Float): Int = activity?.sp(value).orDefault()
inline fun androidx.fragment.app.Fragment.px2dip(px: Int): Float = activity?.px2dip(px).orDefault()
inline fun androidx.fragment.app.Fragment.px2sp(px: Int): Float = activity?.px2sp(px).orDefault()
inline fun androidx.fragment.app.Fragment.dimen(@DimenRes resource: Int):Int = activity?.dimen(resource).orDefault()
