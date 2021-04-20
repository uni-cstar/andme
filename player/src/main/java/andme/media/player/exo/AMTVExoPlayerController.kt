package andme.media.player.exo

import andme.media.player.R
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.google.android.exoplayer2.ui.PlayerControlView

/**
 * Created by Lucio on 2021/4/16.
 */
class AMTVExoPlayerController @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : PlayerControlView(context, attrs, defStyleAttr) {

    private val timerBar: View?
    init {
        timerBar = findViewById(R.id.exo_progress)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val keyCode = event.keyCode
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_LEFT -> {
                return timerBar?.dispatchKeyEvent(event) ?: super.dispatchKeyEvent(event)
            }
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    //如果是按下中心键，则发送一个播放或者暂停的信息
                    dispatchMediaKeyEvent(
                        KeyEvent(
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                        )
                    )
                    return true
                }
            }
        }

        return super.dispatchKeyEvent(event)
    }

}