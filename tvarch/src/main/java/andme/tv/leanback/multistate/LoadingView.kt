package andme.tv.leanback.multistate

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar

/**
 * Created by Lucio on 2021/3/3.
 */
class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mLoadingView: ProgressBar

    init {
        mLoadingView = ProgressBar(context)
        addView(
            mLoadingView,
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).also {
                it.gravity = Gravity.CENTER
            })

    }

    fun show() {
        this.bringToFront()
        this.visibility = View.VISIBLE
    }

    fun hide(){
        this.visibility = View.GONE
    }
}