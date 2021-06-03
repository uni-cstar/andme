package andme.core.widget

import android.content.Context
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

    init {
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.MARQUEE
        //无限循环
        marqueeRepeatLimit = -1
//        setTextIsSelectable(true)
        super.setSelected(true)
    }

    @Deprecated("该方法已被去掉，避免对外修改属性")
    override fun setSelected(selected: Boolean) {

    }

    fun setTexts(contents: List<String>?, separator: String = "                            ") {
        setText(contents?.joinToString(separator).orEmpty())
    }

    fun startScroll(){
        super.setSelected(true)
    }

    fun stopScroll(){
        super.setSelected(false)
    }
}
