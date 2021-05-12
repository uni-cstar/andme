package andme.media.player.videoview

import andme.media.player.core.AMPlayer2
import andme.media.player.core.AMPlayerException
import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.widget.VideoView
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by Lucio on 2021/4/21.
 */
class AMVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : VideoView(context, attrs, defStyleAttr), AMPlayer2 {

    private var completeListeners: CopyOnWriteArrayList<AMPlayer2.OnAMPlayerCompleteListener> =
        CopyOnWriteArrayList<AMPlayer2.OnAMPlayerCompleteListener>()
    private var errorListeners: CopyOnWriteArrayList<AMPlayer2.OnAMPlayerErrorListener> =
        CopyOnWriteArrayList<AMPlayer2.OnAMPlayerErrorListener>()
    private var preparedListeners: CopyOnWriteArrayList<AMPlayer2.OnAMPlayerPreparedListener> =
        CopyOnWriteArrayList<AMPlayer2.OnAMPlayerPreparedListener>()
    private var infoListeners: CopyOnWriteArrayList<AMPlayer2.OnAMPlayerInfoListener> =
        CopyOnWriteArrayList<AMPlayer2.OnAMPlayerInfoListener>()

    private val internalCompleteListener = MediaPlayer.OnCompletionListener {
        completeListeners.forEach {
            it.onAMPlayerComplete(this)
        }
    }

    private val internalErrorListener = MediaPlayer.OnErrorListener { mediaPlayer, what, extra ->

        if (errorListeners.isNotEmpty()) {
            errorListeners.forEach {
                it.onAMPlayerError(this, AMPlayerException(what, extra))
            }
            true
        } else {
            false
        }

    }

    private val internalPreparedListener = MediaPlayer.OnPreparedListener {
        preparedListeners.forEach {
            it.onAMPlayerPrepared(this)
        }
    }


    private val internalInfoListener = MediaPlayer.OnInfoListener { mp, what, extra ->
        infoListeners.forEach {
            it.onAMPlayerInfo(this, what, extra)
        }
        true
    }

    init {
        setOnCompletionListener(internalCompleteListener)
        setOnErrorListener(internalErrorListener)
        setOnPreparedListener(internalPreparedListener)
        setOnInfoListener(internalInfoListener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(0, widthMeasureSpec)
        val height = getDefaultSize(0, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun setDataSource(url: String) {
        setVideoPath(url)
    }

    override fun getBufferingPosition(): Int {
        return bufferPercentage * duration
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
        preparedListeners.remove(listener)
    }

    override fun addInfoListener(listener: AMPlayer2.OnAMPlayerInfoListener) {
        infoListeners.add(listener)
    }

    override fun removeInfoListener(listener: AMPlayer2.OnAMPlayerInfoListener) {
        infoListeners.remove(listener)
    }




}