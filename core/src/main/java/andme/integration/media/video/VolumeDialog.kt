package andme.integration.media.video

import andme.core.R
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView

/**
 * Created by Lucio on 2021/4/8.
 * 音量对话框，处理音量加减
 */
class VolumeDialog(
    ctx: Context, val playerView: View,
    var streamType: Int = AudioManager.STREAM_MUSIC
) : VideoToastDialog(ctx) {

    private lateinit var mProgressBar: ProgressBar
    private lateinit var mValueText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.apply {
            val localLayoutParams: WindowManager.LayoutParams = attributes
            localLayoutParams.gravity = Gravity.TOP or Gravity.START
            localLayoutParams.width = playerView.width
            localLayoutParams.height = playerView.height
            val location = IntArray(2)
            playerView.getLocationOnScreen(location)
            localLayoutParams.x = location[0]
            localLayoutParams.y = location[1]
            attributes = localLayoutParams
        }

        mProgressBar = findViewById(R.id.volume_progressbar)
        mValueText = findViewById(R.id.volume_value_tv)

        val audioManager =
            context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val max = audioManager.getStreamMaxVolume(streamType)
        val current = audioManager.getStreamVolume(streamType)
        updateRenderValue(current, max)
    }

    /**
     * @param current 当前音量
     * @param max 最大音量
     */
    fun updateRenderValue(current: Int, max: Int) {
        mValueText.text = current.toString()
        mProgressBar.progress = (current.toFloat() * 100 / max).toInt()
    }

    override fun getContentLayoutId(): Int {
        return R.layout.am_media_video_volume_dialog
    }

//    fun handleMute(manager: AudioManager){
//        manager.setStreamMute()
//    }

    /**
     * 处理音量+键；音量增加1
     */
    fun handleVolumeByUpKeyEvent(manager: AudioManager, autoDelay: Long = 2000) {
        showWithDelayDismiss(autoDelay)
        val max = manager.getStreamMaxVolume(streamType)
        val current = manager.getStreamVolume(streamType)
        if (current < max) {
            val newValue = current + 1
            manager.setStreamVolume(streamType, newValue, 0)
            updateRenderValue(newValue, max)
        }else{
            updateRenderValue(current, max)
        }
    }

    /**
     * 处理音量-键；音量减1
     */
    fun handleVolumeByDownKeyEvent(manager: AudioManager, autoDelay: Long = 2000) {
        showWithDelayDismiss(autoDelay)
        val max = manager.getStreamMaxVolume(streamType)
        val current = manager.getStreamVolume(streamType)
        if (current > 0) {
            val newValue = current - 1
            manager.setStreamVolume(streamType, newValue, 0)
            updateRenderValue(newValue, max)
        }else{
            updateRenderValue(current, max)
        }
    }

}