package andme.arch.app

import andme.arch.refresh.AMLoadMoreLayout
import andme.arch.refresh.AMRefreshLayout
import andme.arch.refresh.AMRefreshLayoutProvider
import andme.arch.refresh.AMViewModelRefreshableOwnerDelegate
import andme.core.activity.AMBackPressedDispatcher
import andme.core.activity.AMBackPressedOwner
import andme.core.exception.tryCatch
import andme.lang.Note
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Lucio on 2020/11/1.
 * 提供功能：基础ViewModel支持（常规UI事件、生命周期绑定等）、返回键事件分发，状态栏控制等
 */
abstract class AMActivity<VM : AMViewModel> : AppCompatActivity(), AMBackPressedOwner, AMViewModelOwner, AMRefreshLayoutProvider {

    protected inline val activity: AMActivity<VM> get() = this

    //主ViewModel
    protected val viewModel: VM get() = viewModelDelegate.viewModel

    protected open val viewModelDelegate: AMViewModelOwnerDelegate<VM> = AMViewModelRefreshableOwnerDelegate<VM>(this, this)

    //返回键分发器
    private val _backPressedDispatcher = AMBackPressedDispatcher(this)

    /**
     * 获取返回键分发器
     */
    val backPressedDispatcherAM: AMBackPressedDispatcher get() = _backPressedDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createMainViewModel(savedInstanceState)
    }

    /**
     * @param autoBindOwnerIfMatch 如果获取的ViewModel是[AMViewModel]是否自动绑定事件，默认自动绑定
     */
    fun <T : ViewModel> obtainViewModel(clazz: Class<T>, autoBindOwnerIfMatch: Boolean = true): T {
        return this.viewModelDelegate.obtainViewModel(clazz, autoBindOwnerIfMatch)
    }

    /**
     * 初始化主ViewModel
     */
    protected open fun createMainViewModel(savedInstanceState: Bundle?) {
        val vmClass = deduceViewModelClass()
                ?: throw RuntimeException("无法正确推断ViewModel的类型，请重写deduceViewModelClass方法返回自定义的ViewModel Class")
        viewModelDelegate.onCreate(savedInstanceState, vmClass)
    }

    /**
     * 推断ViewModel类型
     */
    @Note(message = "注意：自动推断在有几种情况下无法推断出正确类型，比如范型的个数、位置等会影响范型的推断，对于只有一个类型的范型子类推断无问题。")
    protected open fun deduceViewModelClass(): Class<VM>? {
        return AMViewModelOwnerDelegate.deduceViewModelClass(this, viewModelParameterPosition)
    }

    /**
     * viewmodel 类型的位置
     * 比如：BaseActivity<T1,T2,T3>，如果ViewModel放在T1位置则为0，T2则为1，T3则为2，依次类推，默认定义放在第一个位置
     */
    protected var viewModelParameterPosition = 0

    //绑定viewmodel事件:用于非主viewmodel调用的情况
    protected fun registerViewModelEvent(viewModel: AMViewModel) {
        viewModelDelegate.registerViewModelEvent(viewModel)
    }

    //注销ViewModel事件:用于非主viewmodel调用的情况
    protected fun unregisterViewModelEvent(viewModel: AMViewModel) {
        viewModelDelegate.unregisterViewModelEvent(viewModel)
    }

    override fun onDestroy() {
        tryCatch {
            unregisterViewModelEvent(viewModel)
        }
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModelDelegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun invokeSuperBackPressed() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        tryCatch {
            if (backPressedDispatcherAM.onBackPressed())
                return
        }
        invokeSuperBackPressed()
    }

    override val realCtx: Context
        get() = this

    override fun getViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        )
    }

    /*********refresh support*************/

    override fun getRefreshLayout(): AMRefreshLayout? {
        return null
    }

    override fun getLoadMoreLayout(): AMLoadMoreLayout? {
        return null
    }

    private fun setEnableRefresh(onRefresh: AMRefreshLayout.OnRefreshListenerAM) {
        val refreshLayout = getRefreshLayout() ?: throw IllegalStateException("请先返回刷新布局")
        refreshLayout.enableRefreshAM = true
        refreshLayout.setOnRefreshListenerAM(onRefresh)
    }

    private fun setEnableLoadMore(onLoadMore: AMLoadMoreLayout.OnLoadMoreListenerAM) {
        val loadMoreLayout = getLoadMoreLayout()
                ?: throw IllegalStateException("请通过getLoadMoreLayout方法返回加载更多的布局")
        loadMoreLayout.enableLoadMoreAM = true
        loadMoreLayout.setOnLoadMoreListenerAM(onLoadMore)
    }

    fun initLoadMoreOnly(onLoadMore: AMLoadMoreLayout.OnLoadMoreListenerAM){
        getRefreshLayout()?.enableRefreshAM = false
        setEnableLoadMore(onLoadMore)
    }

    fun initRefreshOnly(onRefresh: AMRefreshLayout.OnRefreshListenerAM) {
        setEnableRefresh(onRefresh)
        getLoadMoreLayout()?.enableLoadMoreAM = false
    }

    fun initRefreshAndLoadMore(onRefresh: AMRefreshLayout.OnRefreshListenerAM, onLoadMore: AMLoadMoreLayout.OnLoadMoreListenerAM) {
        setEnableRefresh(onRefresh)
        setEnableLoadMore(onLoadMore)
    }

    /***********refresh support***************/
}