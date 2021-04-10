package andme.tv.player

import andme.tv.player.internal.AMTVVideoPlayer
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.FrameLayout
import com.shuyu.gsyvideoplayer.GSYVideoManager

/**
 * Created by Lucio on 2021/4/1.
 */
class TVVideoPlayer  private constructor(
    private val mPlayer: AMTVVideoPlayer,
    context: Context, attrs: AttributeSet?, defStyleAttr: Int,
) : FrameLayout(context, attrs, defStyleAttr), TVVideoPlayerView by mPlayer {

    @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
    ):this(AMTVVideoPlayer(context, attrs), context, attrs, defStyleAttr)
    
    init {
        addView(
            mPlayer,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
        setPlayerViewTouchMode(true)
    }

    fun setPlayerViewTouchMode(isFocusable: Boolean){
        mPlayer.isFocusable = isFocusable
        mPlayer.isFocusableInTouchMode = isFocusable
        mPlayer.isClickable = true
    }

    fun setCoreRequestFocus(){
        mPlayer.requestFocus()
    }
    
    @Deprecated("不建议直接使用，避免不好替换内核实现")
     fun getCore(): AMTVVideoPlayer {
        return mPlayer
    }

    fun onVideoPause(){
        mPlayer.onVideoPause()
    }

    fun onVideoResume(){
        mPlayer.onVideoResume()
    }

    fun onDestroy(){
        mPlayer.release()
        GSYVideoManager.releaseAllVideos()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){

        }
        return super.onKeyDown(keyCode, event)

    }
}