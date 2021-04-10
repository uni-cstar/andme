package andme.core.binding

import android.view.View
import android.widget.TextView

/**
 * Created by Lucio on 2021/3/26.
 */

inline fun TextView.bindTextOrGone(message: CharSequence?) {
    if (message.isNullOrEmpty()) {
        visibility = View.GONE
        text = ""
    } else {
        visibility = View.VISIBLE
        text = message
    }
}


/**
 * 绑定文本；文本为空时隐藏控件，在文本不为空时并执行[onVisibleInvoke]回调
 */
inline fun TextView.bindTextOrGone(
    message: CharSequence?,
    onVisibleInvoke: TextView.() -> Unit
) {
    if (message.isNullOrEmpty()) {
        visibility = View.GONE
        text = ""
    } else {
        visibility = View.VISIBLE
        text = message
        onVisibleInvoke.invoke(this)
    }
}
