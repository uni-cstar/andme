package andme.tv.player.qn

import andme.core.exception.friendlyMessage
import andme.core.exception.onCatch
import andme.core.exception.tryCatch
import andme.media.player.core.AMPlayer2
import andme.media.player.core.AMPlayerException
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.pili.pldroid.player.*
import com.pili.pldroid.player.widget.PLVideoView
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by Lucio on 2021/4/14.
 */
class QNPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), AMPlayer2 {

    val kneral: PLVideoView

    //关于seek，当前一次seek未完成时进行seek操作，会回调[PLOnInfoListener(what=MEDIA_INFO_IS_SEEKING)],
    //当seek完成之后会回调seekcomplete方法，此时再重新seek之前未成功的seek操作
    //缓存的当前期望seek的位置
    private var pendingSeekPosition = NONE_SEEK_POSITION

    //是否在seeking中
    private var seeking = false

    private val completeListeners: CopyOnWriteArrayList<AMPlayer2.OnAMPlayerCompleteListener> =
        CopyOnWriteArrayList<AMPlayer2.OnAMPlayerCompleteListener>()

    private val errorListeners: CopyOnWriteArrayList<AMPlayer2.OnAMPlayerErrorListener> =
        CopyOnWriteArrayList<AMPlayer2.OnAMPlayerErrorListener>()

    private val preparedListeners: CopyOnWriteArrayList<AMPlayer2.OnAMPlayerPreparedListener> =
        CopyOnWriteArrayList<AMPlayer2.OnAMPlayerPreparedListener>()

    private val infoListeners: CopyOnWriteArrayList<AMPlayer2.OnAMPlayerInfoListener> =
        CopyOnWriteArrayList<AMPlayer2.OnAMPlayerInfoListener>()

    private val stateListeners = CopyOnWriteArrayList<AMPlayer2.OnAMPlayerStateListener>()


    private val internalPreparedListener = PLOnPreparedListener {
        preparedListeners.forEach {
            it.onAMPlayerPrepared(this)
        }
    }

    private val internalCompleteListener = PLOnCompletionListener {
        completeListeners.forEach {
            it.onAMPlayerComplete(this)
        }
    }

    private val internalErrorListener = PLOnErrorListener {

        var handledException = false

        if (errorListeners.isNotEmpty()) {
            val error =
                AMPlayerException.newException(QNPlayerError(it), AMPlayerException.ERROR_UNKNOWN)
            errorListeners.forEach {
                if (it.onAMPlayerError(this, error)) {
                    handledException = true
                }
            }
        }
        handledException
    }

    private val internalInfoListener = PLOnInfoListener { what, extra ->
        when (what) {
            PLOnInfoListener.MEDIA_INFO_BUFFERING_START -> {
                stateListeners.forEach {
                    it.onAMPlayerStateBuffering()
                }
            }
            PLOnInfoListener.MEDIA_INFO_BUFFERING_END -> {
                stateListeners.forEach {
                    it.onAMPlayerStateBufferingEnd()
                }
            }
            //开始播放音频/视频
            PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START,
            PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START -> {
                stateListeners.forEach {
                    it.onAMPlayerStateBufferingEnd()
                }
                stateListeners.forEach {
                    it.onAMPlayerStateStartPlaying()
                }
            }
            PLOnInfoListener.MEDIA_INFO_IS_SEEKING -> {
                seeking = true
            }
        }

        infoListeners.forEach {
            it.onAMPlayerInfo(this, what, extra)
        }
    }

    private val internalSeekCompleteListener = PLOnSeekCompleteListener {
        synchronized(pendingSeekPosition) {
            tryCatch {
                if (seeking && pendingSeekPosition != NONE_SEEK_POSITION) {
                    //重新执行未完成的最后一次seek操作
                    val seekPosition = pendingSeekPosition.toInt()
                    pendingSeekPosition = NONE_SEEK_POSITION
                    seeking = false
                    seekTo(seekPosition)
                }
            }.onCatch {
                it.printStackTrace()
                Log.e("TAG", it.friendlyMessage)
            }
        }
    }

    init {
        kneral = PLVideoView(context)
        addView(kneral, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        kneral.displayAspectRatio = PLVideoView.ASPECT_RATIO_FIT_PARENT
        kneral.setOnPreparedListener(internalPreparedListener)
        kneral.setOnCompletionListener(internalCompleteListener)
        //关于IOError
        // 请查看 5.3 连接状态处理章节处理逻辑
        // https://developer.qiniu.com/pili/1210/the-android-client-sdk#Advanced-features
        kneral.setOnErrorListener(internalErrorListener)
        kneral.setOnInfoListener(internalInfoListener)
        kneral.setOnSeekCompleteListener(internalSeekCompleteListener)
    }

    override fun setDataSource(url: String) {
        kneral.setVideoPath(url)
    }

    override fun setDataSource(url: String, headers: Map<String, String>) {
        kneral.setVideoPath(url,headers)
    }

    override fun start() {
        kneral.start()
    }

    override fun pause() {
        if (kneral.canPause())
            kneral.pause()
    }

    override fun isPlaying(): Boolean {
        return kneral.isPlaying
    }

    override fun seekTo(positionMs: Int) {
        pendingSeekPosition = positionMs.toLong()
        kneral.seekTo(pendingSeekPosition)
    }

    override fun getDuration(): Int {
        return kneral.duration.toInt()
    }

    override fun getCurrentPosition(): Int {
        return kneral.currentPosition.toInt()
    }

    override fun getBufferingPosition(): Int {
        return (kneral.bufferPercentage * kneral.duration).toInt()
    }

    override fun stopPlayback() {
        kneral.stopPlayback()
    }

    override fun addCompletionListener(listener: AMPlayer2.OnAMPlayerCompleteListener) {
        completeListeners.add(listener)
    }

    override fun removeCompletionListener(listener: AMPlayer2.OnAMPlayerCompleteListener) {
        completeListeners.remove(listener)
    }

    override fun addErrorListener(listener: AMPlayer2.OnAMPlayerErrorListener) {
        errorListeners.add(listener)
    }

    override fun removeErrorListener(listener: AMPlayer2.OnAMPlayerErrorListener) {
        errorListeners.remove(listener)
    }

    override fun addPreparedListener(listener: AMPlayer2.OnAMPlayerPreparedListener) {
        preparedListeners.add(listener)
    }

    override fun removePreparedListener(listener: AMPlayer2.OnAMPlayerPreparedListener) {
        preparedListeners.add(listener)
    }

    override fun addInfoListener(listener: AMPlayer2.OnAMPlayerInfoListener) {
        infoListeners.add(listener)
    }

    override fun removeInfoListener(listener: AMPlayer2.OnAMPlayerInfoListener) {
        infoListeners.remove(listener)
    }

    override fun addStateListener(listener: AMPlayer2.OnAMPlayerStateListener) {
        stateListeners.add(listener)
    }

    override fun removeStateListener(listener: AMPlayer2.OnAMPlayerStateListener) {
        stateListeners.remove(listener)
    }


    companion object {
        private const val TAG = "QNPlayerView"
        const val NONE_SEEK_POSITION = -1L
    }
}