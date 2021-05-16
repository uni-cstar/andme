package andme.core.statelayout

import andme.core.R
import andme.core.statelayout.StateLayoutManager.NONE_RESOURCE_ID
import andme.core.statelayout.StateLayoutManager.STATE_EMPTY
import andme.core.statelayout.StateLayoutManager.STATE_ERROR
import andme.core.statelayout.StateLayoutManager.STATE_LOADING
import andme.core.statelayout.StateLayoutManager.STATE_NO_NETWORK
import andme.core.widget.isVisible
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.children

/**
 * Created by Lucio on 2021/3/30.
 */
internal class StateLayoutDelegate : StateLayout {

    private lateinit var container: ViewGroup
    private val emptyViewInfo = StateLayoutManager.emptyViewInfo.copy()
    private val loadingViewInfo = StateLayoutManager.loadingViewInfo.copy()
    private val errorViewInfo = StateLayoutManager.errorViewInfo.copy()
    private val noNetworkViewInfo = StateLayoutManager.noNetworkViewInfo.copy()

    private var emptyView: StateView? = null
    private var loadingView: StateView? = null
    private var errorView: StateView? = null
    private var noNetworkView: StateView? = null

    private var customStateViewFactory: StateViewFactory? = null

    /**
     * 布局中包含的state view
     */
    private val viewStatesInLayout: HashSet<Int> = hashSetOf()
    private var mOnViewStateChangedListener: ((formerState: Int, curState: Int) -> Unit)? = null


