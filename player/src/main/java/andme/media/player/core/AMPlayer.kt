package andme.media.player.core

import androidx.annotation.IntDef
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by Lucio on 2021/4/15.
 */
interface AMPlayer {

    /**
     * 设置播放数据
     */
    fun setDataSource(url: String)

    /**
     * 开始播放
     */
    fun start()

    fun prepare()

    /**
     * 暂停播放
     */
    fun pause()

    /**
     * 是否正在播放
     */
    fun isPlaying(): Boolean

    /**
     * 从指定位置开始播放
     */
    fun seekTo(positionMs: Long)

    /**
     * 获取持续时间
     */
    fun getDuration(): Long

    /**
     * 获取当前播放位置
     */
    fun getCurrentPosition(): Long

    /**
     * 缓存位置
     */
    fun getBufferingPosition():Long

    /**
     * 停止播放
     */
    fun stopPlayback()



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

     fun stopAndReset()

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
        fun onAMPlayerStateChanged(@State state: Int)
    }

    fun interface OnAMPlayerErrorListener{
        fun onAMPlayerError(e: Throwable)
    }

    /**
     * 播放结束回调
     */
    fun interface OnAMPlayerCompleteListener{
        fun onAMPlayerComplete()
    }
}