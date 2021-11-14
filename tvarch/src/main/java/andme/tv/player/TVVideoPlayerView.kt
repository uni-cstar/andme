//package andme.tv.player
//
//import android.view.View
//import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
//import java.io.File
//
//typealias VideoPlayerCallback = GSYSampleCallBack
//
///**
// * Created by Lucio on 2021/4/1.
// */
//interface TVVideoPlayerView {
//
//    /**
//     * 从哪里开始播放
//     * 目前有时候前几秒有跳动问题，毫秒
//     * 需要在startPlayLogic之前，即播放开始之前
//     */
//    fun setSeekBeforeStart(position:Long)
//
//    fun seekTo(position: Long)
//
//    /**
//     * 开始播放；必须先调用[setDataSource]设置数据源之后才能调用此方法
//     */
//    fun startPlay()
//
//    /**
//     * 设置播放URL
//     *
//     * @param url           播放url
//     * @param cacheWithPlay 是否边播边缓存
//     * @param title         title
//     * @return
//     */
//    fun setDataSource(url: String, cacheWithPlay: Boolean, title: String?): Boolean =
//        setDataSource(url, cacheWithPlay, null, title)
//
//    /**
//     * 设置播放URL
//     *
//     * @param url           播放url
//     * @param cacheWithPlay 是否边播边缓存
//     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
//     * @param title         title
//     * @return
//     */
//    fun setDataSource(
//        url: String,
//        cacheWithPlay: Boolean,
//        cachePath: File?,
//        title: String?
//    ): Boolean
//
//    /**
//     * 设置播放URL
//     *
//     * @param url           播放url
//     * @param cacheWithPlay 是否边播边缓存
//     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
//     * @param mapHeadData   头部信息
//     * @param title         title
//     * @return
//     */
//    fun setDataSource(
//        url: String,
//        cacheWithPlay: Boolean,
//        cachePath: File?,
//        mapHeadData: Map<String, String>?,
//        title: String?
//    ): Boolean
//
//    /**
//     * 设置回调
//     */
//    fun setCallBack(callback: VideoPlayerCallback)
//
//    /***
//     * 设置封面View
//     */
//    fun setThumbImageView(view: View)
//
//    /***
//     * 清除封面
//     */
//    fun clearThumbImageView()
//
//    /**
//     * 获取控制view
//     */
//    fun getPlayerControlView(): TVVideoPlayerControlView
//
//}
//
//
//interface TVVideoPlayerControlView{
//
//    /**
//     * 切换界面到加载中
//     */
//    fun changeUIToPrepareLoading()
//}