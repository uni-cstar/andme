package andme.tv.leanback.widget.viewpager

import andme.core.exception.tryCatch
import andme.core.widget.recyclerView
import andme.core.widget.setGone
import andme.core.widget.setVisible
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Lucio on 2021/5/3.
 */
class ViewPager2ParentConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    /**
     * 用于ViewPager切换页面时清除焦点导致焦点跳跃问题：viewPager2内部注册了OnPageSelected回调，并调用了clearfocus方法，导致切换页面时都会让其他view获取焦点的问题；
     * 因此添加一个隐藏的view用于页面切换时临时获取焦点
     *
     */
    private val fakeFocusView: View
    private val shouldAutoRequestFocus = AtomicBoolean(false)
    private var viewPager2: ViewPager2? = null
    private val listeners = CopyOnWriteArrayList<OnPageCurrentItemChangListener>()

    init {
        fakeFocusView = View(context)
        fakeFocusView.setGone()
        fakeFocusView.isFocusable = true
        fakeFocusView.isFocusableInTouchMode = true

        addView(fakeFocusView, LayoutParams(1, 1))
    }

    /**
     * 设置关联的ViewPager
     */
    fun attachViewPager2(viewPager2: ViewPager2) {
        onViewPagerPrepared(viewPager2)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        tryCatch {
            val viewpager = findViewPager2()
            if (viewpager != null) {
                onViewPagerPrepared(viewpager)
            }
        }
    }

    private fun onViewPagerPrepared(viewPager2: ViewPager2) {
        if (this.viewPager2 == viewPager2)
            return

        if (this.viewPager2 != null) {
            this.viewPager2!!.unregisterOnPageChangeCallback(onPageChangeCallback)
        }
        this.viewPager2 = viewPager2
        this.viewPager2?.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            removeCallbacks(requestPageFocusAction)
            if (shouldAutoRequestFocus.get()) {
                postDelayed(requestPageFocusAction, 250)
            }
        }
    }

    private val requestPageFocusAction = object : Runnable {
        override fun run() {
            tryCatch {
                val viewpager = this@ViewPager2ParentConstraintLayout.viewPager2 ?: return
                val recyclerView = viewpager.recyclerView
                if (shouldAutoRequestFocus.compareAndSet(true, false)) {
                    val currentPosition = viewpager.currentItem
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(currentPosition)
                    if (viewHolder != null) {
                        viewHolder.itemView.requestFocus()
                    } else {
                        if (recyclerView.childCount > 0) {
                            recyclerView.getChildAt(0).requestFocus()
                        } else {
                            viewpager.recyclerView.requestFocus(FOCUS_FORWARD)
                        }
                    }
                    fakeFocusView.setGone()
                }
            }
        }

    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val superHandled = super.dispatchKeyEvent(event)
        Log.d("ViewPager2ParentConstraintLayout", "superHandled ${superHandled}")
        return superHandled || executeKeyEvent(event)
//        val handled = super.dispatchKeyEvent(event)
//        val viewPager = this.viewPager2
//        //如果事件已消耗或者ViewPager为空或者不是第一次按下按键
//        if (handled || viewPager == null || event.action != KeyEvent.ACTION_DOWN)
//            return handled
//
//        val orientation = viewPager.orientation
//        val keyCode = event.keyCode
//        if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && orientation == ViewPager2.ORIENTATION_HORIZONTAL)
//            || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && orientation == ViewPager2.ORIENTATION_VERTICAL)
//        ) {
//            return pageNext(viewPager)
//        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT && orientation == ViewPager2.ORIENTATION_HORIZONTAL)
//            || (keyCode == KeyEvent.KEYCODE_DPAD_UP && orientation == ViewPager2.ORIENTATION_VERTICAL)
//        ) {
//            return pagePrevious(viewPager)
//        }
//        return handled
    }

    private fun executeKeyEvent(event: KeyEvent): Boolean {
        val viewPager = this.viewPager2
        if (viewPager == null || event.action != KeyEvent.ACTION_DOWN || viewPager.recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE)
            return false

        val orientation = viewPager.orientation
        val keyCode = event.keyCode
        if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && orientation == ViewPager2.ORIENTATION_HORIZONTAL)
            || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && orientation == ViewPager2.ORIENTATION_VERTICAL)
        ) {
            return pageNext(viewPager)
        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT && orientation == ViewPager2.ORIENTATION_HORIZONTAL)
            || (keyCode == KeyEvent.KEYCODE_DPAD_UP && orientation == ViewPager2.ORIENTATION_VERTICAL)
        ) {
            return pagePrevious(viewPager)
        }
        return false
    }

    fun registerOnPageCurrentItemChangListener(listener: OnPageCurrentItemChangListener) {
        listeners.add(listener)
    }

    fun removeOnPageCurrentItemChangListener(listener: OnPageCurrentItemChangListener) {
        listeners.remove(listener)
    }

    /**
     *  显示前一页
     */
    private fun pagePrevious(viewPager2: ViewPager2): Boolean {
        val curItem = viewPager2.currentItem
        if (curItem > 0) {
            performPageScroll(viewPager2, curItem - 1)
            return true
        }
        return false
    }

    /**
     * 显示下一页
     */
    private fun pageNext(viewPager2: ViewPager2): Boolean {
        val adapter = viewPager2.adapter ?: return false
        val curItem = viewPager2.currentItem
        if (curItem < adapter.itemCount - 1) {
            performPageScroll(viewPager2, curItem + 1)
            return true
        }
        return false
    }


    fun performPageScroll(viewPager2: ViewPager2, position: Int, smoothScroll: Boolean = true) {
        fakeFocusView.setVisible()
        fakeFocusView.requestFocus()
        shouldAutoRequestFocus.set(true)
        viewPager2.setCurrentItem(position, smoothScroll)
        listeners.forEach {
            it.onPageCurrentItemChanged(position)
        }
    }

    private fun ViewGroup.findViewPager2(): ViewPager2? {
        children.forEach {
            if (it is ViewPager2)
                return it
            if (it is ViewGroup) {
                val rv = it.findViewPager2()
                if (rv != null)
                    return rv
            }
        }
        return null
    }

    interface OnPageCurrentItemChangListener {
        fun onPageCurrentItemChanged(currentItem: Int)
    }
}