package andme.core.widget

import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by Lucio on 2021/6/3.
 * 传统跑马灯
 */
class MarqueeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var enableMarquee: Boolean = false

    init {
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.MARQUEE
        //无限循环
        marqueeRepeatLimit = -1
//        //注意，必须要focus才能让第一次滚动，这里没有搞清楚。。。暂时不用这个控件
//        isFocusable = true
    }

    @Deprecated("该方法已被去掉，避免对外修改属性")
    override fun setSelected(selected: Boolean) {

    }

    fun setTexts(contents: List<String>?, separator: String = "                            ") {
        setText(contents?.joinToString(separator).orEmpty())

        //确保设置文本之后跑马灯能动：目前有个问题就是初次运行，跑马灯没动
        if (enableMarquee) {
            stopScroll()
            startScroll()
        }
    }

    override fun isFocused(): Boolean {
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if (focused)
            super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (hasWindowFocus)
            super.onWindowFocusChanged(hasWindowFocus)
    }

    fun startScroll() {
        enableMarquee = true
        //先设置为false，避免与之前标记相同，不会开始跑马灯的问题
        super.setSelected(false)
        super.setSelected(true)
    }

    fun stopScroll() {
        enableMarquee = false
        super.setSelected(false)
    }
}
