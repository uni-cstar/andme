package andme.core.multistate

import andme.core.R
import andme.core.content.layoutInflater
import andme.core.multistate.MultiState.NONE_RESOURCE_ID
import andme.core.multistate.MultiState.STATE_EMPTY
import andme.core.multistate.MultiState.STATE_ERROR
import andme.core.multistate.MultiState.STATE_LOADING
import andme.core.multistate.MultiState.STATE_NO_NETWORK
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.children

/**
 * Created by Lucio on 2021/3/30.
 */
internal class LayoutDelegate : MultiStateLayout {

    lateinit var mTarget: ViewGroup
    val mEmptyInfo = MultiState.emptyInfo.copy()
    val mLoadingInfo = MultiState.loadingInfo.copy()
    val mErrorInfo = MultiState.errorInfo.copy()
    val mNoNetworkInfo = MultiState.noNetworkInfo.copy()
    var mOnViewStateChangedListener: ((formerState: Int, curState: Int) -> Unit)? = null
    var mClickListener: MultiStateLayout.OnItemViewClickListener? = null

    /**
     * 布局中包含的state view
     */
    val mStatesInLayout: HashSet<Int> = hashSetOf()

    var mEmptyView: View? = null
    var mLoadingView: View? = null
    var mErrorView: View? = null
    var mNoNetworkView: View? = null

    var mCurrentState: Int = MultiState.STATE_KNOWN
        set(value) {
            if (field != value) {
                mOnViewStateChangedListener?.invoke(field, value)
                field = value
            }
        }

    /**
     * 解析属性
     */
    fun obtainStyledAttributes(
        context: Context,
        parent: ViewGroup,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) {
        mTarget = parent
        context.obtainStyledAttributes(attrs, R.styleable.MultiStateLayout, defStyleAttr, 0)
            .apply {
                mEmptyInfo.layoutId =
                    getResourceId(
                        R.styleable.MultiStateLayout_msl_emptyView,
                        mEmptyInfo.layoutId
                    )
                mLoadingInfo.layoutId =
                    getResourceId(
                        R.styleable.MultiStateLayout_msl_loadingView,
                        mLoadingInfo.layoutId
                    )
                mErrorInfo.layoutId =
                    getResourceId(
                        R.styleable.MultiStateLayout_msl_errorView,
                        mErrorInfo.layoutId
                    )
                mNoNetworkInfo.layoutId =
                    getResourceId(
                        R.styleable.MultiStateLayout_msl_noNetworkView,
                        mNoNetworkInfo.layoutId
                    )
                recycle()
            }
    }


    /**
     * 当前是否是内容视图
     */
    val isContent
        get() = mCurrentState == MultiState.STATE_CONTENT

    /**
     * 当前是否是加载中视图
     */
    val isLoading
        get() = mCurrentState == MultiState.STATE_LOADING

    /**
     * 当前是否是空数据视图
     */
    val isEmpty: Boolean
        get() = mCurrentState == MultiState.STATE_EMPTY

    /**
     * 当前是否是错误视图
     */
    val isError: Boolean
        get() = mCurrentState == MultiState.STATE_ERROR

    /**
     * 当前是否是断网视图
     */
    val isNoNetwork: Boolean
        get() = mCurrentState == MultiState.STATE_NO_NETWORK

    private fun inflateView(layoutId: Int): View =
        mTarget.context.layoutInflater.inflate(layoutId, mTarget, false)

    override fun getAttachedViewGroup(): ViewGroup {
        return mTarget
    }

    /**
     * 显示内容布局
     */
    override fun showContent() {
        showContentView()
    }

    /**
     * 显示内容布局
     */
    private fun showContentView() {
        mTarget.children.forEach {
            it.visibility =
                if (it.tag == MultiState.STATE_CONTENT || it.tag !in mStatesInLayout) View.VISIBLE else View.GONE
        }
        mCurrentState = MultiState.STATE_CONTENT
    }


    override fun showLoading(): View {
        return showStateView(mLoadingInfo)
    }

    override fun showLoading(hint: String): View {
        return showLoading(mLoadingInfo.hintId, hint)
    }

    override fun showLoading(hintViewId: Int, hint: String): View {
        return showStateView(mLoadingInfo).also {
            it.findViewById<TextView>(hintViewId)?.text = hint
        }
    }

    override fun setCustomLoadingView(customView: View) {
        mLoadingView = customView
    }

    override fun showEmpty(): View {
        return showStateView(mEmptyInfo)
    }

    override fun showEmpty(hint: String): View {
        return showEmpty(mEmptyInfo.hintId, hint)
    }

