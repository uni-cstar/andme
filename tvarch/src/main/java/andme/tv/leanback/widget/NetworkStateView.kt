package andme.tv.leanback.widget

import andme.core.exception.tryCatch
import andme.core.util.NETWORK_TYPE_ETHERNET
import andme.core.util.NETWORK_TYPE_NONE
import andme.core.util.NETWORK_TYPE_WIFI
import andme.core.util.getNetworkType
import andme.tv.arch.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * Created by Lucio on 2021/2/27.
 * 根据网络状态显示对应图标
 */
class NetworkStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val mNetReceiver = NetworkChangeReceiver()
    private val mNetFilter = IntentFilter().also {
        it.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        context.registerReceiver(mNetReceiver, mNetFilter)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        context.unregisterReceiver(mNetReceiver)
    }

    private fun onNetworkChanged(type: Int) {
        when (type) {
            NETWORK_TYPE_ETHERNET -> setImageResource(R.drawable.amtv_ic_network_ethernet)
            NETWORK_TYPE_WIFI -> setImageResource(R.drawable.amtv_ic_network_wifi)
            NETWORK_TYPE_NONE -> {
                setImageResource(R.drawable.amtv_ic_network_disable)
            }
            else -> {
                setImageResource(R.drawable.amtv_ic_network_wifi)
            }
        }
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            tryCatch {
                val networkType = context.getNetworkType()
                onNetworkChanged(networkType)
            }
        }
    }
}