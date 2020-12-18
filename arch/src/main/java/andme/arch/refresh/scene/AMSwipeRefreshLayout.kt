package andme.arch.refresh.scene

import andme.arch.refresh.AMRefreshLayout
import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

/**
 * Created by Lucio on 2020/12/17.
 */
open class AMSwipeRefreshLayout : SwipeRefreshLayout, AMRefreshLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onRefreshSuccessAM() {
        finishRefreshing()
    }

    override fun onRefreshFailAM(e: Throwable?) {
        finishRefreshing()
    }

    override var enableRefreshAM: Boolean
        get() = isEnabled
        set(value) {
            isEnabled = value
        }

    override var isRefreshingAM: Boolean
        get() = this.isRefreshing
        set(value) {
            isRefreshing = value
        }

    override fun setOnRefreshListenerAM(listener: AMRefreshLayout.OnRefreshListenerAM?) {
        setOnRefreshListener(createRefreshListener(listener))
    }

    private fun createRefreshListener(listener: AMRefreshLayout.OnRefreshListenerAM?): OnRefreshListener? {
        if (listener == null)
            return null
        return OnRefreshListener { listener.onRefreshAM() }
    }

    protected open fun finishRefreshing() {
        this.isRefreshingAM = false
    }

}