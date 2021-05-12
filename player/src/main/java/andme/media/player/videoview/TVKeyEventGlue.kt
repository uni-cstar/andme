//package andme.media.player.videoview
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.util.AttributeSet
//import android.view.KeyEvent
//import android.widget.FrameLayout
//
///**
// * Created by Lucio on 2021/4/21.
// */
//class TVKeyEventGlue @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//) : FrameLayout(context, attrs, defStyleAttr) {
//
//    /**
//     * 是否禁用事件分发
//     */
//    protected var enableDispatchKeyEvent:Boolean = true
//
//    init {
//        isFocusable = true
//        isFocusableInTouchMode = true
//        requestFocus()
//    }
//
//    /**
//     * 是否是关注的按键
//     */
//    protected open fun isDpadKey(keyCode: Int): Boolean {
//        return keyCode == KeyEvent.KEYCODE_DPAD_UP
//                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
//                || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
//                || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
//                || keyCode == KeyEvent.KEYCODE_DPAD_CENTER
//    }
//
//    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//        if(!enableDispatchKeyEvent)
//            return super.dispatchKeyEvent(event)
//
//        if (player != null && player.isPlayingAd()) {
//            return super.dispatchKeyEvent(event)
//        }
//        val isDpadKey: Boolean = isDpadKey(event.keyCode)
//        var handled = false
//        if (isDpadKey && useController() && !controller.isVisible()) {
//            // Handle the key event by showing the controller.
//            maybeShowController(true)
//            handled = true
//        } else if (dispatchMediaKeyEvent(event) || super.dispatchKeyEvent(event)) {
//            // The key event was handled as a media key or by the super class. We should also show the
//            // controller, or extend its show timeout if already visible.
//            maybeShowController(true)
//            handled = true
//        } else if (isDpadKey && useController()) {
//            // The key event wasn't handled, but we should extend the controller's show timeout.
//            maybeShowController(true)
//        }
//        return handled
//    }
//}