package andme.core.multistate

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Lucio on 2021/3/30.
 */
class FrameStateLayout private constructor(
    private val mDelegate: LayoutDelegate,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MultiStateLayout by mDelegate {

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : this(LayoutDelegate(), context, attrs, defStyleAttr)

    init {
        mDelegate.obtainStyledAttributes(context, this, attrs, defStyleAttr)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mDelegate.showContent()
    }

}