package andme.arch.app

import andme.core.app.AMAnonyContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Lucio on 2020-11-02.
 */

interface AMViewModelOwner : AMAnonyContext, LifecycleOwner {

    fun getViewModelProvider(): ViewModelProvider

    fun finish()

    fun onBackPressed()

//    companion object {
//
//        fun new(activity: ComponentActivity): AMViewModelOwner {
//            return object : AMViewModelOwner {
//
//                override val realCtx: Context
//                    get() = activity
//
//                override fun getLifecycle(): Lifecycle = activity.lifecycle
//
//
//                override fun getViewModelProvider(): ViewModelProvider {
//                    return ViewModelProvider(
//                            activity,
//                            ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application)
//                    )
//                }
//
//
//                override fun finish() {
//                    activity.finish()
//                }
//
//                override fun onBackPressed() {
//                    activity.onBackPressed()
//                }
//
//                override fun startActivity(intent: Intent) {
//                    activity.startActivity(intent)
//                }
//
//                override fun startActivityForResult(intent: Intent, requestCode: Int) {
//                    activity.startActivityForResult(intent, requestCode)
//                }
//            }
//        }
//
//        fun new(fragment: Fragment): AMViewModelOwner {
//            return object : AMViewModelOwner {
//
//                override fun getLifecycle(): Lifecycle = fragment.lifecycle
//
//                override fun getViewModelProvider(): ViewModelProvider {
//                    return ViewModelProvider(
//                            fragment,
//                            ViewModelProvider.AndroidViewModelFactory.getInstance(fragment.requireActivity().application)
//                    )
//                }
//
//                override fun finish() {
//                    fragment.requireActivity().finish()
//                }
//
//                override fun onBackPressed() {
//                    fragment.requireActivity().onBackPressed()
//                }
//
//                override val realCtx: Context
//                    get() = fragment.requireContext()
//
//                override fun startActivity(intent: Intent) {
//                    fragment.startActivity(intent)
//                }
//                override fun startActivityForResult(intent: Intent, requestCode: Int) {
//                    fragment.startActivityForResult(intent, requestCode)
//                }
//            }
//        }
//    }
}