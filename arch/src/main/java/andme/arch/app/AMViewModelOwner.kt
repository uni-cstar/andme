package andme.arch.app

import andme.core.app.AMAnonyContext
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Lucio on 2020-11-02.
 */

interface AMViewModelOwner: AMAnonyContext {

    fun getLifecycle(): Lifecycle

    fun getLifecycleOwner(): LifecycleOwner

    fun getViewModelProvider(): ViewModelProvider

    fun finish()

    fun onBackPressed()


    companion object {

        fun new(activity: ComponentActivity): AMViewModelOwner {
           return object :AMViewModelOwner{
               override val realCtx: Context
                   get() = activity

               override fun getLifecycle(): Lifecycle {
                   return activity.lifecycle
               }

               override fun getLifecycleOwner(): LifecycleOwner {
                   return activity
               }

               override fun getViewModelProvider(): ViewModelProvider {
                   return ViewModelProvider(
                       activity,
                       ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application)
                   )
               }

               override fun finish() {
                   activity.finish()
               }

               override fun onBackPressed() {
                   activity.onBackPressed()
               }

               override fun startActivity(intent: Intent) {
                   activity.startActivity(intent)
               }

               override fun startActivityForResult(intent: Intent, requestCode: Int) {
                   activity.startActivityForResult(intent,requestCode)
               }
           }
        }

        fun new(fragment: Fragment): AMViewModelOwner {
            return object:AMViewModelOwner{
                override fun getLifecycle(): Lifecycle {
                    return fragment.lifecycle
                }

                override fun getLifecycleOwner(): LifecycleOwner {
                    return fragment
                }

                override fun getViewModelProvider(): ViewModelProvider {
                    return ViewModelProvider(
                        fragment,
                        ViewModelProvider.AndroidViewModelFactory.getInstance(fragment.requireActivity().application)
                    )
                }

                override fun finish() {
                    fragment.requireActivity().finish()
                }

                override fun onBackPressed() {
                    fragment.requireActivity().onBackPressed()
                }

                override val realCtx: Context
                    get() = fragment.requireContext()

                override fun startActivity(intent: Intent) {
                    fragment.startActivity(intent)
                }

                override fun startActivityForResult(intent: Intent, requestCode: Int) {
                    fragment.startActivityForResult(intent,requestCode)
                }

            }
        }
    }
}