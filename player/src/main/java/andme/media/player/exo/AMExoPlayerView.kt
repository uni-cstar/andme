//package andme.media.player.exo
//
//import andme.lang.orDefault
//import andme.media.player.core.AMPlayer
//import android.content.Context
//import android.util.AttributeSet
//import android.view.View
//import com.google.android.exoplayer2.ExoPlaybackException
//import com.google.android.exoplayer2.MediaItem
//import com.google.android.exoplayer2.Player
//import com.google.android.exoplayer2.SimpleExoPlayer
//import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
//import com.google.android.exoplayer2.ui.PlayerView
//import com.google.android.exoplayer2.upstream.HttpDataSource.HttpDataSourceException
//import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException
//import java.io.IOException
//import java.util.concurrent.CopyOnWriteArraySet
//
//
///**
// * Created by Lucio on 2021/4/15.
// */
//class AMExoPlayerView @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//) : PlayerView(context, attrs, defStyleAttr), AMPlayer {
//
//    private val stateListeners: CopyOnWriteArraySet<AMPlayer.OnAMPlayerStateChangedListener> =
//        CopyOnWriteArraySet()
//    private val errorListeners: CopyOnWriteArraySet<AMPlayer.OnAMPlayerErrorListener> =
//        CopyOnWriteArraySet()
//
//    /**
//     * 是否允许内容比例自动更改（即根据控件大小变化内容区域）
//     */
//    private var enableContentAspectRatioChanged: Boolean = true
//
//    init {
//        player = SimpleExoPlayer.Builder(context).build()
//        player?.addListener(object : Player.EventListener {
//
//            override fun onPlaybackStateChanged(state: Int) {
//
//                val myState = when (state) {
//                    Player.STATE_READY -> {
//                        AMPlayer.STATE_READY
//                    }
//                    Player.STATE_ENDED -> {
//                        AMPlayer.STATE_ENDED
//                    }
//
//                    Player.STATE_BUFFERING -> {
//                        AMPlayer.STATE_BUFFERING
//                    }
//                    else -> {
//                        AMPlayer.STATE_IDLE
//                    }
//                }
//                stateListeners.forEach {
//                    it.onAMPlayerStateChanged(myState)
//                }
//            }
//
//
//            override fun onPlayerError(error: ExoPlaybackException) {
//                errorListeners.forEach {
//                    it.onAMPlayerError(error)
//                }
//
//                if (error.type == ExoPlaybackException.TYPE_SOURCE) {
//                    val cause: IOException = error.sourceException
//                    if (cause is HttpDataSourceException) {
//                        // An HTTP error occurred.
//                        val httpError = cause
//                        // This is the request for which the error occurred.
//                        val requestDataSpec = httpError.dataSpec
//                        // It's possible to find out more about the error both by casting and by
//                        // querying the cause.
//                        if (httpError is InvalidResponseCodeException) {
//                            // Cast to InvalidResponseCodeException and retrieve the response code,
//                            // message and headers.
//                        } else {
//                            // Try calling httpError.getCause() to retrieve the underlying cause,
//                            // although note that it may be null.
//                        }
//                    }
//                }
//            }
//        })
//    }
//
//
//    override fun setDataSource(url: String) {
//        player?.setMediaItem(MediaItem.fromUri(url))
//    }
//
//    override fun prepare() {
//        player?.prepare()
//    }
//
//    override fun start() {
////        player?.let {
////            if(it.play())
////        }
//        player?.play()
//    }
//
//    override fun pause() {
//        player?.stop()
//    }
//
//    override fun stopAndReset() {
//        player?.stop(true)
//    }
//
//    override fun isPlaying(): Boolean {
//        return player?.isPlaying.orDefault()
//    }
//
//    override fun stopPlayback() {
//        player?.release()
//    }
//
//    override fun seekTo(positionMs: Long) {
//        player?.seekTo(positionMs)
//    }
//
//    override fun getDuration(): Long {
//        return player?.duration.orDefault()
//    }
//
//    override fun getCurrentPosition(): Long {
//        return player?.currentPosition.orDefault()
//    }
//
//    override fun getBufferingPosition(): Long {
//        return player?.contentBufferedPosition.orDefault()
//    }
//
//    override fun addPlayerStateChangedListener(listener: AMPlayer.OnAMPlayerStateChangedListener) {
//        stateListeners.add(listener)
//    }
//
//    override fun removePlayerStateChangedListener(listener: AMPlayer.OnAMPlayerStateChangedListener) {
//        stateListeners.remove(listener)
//    }
//
//    override fun addPlayerErrorListener(listener: AMPlayer.OnAMPlayerErrorListener) {
//        errorListeners.add(listener)
//    }
//
//    override fun removePlayerErrorListener(listener: AMPlayer.OnAMPlayerErrorListener) {
//        errorListeners.remove(listener)
//    }
//
//    override fun getCurrentState(): Int {
//        return player?.playbackState.orDefault(Player.STATE_IDLE)
//    }
//
//    /**
//     * 是否应该显示暂停按钮
//     */
//    override fun shouldShowPauseButton(): Boolean {
//        return player != null && player!!.playbackState != Player.STATE_ENDED && player!!.playbackState != Player.STATE_IDLE && player!!.playWhenReady
//    }
//
////    override fun showController() {
////    }
//
//
//    fun setEnableContentAspectRatioChanged(isEnable: Boolean) {
//        enableContentAspectRatioChanged = isEnable
//    }
//
//    override fun onContentAspectRatioChanged(
//        contentAspectRatio: Float,
//        contentFrame: AspectRatioFrameLayout?,
//        contentView: View?
//    ) {
//        super.onContentAspectRatioChanged(
//            if (!enableContentAspectRatioChanged) 0f else contentAspectRatio,
//            contentFrame,
//            contentView
//        )
//    }
//}