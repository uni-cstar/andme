package andme.arch.app

import andme.arch.activity.AMBackPressedDispatcher
import andme.arch.activity.AMBackPressedOwner
import andme.core.exception.tryCatch
import andme.lang.Note
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Lucio on 2020/11/1.
 * 提供功能：基础ViewModel支持（常规UI事件、生命周期绑定等）、返回键事件分发，状态栏控制等
 */
abstract class AMActivity<VM : AMViewModel> : AppCompatActivity(), AMBackPressedOwner {

    protected inline val activity: AMActivity<VM> get() = this

    //主ViewModel
    protected val viewModel: VM get() = viewModelDelegate.viewModel

    protected open val viewModelDelegate: AMViewModelOwnerDelegate<VM> = AMViewModelOwnerDelegate<VM>(this)

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

    final override fun invokeSuperBackPressed() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        tryCatch {
            if (backPressedDispatcherAM.onBackPressed())
                return
        }
        super.onBackPressed()
    }

//
//    protected open val defaultStatusBarAlpha: Int = amStatusBar.defaultStatusBarAlpha
//
//    /**
//     * 设置状态栏模式为浅色模式（背景浅色，文字图标则为暗色-黑色）
//     */
//    open fun setStatusBarLightMode() {
//        amStatusBar.setStatusBarLightMode(this)
//    }
//
//    /**
//     * 设置状态栏为暗色模式（默认即为暗色模式，深色背景，文字图标为浅色-白色）
//     */
//    open fun setStatusBarDarkMode() {
//        amStatusBar.setStatusBarDarkMode(this)
//    }
//
//    /**
//     * 设置状态栏颜色
//     *
//     * @param activity       需要设置的activity
//     * @param color          状态栏颜色值
//     * @param alpha 状态栏透明度
//     */
//    @JvmOverloads
//    open fun setStatusBarColor(
//        @ColorInt color: Int,
//        @androidx.annotation.IntRange(from = 0, to = 255) alpha: Int = defaultStatusBarAlpha
//    ) {
//        amStatusBar.setStatusBarColor(this, color, alpha)
//    }
//
//    @JvmOverloads
//    fun setStatusBarTransparent(needOffsetView: View? = null) {
//        amStatusBar.setStatusBarTransparent(this, needOffsetView)
//    }
//
//    /**
//     * 为头部是 ImageView 的界面设置状态栏透明
//     *
//     * @param activity       需要设置的activity
//     * @param alpha 状态栏透明度
//     * @param needOffsetView 需要向下偏移的 View
//     */
//    @JvmOverloads
//    fun setStatusBarTranslucent(
//        @androidx.annotation.IntRange(from = 0, to = 255) alpha: Int = defaultStatusBarAlpha,
//        needOffsetView: View? = null
//    ) {
//        amStatusBar.setStatusBarTranslucent(this, alpha, needOffsetView)
//    }
//
//    @JvmOverloads
//    fun setStatusBarTransparentInFragment(needOffsetView: View? = null) {
//        amStatusBar.setStatusBarTransparentInFragment(this, needOffsetView)
//    }
//
//    /**
//     * 为 fragment 头部是 ImageView 的设置状态栏透明
//     *
//     * @param activity       fragment 对应的 activity
//     * @param alpha 状态栏透明度
//     * @param needOffsetView 需要向下偏移的 View
//     */
//    @JvmOverloads
//    fun setStatusBarTranslucentInFragment(
//        @androidx.annotation.IntRange(from = 0, to = 255) alpha: Int = defaultStatusBarAlpha,
//        needOffsetView: View? = null
//    ) {
//        amStatusBar.setStatusBarTranslucentInFragment(this, alpha, needOffsetView)
//    }

//    /**
//     * 快速运行一个Activity
//     */
//    fun startActivity(clazz: Class<out Fragment>, args: Bundle? = null) {
//        val colorPrimaryDark = getThemeColor(R.attr.colorPrimaryDark)
//        val it = AMContainerActivity.Builder(this, clazz)
//            .setFragmentArgument(args)
//            .setStatusBarColor(colorPrimaryDark)
//            .build()
//        startActivity(it)
//    }
//
//    protected fun getThemeColor(@AttrRes attr: Int): Int {
//        val typedValue = TypedValue()
//        theme.resolveAttribute(attr, typedValue, true)
//        return typedValue.data
//    }


}