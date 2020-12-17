package andme.arch.app

import andme.core.toastHandlerAM
import andme.lang.Note
import andme.lang.runOnTrue
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import java.lang.reflect.ParameterizedType

/**
 * [AMViewModel]的事件绑定、实现等代理类
 */
open class AMViewModelOwnerDelegate<VM : ViewModel> constructor(open val realOwner: AMViewModelOwner) : AMViewModelOwner by realOwner {

    lateinit var viewModel: VM
        private set

    open fun onCreate(savedInstanceState: Bundle?, vmClass: Class<VM>) {
        viewModel = getViewModelProvider().get(vmClass)
        initViewModelEvent(viewModel, false)
    }

    /**
     * @param autoBindOwnerIfMatch 如果获取的ViewModel是[AMViewModel]是否自动绑定事件，默认自动绑定
     */
    fun <T : ViewModel> obtainViewModel(clazz: Class<T>, autoBindOwnerIfMatch: Boolean = true): T {
        return getViewModelProvider().get(clazz).also {
            if (autoBindOwnerIfMatch && it is AMViewModel && !it.hasBindOwner) {
                initViewModelEvent(viewModel, false)
            }
        }
    }

    /**
     * 初始化viewmodel相关事件
     * @param removePrevious 是否在绑定事件之前先调用移除方法，避免多次绑定
     */
    protected open fun initViewModelEvent(viewModel: ViewModel, removePrevious: Boolean = true) {

        if (viewModel !is AMViewModel)
            return
        //先移除观察，避免重复绑定
        this.runOnTrue(removePrevious) {
            unregisterViewModelEvent(viewModel)
        }
        //添加观察
        registerViewModelEvent(viewModel)
    }

    //绑定viewmodel事件
    open fun registerViewModelEvent(viewModel: AMViewModel) {
        viewModel.hasBindOwner = true
        lifecycle.addObserver(viewModel)
        val lifecycleOwner = this
        viewModel.apply {
            finishEvent.observe(lifecycleOwner, Observer {
                onFinishByViewModel()
            })

            backPressedEvent.observe(lifecycleOwner, Observer {
                onBackPressedByViewModel()
            })
            startActivityEvent.observe(lifecycleOwner, Observer<Intent> {
                onStartActivityByViewModel(it)
            })

            startActivityForResultEvent.observe(lifecycleOwner, Observer<Pair<Intent, Int>> {
                startActivityForResultByViewModel(it.first, it.second)
            })

            toastEvent.observe(lifecycleOwner, Observer {
                onToastByViewModel(it.first, it.second)
            })

            contextActionEvent.observe(lifecycleOwner, Observer<AMViewModel.ContextAction> {
                onContextActionByViewModel(it)
            })
        }
    }

    //注销ViewModel事件
    open fun unregisterViewModelEvent(viewModel: AMViewModel) {
        viewModel.unregister(this)
    }

    protected open fun onFinishByViewModel() {
        finish()
    }

    protected open fun onBackPressedByViewModel() {
        onBackPressed()
    }

    protected open fun onStartActivityByViewModel(intent: Intent) {
        startActivity(intent)
    }

    protected open fun startActivityForResultByViewModel(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    protected open fun onToastByViewModel(msg: String, length: Int) {
        toastHandlerAM.showToast(realCtx, msg, length)
    }

    protected open fun onContextActionByViewModel(event: AMViewModel.ContextAction) {
        event.onContextAction(realCtx)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        (viewModel as? AMViewModel)?.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        /**
         * 推断ViewModel类型
         * @param tPosition ViewModel类型定义的位置：即ViewModel的T类型是第几个参数
         */
        @JvmStatic
        @Note(message = "注意：自动推断在有几种情况下无法推断出正确类型，比如范型的个数、位置等会影响范型的推断，对于只有一个类型的范型子类推断无问题。")
        internal fun <VM> deduceViewModelClass(instance: Any, tPosition: Int = 0): Class<VM>? {
            val gSuperClass = findParameterizedType(instance.javaClass) ?: return null
            val target = gSuperClass.actualTypeArguments[tPosition]

            if (target is Class<*>) {
                return target as? Class<VM>
            }
            return null
//        else if(target is TypeVariable<*>){
//            val field =  target.javaClass.getDeclaredField("boundASTs")
//            field.isAccessible = true
//            val typeSignature = field.get(target) as Array<*>
//            val tempFieldValue = typeSignature[0]!!
//            val pathField =tempFieldValue.javaClass.getDeclaredField("path")
//            pathField.isAccessible = true
//            val pathValue = pathField.get(tempFieldValue) as ArrayList<*>
//            val nameField = pathValue[0].javaClass.getDeclaredField("name")
//            nameField.isAccessible = true
//            val nameValue = nameField.get(pathValue[0]) as String
//
//            val temClass = Class.forName(nameValue)
//            println(temClass.name)
//            return temClass as? Class<T>
//        }
        }

        //查找泛型具体类型
        @JvmStatic
        private fun findParameterizedType(clazz: Class<*>): ParameterizedType? {
            val superClass = clazz.superclass ?: return null
//            if (superClass != AMActivity::class.java) {
//                return findParameterizedType(superClass)
//            }
            val gSuperClass = clazz.genericSuperclass
            if (gSuperClass !is ParameterizedType)
                return findParameterizedType(superClass)
            return gSuperClass
        }
    }

}