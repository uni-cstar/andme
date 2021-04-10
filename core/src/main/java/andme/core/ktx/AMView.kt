package andme.core.ktx

import andme.lang.orDefault
import android.view.View
import android.view.ViewGroup

/**
 * Created by Lucio on 2019-11-03.
 */

/**
 * 从ViewGroup移除自身
 */
inline fun View?.removeSelf() {
    if (this == null)
        return
    (this.parent as? ViewGroup)?.removeView(this)
}

inline fun View.updateLayoutParamsOrDefault(block: ViewGroup.LayoutParams.() -> Unit) {
    var lp = layoutParams.orDefault {
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    block(lp)
    layoutParams = lp
}

inline fun View.updateLayoutSize(width: Int, height: Int) {
    updateLayoutParamsOrDefault {
        this.width = width
        this.height = height
    }
}


inline fun View.updateLayoutWidth(width: Int) {
    updateLayoutParamsOrDefault {
        if (this.width == width)
            return
        this.width = width
    }
}