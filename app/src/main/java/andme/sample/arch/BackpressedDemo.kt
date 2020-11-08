package andme.sample.arch

import andme.arch.activity.AMBackPressedOwner
import andme.arch.app.AMFragment
import andme.arch.app.AMViewModel
import andme.arch.app.tryUi
import andme.sample.R
import android.app.Application
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class BackpressedDemo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backpressed_demo)
    }
}

class BackPressedVM(application: Application) : AMViewModel(application){

    val content:MutableLiveData<String> = MutableLiveData()

    fun onNaviBackClick(view:View){
        viewModelScope.launch {
            tryUi {
                println("开始处理耗时任务")
                //随机延迟1-4s
                delay(Random.nextLong(1000,4000))
                println("耗时任务已处理，刷新输入框内容")
                content.value = "任务已完成"
            }
        }
    }
}
//
//class BackPressedFragment: AMFragment<BackPressedVM>() {
//
//    override fun onCreateContentView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//    }
//
//    override fun initViews(view: View, savedInstanceState: Bundle?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun handleOnBackPressed(owner: AMBackPressedOwner): Boolean {
//        val content = edit.text.toString()//获取当前输入框内容
//        if(content.isNullOrEmpty())
//            return false
//        //已输入内容，对话框提示
//        AlertDialog.Builder(requireContext())
//            .setMessage("是否保存草稿箱？")
//            .setPositiveButton("保存", DialogInterface.OnClickListener { dialog, which ->
//                //save draft
//                owner.invokeSuperBackPressed()//继续往上传递
//                // or
//                requireActivity().finish() //关闭当前界面
//
//            })
//            .setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
//                owner.invokeSuperBackPressed()//继续往上传递
//                // or
//                requireActivity().finish() //关闭当前界面
//            })
//        return true
//
//
////        val content = viewModel.content.value
////        if(content.isNullOrEmpty())
////            return false
////
////        alert
////        if(viewModel.content.value.isNullOrEmpty()){}
////        return true
//    }
//
//
//}