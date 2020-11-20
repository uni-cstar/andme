package andme.arch.app

import andme.core.kt.Note
import andme.core.kt.runOnTrue
import andme.core.toastHandlerAM
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import java.lang.reflect.ParameterizedType

open class AMViewModelOwnerDelegate<VM : AMViewModel> private constructor(private val _owner: AMViewModelOwner) {

    lateinit var viewModel: VM
        private set

    constructor(activity: ComponentActivity) : this(AMViewModelOwner.Companion.new(activity))

    constructor(fragment: Fragment) : this(AMViewModelOwner.Companion.new(fragment))

    open fun onCreate(savedInstanceState: Bundle?, vmClass: Class<VM>) {
        viewModel = _owner.getViewModelProvider().get(vmClass)
        initViewModelEvent(viewModel, false)
    }

    /**
     * 初始化viewmodel相关事件
     * @param removePrevious 是否在绑定事件之前先调用移除方法，避免多次绑定
     */
    protected open fun initViewModelEvent(viewModel: AMViewModel, removePrevious: Boolean = true) {
        //先移除观察，避免重复绑定
        this.runOnTrue(removePrevious) {
            unregisterViewModelEvent(viewModel)
        }

        //添加观察
        registerViewModelEvent(viewModel)
    }

    //绑定viewmodel事件
     fun registerViewModelEvent(viewModel: AMViewModel) {
        _owner.getLifecycle().addObserver(viewModel)
        val lifecycleOwner = _owner.getLifecycleOwner()
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
     fun unregisterViewModelEvent(viewModel: AMViewModel) {
        _owner.getLifecycle().removeObserver(viewModel)
        val lifecycleOwner = _owner.getLifecycleOwner()
        viewModel.apply {
            //先移除所有事件
            finishEvent.removeObservers(lifecycleOwner)
            backPressedEvent.removeObservers(lifecycleOwner)
            startActivityEvent.removeObservers(lifecycleOwner)
            startActivityForResultEvent.removeObservers(lifecycleOwner)
            toastEvent.removeObservers(lifecycleOwner)
            contextActionEvent.removeObservers(lifecycleOwner)
        }
    }

    protected open fun onFinishByViewModel() {
        _owner.finish()
    }

    protected open fun onBackPressedByViewModel() {
        _owner.onBackPressed()
    }

    protected open fun onStartActivityByViewModel(intent: Intent) {
        _owner.startActivity(intent)
    }

    protected open fun startActivityForResultByViewModel(intent: Intent, requestCode: Int) {
        _owner.startActivityForResult(intent, requestCode)
    }

    protected open fun onToastByViewModel(msg: String, length: Int) {
        toastHandlerAM.showToast(_owner.realCtx,msg,length)
    }

    protected open fun onContextActionByViewModel(event: AMViewModel.ContextAction) {
        event.onContextAction(_owner.realCtx)
    }

     fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        /**
         * 推断ViewModel类型
         * @param tPosition ViewModel类型定义的位置：即ViewModel的T类型是第几个参数
         */
        @JvmStatic
        @Note(message = "注意：自动推断在有几种情况下无法推断出正确类型，比如范型的个数、位置等会影响范型的推断，对于只有一个类型的范型子类推断无问题。")
        internal fun <VM> deduceViewModelClass(instance: Any,tPosition:Int = 0): Class<VM>? {
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