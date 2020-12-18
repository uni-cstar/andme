package andme.arch.refresh.scene

import andme.arch.R
import android.content.Context
import android.util.AttributeSet

/**
 * Created by Lucio on 2020/12/17.
 * SwipeRefreshLayout + RecyclerView + BRVAHAdapter 刷新场景
 */
open class AMSwipeRefreshBRVAHRecyclerLayout : AMSwipeRefreshBRVAHLayout {

    lateinit var recyclerView: AMRecyclerView
        private set

    constructor(context: Context) : super(context){
        ensureRecyclerChildView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        ensureRecyclerChildView()
    }

    private fun ensureRecyclerChildView() {
        val child = findViewById<AMRecyclerView>(R.id.am_id_recycler)
        if (child != null) {
            recyclerView = child
        } else {
            recyclerView = AMRecyclerView(context)
            addView(recyclerView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }
        recyclerView.setSwipeRefreshBRVAHLayoutParent(this)
    }

    fun initRecyclerView(initial: AMRecyclerView.() -> Unit) {
        initial(recyclerView)
    }

}