package andme.media.player.core

import andme.core.ctxAM
import andme.core.util.isNetworkConnected
import andme.lang.orDefaultIfNullOrEmpty
import android.media.MediaPlayer
import androidx.annotation.IntDef

/**
 * Created by Lucio on 2021/6/2.
 */


class AMPlayerException(val sourceReason: Throwable, @ErrorCode val code: Int) : Throwable() {

    @IntDef(value = [ERROR_UNKNOWN, ERROR_IO])
    annotation class ErrorCode

    /**
     * 用于MediaPlayer获取原始错误
     */
    val mediaPlayerException: MediaPlayerException? get() = sourceReason as? MediaPlayerException

    fun getFriendlyMessage(isLive: Boolean): String {
        if (isLive) {
            return getLiveFriendlyMessage()
        } else {
            return getVodFriendlyMessage()
        }
    }

    private fun getLiveFriendlyMessage(): String {
        return when (code) {
            ERROR_IO -> {
                if (ctxAM.isNetworkConnected()) {
                    "直播已断开"
                } else {
                    "连接失败（网络错误或服务器已断开）"
                }
            }
            else -> {
                sourceReason.message.orDefaultIfNullOrEmpty("未知异常。")
            }
        }
    }

    private fun getVodFriendlyMessage(): String {
        return when (code) {
            ERROR_IO -> {
                "连接失败（网络错误或服务器已断开）"
            }
            else -> {
                sourceReason.message.orDefaultIfNullOrEmpty("未知异常。")
            }
        }
    }

    companion object {
        const val ERROR_UNKNOWN = MediaPlayer.MEDIA_ERROR_UNKNOWN
        const val ERROR_IO = MediaPlayer.MEDIA_ERROR_IO

        fun newMediaPlayerException(what: Int, extra: Int): AMPlayerException {
            val reason = MediaPlayerException(what, extra)
            return when (extra) {
                MediaPlayer.MEDIA_ERROR_IO ->
                    AMPlayerException(reason, ERROR_IO)
                else -> {
                    AMPlayerException(reason, ERROR_UNKNOWN)
                }
            }
        }

        fun newException(reason: Throwable, @ErrorCode code: Int): AMPlayerException {
            return AMPlayerException(reason, code)
        }
    }
}