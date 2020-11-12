package andme.arch.app

import andme.arch.activity.AMBackPressedCallback
import andme.arch.activity.AMBackPressedOwner
import andme.core.kt.Note
import andme.core.ktx.removeSelf
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

/**
 * Created by Lucio on 2020-02-20.
 * Fragment基类
 *  支持：主viewmodel支持、返回键事件拦截处理、view缓存处理
 */
abstract class AMFragment<VM : AMViewModel> : Fragment() {

    @JvmField
    protected var contentView: View? = null

    protected open val viewModelDelegate: AMViewModelOwnerDelegate<VM> =
        AMViewModelOwnerDelegate<VM>(this)

    //主ViewModel
    protected val viewModel: VM get() = viewModelDelegate.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createMainViewModel(savedInstanceState)
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
     * viewmodel 类型的位置
     * 比如：BaseFragment<T1,T2,T3>，如果ViewModel放在T1位置则为0，T2则为1，T3则为2，依次类推，默认定义放在第一个位置
     */
    protected var viewModelParameterPosition = 0

    /**
     * 推断ViewModel类型
     */
    @Note(message = "注意：自动推断在有几种情况下无法推断出正确类型，比如范型的个数、位置等会影响范型的推断，对于只有一个类型的范型子类推断无问题。")
    protected open fun deduceViewModelClass(): Class<VM>? {
        return AMViewModelOwnerDelegate.deduceViewModelClass(this,viewModelParameterPosition)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AMActivity<*>) {
            //注册返回键事件
            context.backPressedDispatcherAM.addCallback(this, object : AMBackPressedCallback() {
                override fun handleOnBackPressed(owner: AMBackPressedOwner): Boolean {
                    return handleOnBackPressed(owner)
                }
            })
        }
    }

    fun addOnBackPressed(onBackPressed: () -> Boolean): OnBackPressedCallback {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!onBackPressed()) {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        return callback
    }

    fun addOnBackPressed(owner: LifecycleOwner, onBackPressed: () -> Boolean): OnBackPressedCallback {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!onBackPressed()) {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(owner,callback)
        return callback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (isEnableViewCache()) {
            if (contentView == null) {
                contentView = createAndInitView(inflater, container, savedInstanceState)
            } else {
                contentView.removeSelf()
            }
        } else {
            contentView = createAndInitView(inflater, container, savedInstanceState)
        }
        return contentView!!
    }

    private fun createAndInitView(
        inflater: LayoutInflater
        , container: ViewGroup?
        , savedInstanceState: Bundle?
    ): View {
        return onCreateContentView(inflater, container, savedInstanceState).apply {
            initViews(this, savedInstanceState)
        }
    }

    /**
     * 创建视图布局
     * 重写此方法返回整整的布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    /**
     * 初始化视图布局
     *
     * @param view
     */
    protected abstract fun initViews(view: View, savedInstanceState: Bundle?)

    /**
     * 处理返回键事件
     */
    protected open fun handleOnBackPressed(owner: AMBackPressedOwner): Boolean {
        //默认不消耗返回键事件
        //可以重写此方法在Fragment中处理返回键事件逻辑
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isEnableViewCache()) {
            contentView.removeSelf()
        } else {
            contentView = null
        }
    }

    /**
     * 是否启用View缓存：默认开启,可以设置为false避免view 缓存
     */
    protected open fun isEnableViewCache(): Boolean {
        return true
    }

    //绑定viewmodel事件:用于非主viewmodel调用的情况
    protected fun registerViewModelEvent(viewModel: AMViewModel) {
        viewModelDelegate.registerViewModelEvent(viewModel)
    }

    //注销ViewModel事件:用于非主viewmodel调用的情况
    protected fun unregisterViewModelEvent(viewModel: AMViewModel) {
        viewModelDelegate.unregisterViewModelEvent(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterViewModelEvent(viewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModelDelegate.onActivityResult(requestCode, resultCode, data)
    }

}