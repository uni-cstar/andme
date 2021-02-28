package andme.tv.leanback.widget

import android.view.View

/**
 * Interface for highlighting the item that has focus.
 * 复刻[androidx.leanback.widget.FocusHighlightHandler]
 */
interface FocusHighlightHandler {
    /**
     * Called when an item gains or loses focus.
     * @hide
     *
     * @param view The view whose focus is changing.
     * @param hasFocus True if focus is gained; false otherwise.
     */
    fun onItemFocused(view: View, hasFocus: Boolean)

    /**
     * Called when the view is being created.
     */
    fun onInitializeView(view: View)
}