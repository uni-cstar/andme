package andme.arch.refresh.scene

import andme.arch.refresh.AMRefreshLayout
import andme.core.exception.tryCatch
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

/**
 * Created by Lucio on 2020/12/17.
 */
open class AMSwipeRefreshLayout : SwipeRefreshLayout, AMRefreshLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var mListener:OnRefreshListener? = null

    override fun autoRefresh(delay: Long) {
        if(isRefreshingAM){
            Log.w("AMWarn","已经处于刷新中,忽略刷新请求")
            return
        }
        if (delay > 0) {
            postDelayed({
                tryCatch {
                    if(isRefreshingAM){
                        Log.w("AMWarn","已经处于刷新中,忽略刷新请求")
                        return@postDelayed
                    }
                    invokeRefreshRequest()
                }
            }, delay)
        }else{
            invokeRefreshRequest()
        }
    }

    private fun invokeRefreshRequest(){
        isRefreshingAM = true
        mListener?.onRefresh()
    }

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
        val listenerWrapper = createRefreshListener(listener)
        setOnRefreshListener(listenerWrapper)
        mListener = listenerWrapper
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