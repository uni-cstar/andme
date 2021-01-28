package andme.core.text

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt

/**
 * 确保TextView能够响应Span点击
 */
fun TextView.enableClickSpan() {
    if (movementMethod != LinkMovementMethod.getInstance()) {
        movementMethod = LinkMovementMethod.getInstance()
    }
}

/**
 * Span点击回调
 */
typealias OnSpanClick = (String, View) -> Unit

/**
 * Created by Lucio on 2020/11/25.
 * @param content 文本内容
 * @param color click span文本颜色
 * @param onClick 点击回调
 */
class ClickSpan(val content: String, @ColorInt val color: Int, val onClick: OnSpanClick) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        ds.color = color
        ds.isUnderlineText = false
    }

    override fun onClick(widget: View) {
        onClick.invoke(content, widget)
    }
}

fun ClickSpan( content: CharSequence, color: Int,vararg spans: Pair<String, OnSpanClick>): SpannableStringBuilder {
    return SpannableStringBuilder(content).apply {
        spans.forEach {
            val span = it.first
            val onSpanClick = it.second
            if (span.isNotEmpty()) {
                val spanIndex = content.indexOf(span)
                if (spanIndex >= 0) {
                    setSpan(ClickSpan(span, color, onSpanClick), spanIndex, spanIndex + span.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }
}

/**
 * 应用可点击文本
 * @param content 文本内容
 * @param color 可点击部分文本颜色
 * 突出显示的文本以及点击响应的事件处理
 */
fun TextView.applyClickSpan(content: String, @ColorInt color: Int, vararg spans: Pair<String, OnSpanClick>) {
    enableClickSpan()
    //设置点击颜色
    highlightColor = Color.TRANSPARENT
    text = ClickSpan(content,color,*spans)
}



/**
 * 高亮显示
 * @param start 渲染开始位置
 * @param end 渲染结束位置
 * @param color 颜色
 */
fun CharSequence.toHighLight(start: Int, end: Int, @ColorInt color: Int): CharSequence {
    val style = SpannableStringBuilder(this)
    if (start >= 0 && end >= 0 && end >= start) {
        style.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return style

}

/**
 * 高亮显示
 * @param tag 需要高亮的部分
 * @param color 渲染颜色
 * @return
 */
fun CharSequence.toHighLight(tag: String,@ColorInt color: Int): CharSequence {
    if (tag.isEmpty() || this.isEmpty())
        return this
    val start = this.indexOf(tag)
    val end = start + tag.length
    return this.toHighLight(start, end, color)
}

/**
 * Returns whether the given [CharSequence] contains only digits.
 *
 * @see TextUtils.isDigitsOnly
 */
inline fun CharSequence.isDigitsOnly() = TextUtils.isDigitsOnly(this)