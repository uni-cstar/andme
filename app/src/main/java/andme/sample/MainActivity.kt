package andme.sample

import andme.lang.urlEncode
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn).setOnClickListener {
            it.requestFocus()
//            val it =  Intent().also {
//                it.component = ComponentName("com.insight.hxytv", "ucux.android.tv.ui.entrance.LauncherActivity")
//                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }

            //对应界面协议
            val directTo = "gdtv://exam/errquestion"
            //对协议进行url编码
            val encodeDirectTo = directTo.urlEncode()
            //将前面得到的值作为uri参数，封装成新的协议
            val uri = Uri.parse("uxtv://applink?uri=${encodeDirectTo}")
            //固定action和data创建意图
            val it = Intent("android.intent.action.VIEW", uri)
            startActivity(it)
        }
    }


}