    override fun showEmpty(hintViewId: Int, hint: String): View {
        return showStateView(mEmptyInfo).also {
            it.findViewById<TextView>(hintViewId)?.text = hint
        }
    }

    override fun setCustomEmptyView(customView: View) {
        mEmptyView = customView
    }

    override fun showError(): View {
        return showStateView(mErrorInfo)
    }

    override fun showError(hint: String): View {
        return showError(mErrorInfo.hintId, hint)
    }

    override fun showError(hintViewId: Int, hint: String): View {
        return showStateView(mErrorInfo).also {
            it.findViewById<TextView>(hintViewId)?.text = hint
        }
    }

    override fun setCustomErrorView(customView: View) {
        mErrorView = customView
    }

    override fun showNoNetworkView(): View {
        return showStateView(mNoNetworkInfo)
    }

    override fun showNoNetworkView(hint: String): View {
        return showNoNetworkView(mNoNetworkInfo.hintId, hint)
    }

    override fun showNoNetworkView(hintViewId: Int, hint: String): View {
        return showStateView(mNoNetworkInfo).also {
            it.findViewById<TextView>(hintViewId)?.text = hint
        }
    }

    override fun setCustomNoNetworkView(customView: View) {
        mNoNetworkView = customView
    }

    private fun showStateView(info: StateInfo): View {
        val view = when (info.state) {
            STATE_LOADING -> {
                ensureLoadingView()
            }
            STATE_EMPTY -> {
                ensureEmptyView()
            }
            STATE_ERROR -> {
                ensureErrorView()
            }
            STATE_NO_NETWORK -> {
                ensureNoNetworkView()
            }
            else -> {
                throw IllegalStateException("无效的State View:${info.state}")
            }
        }
        showViewByState(info.state)
        return view
    }

    private fun ensureLoadingView(): View {
        return mLoadingView ?: inflateStateViewByInfo(mLoadingInfo).also {
            mLoadingView = it
        }
    }

    private fun ensureEmptyView(): View {
        return mEmptyView ?: inflateStateViewByInfo(mEmptyInfo).also {
            mEmptyView = it
        }
    }

    private fun ensureErrorView(): View {
        return mErrorView ?: inflateStateViewByInfo(mErrorInfo).also {
            mErrorView = it
        }
    }

    private fun ensureNoNetworkView(): View {
        return mNoNetworkView ?: inflateStateViewByInfo(mNoNetworkInfo).also {
            mNoNetworkView = it
        }
    }

    private fun inflateStateViewByInfo(info: StateInfo): View {
        checkLayoutId(info.layoutId)
        return inflateView(info.layoutId).also {
            if (info.hintId.isLegalResId && info.hintText != null) {
                it.findViewById<TextView>(info.hintId)?.text = info.hintText
            }
            if (!info.clickViewIds.isNullOrEmpty()) {
                it.setOnChildViewClickListener(info.state, info.clickViewIds)
            }
            it.tag = info.state
            mStatesInLayout.add(info.state)
            mTarget.addView(it, 0)
        }
    }

    override fun showCustomStateView(state: Int): View {
        if (!mStatesInLayout.contains(state)) {
            return inflateStateViewByInfo(MultiState.getStateInfoOrCreate(state)).also {
                showViewByState(state)
            }
        } else {
            return mTarget.findViewWithTag<View>(state).also {
                showViewByState(state)
            }
        }
    }

    override fun getCurrentState(): Int {
        return mCurrentState
    }


    /**
     * 视图改变监听事件
     * formerState：之前的状态
     * curState：当前状态
     */
    fun setOnViewStateChangeListener(listener: (formerState: Int, curState: Int) -> Unit) {
        mOnViewStateChangedListener = listener
    }

    /**
     * 设置点击事件
     */
    override fun setOnItemViewClickListener(listener: MultiStateLayout.OnItemViewClickListener?) {
        this.mClickListener = listener
    }


    private fun checkLayoutId(@LayoutRes layoutId: Int) {
        if (layoutId == NONE_RESOURCE_ID) {
            throw NullPointerException("请先设置该状态视图的布局！")
        }
    }


    private fun View.setOnChildViewClickListener(status: Int, childViewIds: List<Int>) {
        mClickListener?.also { callback ->
            if (childViewIds.isEmpty()) {
                return
            }
            childViewIds.forEach { id ->
                this.findViewById<View>(id)?.setOnClickListener { view ->
                    callback.onItemViewClick(view, status)
                }
            }
        }
    }

    private fun showViewByState(status: Int) {
        mTarget.children.forEach {
            it.visibility = if (it.tag == status) View.VISIBLE else View.GONE
        }
        mCurrentState = status
    }


    private val Int.isLegalResId
        get() = this != NONE_RESOURCE_ID

}