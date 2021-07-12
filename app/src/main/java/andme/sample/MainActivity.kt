package andme.sample

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn).setOnClickListener {
            it.requestFocus()
            val it =  Intent().also {
                it.component = ComponentName("com.insight.hxytv", "ucux.android.tv.ui.entrance.LauncherActivity")
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(it)
        }
    }


}