    private var currentViewState: Int = StateLayoutManager.STATE_KNOWN
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
        container = parent
        context.obtainStyledAttributes(attrs, R.styleable.StateLayout, defStyleAttr, 0)
            .apply {
                emptyViewInfo.layoutId =
                    getResourceId(
                        R.styleable.StateLayout_sl_emptyLayout,
                        emptyViewInfo.layoutId
                    )
                loadingViewInfo.layoutId =
                    getResourceId(
                        R.styleable.StateLayout_sl_loadingLayout,
                        loadingViewInfo.layoutId
                    )
                errorViewInfo.layoutId =
                    getResourceId(
                        R.styleable.StateLayout_sl_errorLayout,
                        errorViewInfo.layoutId
                    )
                noNetworkViewInfo.layoutId =
                    getResourceId(
                        R.styleable.StateLayout_sl_noNetworkLayout,
                        noNetworkViewInfo.layoutId
                    )
                recycle()
            }
    }

    override fun setCustomViewFactory(factory: StateViewFactory?){
        this.customStateViewFactory = factory
    }

    /**
     * 当前是否是内容视图
     */
    val isContent
        get() = currentViewState == StateLayoutManager.STATE_CONTENT

    /**
     * 当前是否是加载中视图
     */
    val isLoading
        get() = currentViewState == StateLayoutManager.STATE_LOADING

    /**
     * 当前是否是空数据视图
     */
    val isEmpty: Boolean
        get() = currentViewState == StateLayoutManager.STATE_EMPTY

    /**
     * 当前是否是错误视图
     */
    val isError: Boolean
        get() = currentViewState == StateLayoutManager.STATE_ERROR

    /**
     * 当前是否是断网视图
     */
    val isNoNetwork: Boolean
        get() = currentViewState == StateLayoutManager.STATE_NO_NETWORK


    /**
     * 显示内容布局
     */
    override fun showContentView() {
        container.children.forEach {
            it.visibility =
                if (it.tag == StateLayoutManager.STATE_CONTENT || it.getTag(R.id.sl_state) !in viewStatesInLayout) View.VISIBLE else View.GONE
        }
        currentViewState = StateLayoutManager.STATE_CONTENT
    }

    override fun showLoadingView(setup: (StateView.() -> Unit)?) {
        showStateView(loadingViewInfo, setup)
    }

    /**
     * 设置自定义LoadingView
     *
     * @param attachToViewGroup 是否将view添加到当前ViewGroup中
     */
    override fun setCustomLoadingView(customView: StateView, attachToViewGroup: Boolean) {
        if (attachToViewGroup) {
            require(customView.view.parent == null) {
                "the parent of the custom view is not null."
            }
            addStateViewToContainer(customView.view, loadingViewInfo.state)
        }
        loadingView = customView
    }

    override fun showEmptyView(setup: (StateView.() -> Unit)?) {
        showStateView(emptyViewInfo, setup)
    }

    override fun setCustomEmptyView(customView: StateView, attachToViewGroup: Boolean) {
        if (attachToViewGroup) {
            require(customView.view.parent == null) {
                "the parent of the custom view is not null."
            }
            addStateViewToContainer(customView.view, emptyViewInfo.state)
        }
        emptyView = customView
    }

    override fun showErrorView(setup: (StateView.() -> Unit)?) {
        showStateView(errorViewInfo, setup)
    }

    override fun setCustomErrorView(customView: StateView, attachToViewGroup: Boolean) {
        if (attachToViewGroup) {
            require(customView.view.parent == null) {
                "the parent of the custom view is not null."
            }
            addStateViewToContainer(customView.view, errorViewInfo.state)
        }
        errorView = customView
    }

    override fun showNoNetworkView(setup: (StateView.() -> Unit)?) {
        showStateView(noNetworkViewInfo, setup)
    }

    override fun setCustomNoNetworkView(customView: StateView, attachToViewGroup: Boolean) {
        if (attachToViewGroup) {
            require(customView.view.parent == null) {
                "the parent of the custom view is not null."
            }
            addStateViewToContainer(customView.view, noNetworkViewInfo.state)
        }
        noNetworkView = customView
    }

    private fun showStateView(info: StateViewInfo, setup: (StateView.() -> Unit)?): StateView {
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
        setup?.invoke(view)
        showStateViewByTag(info.state)
        return view
    }

    private fun ensureLoadingView(): StateView {
        return loadingView ?: inflateStateViewByInfo(loadingViewInfo).also {
            loadingView = it
        }
    }

    private fun ensureEmptyView(): StateView {
        return emptyView ?: inflateStateViewByInfo(emptyViewInfo).also {
            emptyView = it
        }
    }

    private fun ensureErrorView(): StateView {
        return errorView ?: inflateStateViewByInfo(errorViewInfo).also {
            errorView = it
        }
    }

    private fun ensureNoNetworkView(): StateView {
        return noNetworkView ?: inflateStateViewByInfo(noNetworkViewInfo).also {
            noNetworkView = it
        }
    }

    private fun inflateStateViewByInfo(info: StateViewInfo): StateView {
        checkLayoutId(info.layoutId)
        val stateViewFactory = customStateViewFactory ?: info.factory

        return stateViewFactory.createStateView(container.context, container, info).also {
            addStateViewToContainer(it.view, info.state)
        }
    }

    override fun showCustomStateView(state: Int, setup: (View.() -> Unit)?) {
        if (!viewStatesInLayout.contains(state)) {
            inflateStateViewByInfo(StateLayoutManager.getStateInfoOrCreate(state)).also {
                setup?.invoke(it.view)
                showStateViewByTag(state)
            }
        } else {
            container.findViewWithTag<View>(state)?.also {
                setup?.invoke(it)
                showStateViewByTag(state)
            }
        }
    }

    /**
     * 视图改变监听事件
     * formerState：之前的状态
     * curState：当前状态
     */
    fun setOnViewStateChangeListener(listener: (formerState: Int, curState: Int) -> Unit) {
        mOnViewStateChangedListener = listener
    }

    private fun checkLayoutId(@LayoutRes layoutId: Int) {
        if (layoutId == NONE_RESOURCE_ID) {
            throw IllegalStateException("请先设置该状态视图的布局！")
        }
    }

    /**
     * 根据child tag 显示指定State的view
     */
    private fun showStateViewByTag(viewState: Int) {
        container.children.forEach {
            it.isVisible = it.getTag(R.id.sl_state) == viewState
        }
        currentViewState = viewState
    }

    private fun addStateViewToContainer(view: View, @StateLayoutManager.State state: Int) {
        container.addView(view)
        view.setTag(R.id.sl_state, state)
        viewStatesInLayout.add(state)
    }


}