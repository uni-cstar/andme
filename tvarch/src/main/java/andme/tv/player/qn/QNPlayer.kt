package andme.tv.player.qn

import andme.core.appManagerAM
import com.pili.pldroid.player.AVOptions

/**
 * Created by Lucio on 2021/4/15.
 */
object QNPlayer {

    /**
     * 打开重试次数，设置后若打开流地址失败，则会进行重试
     */
    const val RETRY_TIMES = 3

    /**
     * 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
     */
    const val AV_PREPARE_TIMEOUT = 10 * 1000

    private val logLevel: Int get() = if (appManagerAM.isDebuggable) 3 else 5

    private fun DefaultAVOptions(): AVOptions {
        return AVOptions().apply {
            //硬解优先；失败后切换成软解
            setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO)
//            //快开模式，启用后会加快该播放器实例再次打开相同协议的视频流的速度
//            setInteger(AVOptions.KEY_FAST_OPEN, 1)
            setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, RETRY_TIMES)
            setInteger(AVOptions.KEY_LOG_LEVEL, logLevel)
            setInteger(AVOptions.KEY_PREPARE_TIMEOUT, AV_PREPARE_TIMEOUT)
        }
    }

    fun LiveOption(): AVOptions {
        return DefaultAVOptions().apply {
            setInteger(AVOptions.KEY_LIVE_STREAMING, 1)
        }
    }

    /**
     * 创建点播参数
     * @param startPosition 开始播放时间，单位ms
     */
    fun VideoPlayOptions(startPosition: Int = 0): AVOptions {
        return DefaultAVOptions().apply {
            //默认的缓存大小，单位是 ms ;默认值是：500
            setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 500)
            //最大的缓存大小，单位是 ms; 默认值是：2000，若设置值小于 KEY_CACHE_BUFFER_DURATION 则不会生效
            setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000)
            //是否开启直播优化，1 为开启，0 为关闭。若开启，视频暂停后再次开始播放时会触发追帧机制
            setInteger(AVOptions.KEY_LIVE_STREAMING, 0)
            // 设置拖动模式，1 位精准模式，即会拖动到时间戳的那一秒；0 为普通模式，会拖动到时间戳最近的关键帧。默认为 0
            setInteger(AVOptions.KEY_SEEK_MODE, 1)
            if (startPosition > 0) {
                setInteger(AVOptions.KEY_START_POSITION, startPosition)
            }
        }
    }


}