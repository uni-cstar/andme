package andme.media.player.core

import android.view.View

/**
 * Created by Lucio on 2021/4/15.
 */
interface AMPlayer2 {

    /**
     * 设置播放数据
     */
    fun setDataSource(url: String)

    /**
     * 开始播放
     */
    fun start()

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
     * @param positionMs ms
     */
    fun seekTo(positionMs: Int)

    /**
     * 获取持续时间
     * @return ms
     */
    fun getDuration(): Int

    /**
     * 获取当前播放位置
     * @return ms
     */
    fun getCurrentPosition(): Int

    /**
     * 缓存位置
     * @return ms
     */
    fun getBufferingPosition(): Int

    /**
     * 停止播放
     */
    fun stopPlayback()

    fun addCompletionListener(listener:OnAMPlayerCompleteListener)
    fun removeCompletionListener(listener:OnAMPlayerCompleteListener)

    fun addErrorListener(listener: OnAMPlayerErrorListener)
    fun removeErrorListener(listener: OnAMPlayerErrorListener)

    fun addPreparedListener(listener: OnAMPlayerPreparedListener)
    fun removePreparedListener(listener: OnAMPlayerPreparedListener)

    fun addInfoListener(listener:OnAMPlayerInfoListener)
    fun removeInfoListener(listener:OnAMPlayerInfoListener)


//
//    companion object {
//        /**
//         * The player does not have any media to play.
//         * 初始状态 播放器没有工作中（播放器已停止/播放失败）
//         */
//        const val STATE_IDLE = 1
//
//        /**
//         * 缓冲中，无法进行播放
//         * The player is not able to immediately play from its current position. This state typically
//         * occurs when more data needs to be loaded.
//         */
//        const val STATE_BUFFERING = 2
//
//        /**
//         * 播放已就绪；可以立即开始播放
//         * The player is able to immediately play from its current position. The player will be playing if
//         * [.getPlayWhenReady] is true, and paused otherwise.
//         */
//        const val STATE_READY = 3
//
//        /**
//         * 播放结束
//         * The player has finished playing the media.
//         */
//        const val STATE_ENDED = 4
//    }
//
//    /**
//     * Playback state. One of [.STATE_IDLE], [.STATE_BUFFERING], [.STATE_READY] or
//     * [.STATE_ENDED].
//     */
//    @Documented
//    @Retention(RetentionPolicy.SOURCE)
//    @IntDef(STATE_IDLE, STATE_BUFFERING, STATE_READY, STATE_ENDED)
//    annotation class State
//
//
//    fun interface OnAMPlayerStateChangedListener {
//        fun onAMPlayerStateChanged(@State state: Int)
//    }

    /**
     * 错误结束回调
     */
    fun interface OnAMPlayerErrorListener {
        fun onAMPlayerError(player: AMPlayer2, e: Throwable)
    }

    /**
     * 播放结束回调
     */
    fun interface OnAMPlayerCompleteListener {
        fun onAMPlayerComplete(player: AMPlayer2)
    }

    /**
     * 播放已准备回调
     */
    fun interface OnAMPlayerPreparedListener {
        fun onAMPlayerPrepared(player: AMPlayer2)
    }

    /**
     * 信息回调
     */
    fun interface OnAMPlayerInfoListener {
        fun onAMPlayerInfo(player: AMPlayer2, what: Int, extra: Int)
    }


    interface Control{

        /**
         * 显示控制器
         * @param timeout 超时时间，即超过多少时间之后隐藏
         */
        fun show(timeout:Long)

        fun hide()

        /**
         * 设置播放器
         */
        fun setPlayer(player:AMPlayer2?)


        /**
         * Adds a [VisibilityListener].
         *
         * @param listener The listener to be notified about visibility changes.
         */
        fun addVisibilityListener(listener: VisibilityListener)

        /**
         * Removes a [VisibilityListener].
         *
         * @param listener The listener to be removed.
         */
        fun removeVisibilityListener(listener: VisibilityListener)

        /** Listener to be notified about changes of the visibility of the UI control.  */
        fun interface VisibilityListener {
            /**
             * Called when the visibility changes.
             *
             * @param visibility The new visibility. Either [View.VISIBLE] or [View.GONE].
             */
            fun onVisibilityChange(visibility: Int)
        }


    }
}