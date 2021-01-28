package andme.arch.refresh.scene

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Created by Lucio on 2020/12/17.
 */
class AMRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mParent: AMSwipeRefreshBRVAHLayout? = null

    internal fun setSwipeRefreshBRVAHLayoutParent(parent: AMSwipeRefreshBRVAHLayout?) {
        this.mParent = parent
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        mParent?.also {
            if (adapter is BaseQuickAdapter<*, *>) {
                it.attachAdapter(adapter)
            } else {
                it.detachAdapter()
            }
        }
    }
}