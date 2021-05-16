package andme.core.statelayout

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Created by Lucio on 2021/3/30.
 */
class ConstraintStateLayout private constructor(
    private val mDelegate: StateLayoutDelegate,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), StateLayout by mDelegate {

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : this(StateLayoutDelegate(), context, attrs, defStyleAttr)

    init {
        mDelegate.obtainStyledAttributes(context, this, attrs, defStyleAttr)
    }

//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        mDelegate.showContent()
//    }

}