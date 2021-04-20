package andme.media.player.core

import androidx.annotation.IntDef
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by Lucio on 2021/4/15.
 */
interface AMPlayer {

    fun setDataSource(url: String)

    fun prepare()

    fun start()

    fun stop()

    fun stopAndReset()

    fun isPlaying(): Boolean

    fun release()

    fun seekTo(positionMs: Long)

    fun getDuration(): Long

    fun getCurrentPosition(): Long

    fun getBufferingPosition():Long

    fun addPlayerStateChangedListener(listener: OnAMPlayerStateChangedListener)
    fun removePlayerStateChangedListener(listener: OnAMPlayerStateChangedListener)

    fun addPlayerErrorListener(listener: OnAMPlayerErrorListener)
    fun removePlayerErrorListener(listener: OnAMPlayerErrorListener)

    /**
     * 是否当前应该显示暂停按钮
     */
    fun shouldShowPauseButton():Boolean

    @State
    fun getCurrentState():Int

    companion object{
        /**
         * The player does not have any media to play.
         * 初始状态 播放器没有工作中（播放器已停止/播放失败）
         */
        const val STATE_IDLE = 1

        /**
         * 缓冲中，无法进行播放
         * The player is not able to immediately play from its current position. This state typically
         * occurs when more data needs to be loaded.
         */
        const val  STATE_BUFFERING = 2

        /**
         * 播放已就绪；可以立即开始播放
         * The player is able to immediately play from its current position. The player will be playing if
         * [.getPlayWhenReady] is true, and paused otherwise.
         */
        const val  STATE_READY = 3

        /**
         * 播放结束
         * The player has finished playing the media.
         */
        const val  STATE_ENDED = 4
    }
    /**
     * Playback state. One of [.STATE_IDLE], [.STATE_BUFFERING], [.STATE_READY] or
     * [.STATE_ENDED].
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(STATE_IDLE, STATE_BUFFERING, STATE_READY, STATE_ENDED)
    annotation class State


    fun interface OnAMPlayerStateChangedListener{
        fun onAMPlayerStateChanged(@State state:Int)
    }

    fun interface OnAMPlayerErrorListener{
        fun onAMPlayerError(e:Throwable)
    }
}