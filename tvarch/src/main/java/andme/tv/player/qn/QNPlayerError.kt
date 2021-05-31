package andme.tv.player.qn

import com.pili.pldroid.player.PLOnErrorListener

/**
 * Created by Lucio on 2021/4/15.
 */
class QNPlayerError(val errCode: Int) : RuntimeException() {

    override val message: String
        get() {
            return when (errCode) {

                PLOnErrorListener.ERROR_CODE_OPEN_FAILED -> {
                    "播放器打开失败"
                }
                PLOnErrorListener.ERROR_CODE_IO_ERROR -> {
                    "网络异常"
                }
                PLOnErrorListener.ERROR_CODE_SEEK_FAILED -> {
                    "拖动失败"
                }
                PLOnErrorListener.ERROR_CODE_CACHE_FAILED -> {
                    "预加载失败"
                }
                PLOnErrorListener.ERROR_CODE_HW_DECODE_FAILURE -> {
                    "硬解失败"
                }
                PLOnErrorListener.ERROR_CODE_PLAYER_DESTROYED -> {
                    "播放器已被销毁，需要再次 setVideoURL 或 prepareAsync"
                }
                PLOnErrorListener.ERROR_CODE_PLAYER_VERSION_NOT_MATCH -> {
                    "so 库版本不匹配，需要升级"
                }
                PLOnErrorListener.ERROR_CODE_PLAYER_CREATE_AUDIO_FAILED -> {
                    "AudioTrack 初始化失败，可能无法播放音频"
                }
                else -> {
                    "未知错误"
                }
            }
        }
}