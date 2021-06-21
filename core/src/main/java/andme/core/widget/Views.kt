package andme.core.widget

import android.view.View

/**
 * Created by Lucio on 2021/4/9.
 */

inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }


inline fun View.setVisibleOrInVisible(value: Boolean) {
    if (value) {
        if(visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
    } else {
        if(visibility != View.INVISIBLE) {
            visibility = View.INVISIBLE
        }
    }
}


inline fun View.setVisible() {
    visibility = View.VISIBLE
}

inline fun View.setInVisible() {
    visibility = View.INVISIBLE
}

inline fun View.setGone() {
    visibility = View.GONE
}