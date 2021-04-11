package andme.tv.player.internal

import andme.integration.media.video.VolumeDialog
import andme.tv.arch.R
import andme.tv.player.PlayerLogger
import andme.tv.player.TVVideoPlayerControlView
import andme.tv.player.TVVideoPlayerView
import andme.tv.player.VideoPlayerCallback
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import java.io.File
import kotlin.math.ceil

/**
 * Created by Lucio on 2021/4/8.
 */
open class AMTVVideoPlayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : GSYVideoPlayer(context, attrs, defStyleAttr), TVVideoPlayerView,
    TVVideoPlayerControlView {

    protected val mControlView: TVVideoControlView

    private lateinit var mCustomVolumeDialog: VolumeDialog


    /**
     * 步长倍数，最多8倍
     */
    private var mSeekScale = 1

    /**
     * 持续seek的标记位
     */
    private var mSeekContinueFlag: Boolean = false


    private var mSeekPositionChanged: Long = 0
    private var mSeekMaxDuration: Long = 0


    /**
     * 上一次渲染seek的时间
     */
    private var mLastRenderSeekTime: Long = 0


    init {
        mControlView = findViewById(R.id.control_view)
        //设置自动隐藏时间
        dismissControlTime = 8000
    }

    override fun startPlayLogic() {
        prepareVideo()
    }

    override fun showWifiDialog() {
    }

    override fun showProgressDialog(
        deltaX: Float,
        seekTime: String?,
        seekTimePosition: Int,
        totalTime: String?,
        totalTimeDuration: Int
    ) {
    }

    override fun dismissProgressDialog() {
    }


    override fun showBrightnessDialog(percent: Float) {
    }

    override fun dismissBrightnessDialog() {
    }

    override fun onClickUiToggle(e: MotionEvent?) {
    }

    override fun hideAllWidget() {
        mControlView.hideAllWidget()
    }

    override fun changeUiToNormal() {
        mControlView.renderInit()
    }

    override fun changeUiToPreparingShow() {
        mControlView.renderPreparing()
    }

    override fun changeUiToPlayingShow() {
        mControlView.renderStartPlaying()
    }

    override fun changeUiToPauseShow() {
        mControlView.renderPause()
    }

    override fun changeUiToError() {
        mControlView.renderError()
    }

    override fun changeUiToCompleteShow() {
        mControlView.renderComplete()

    }

    override fun changeUiToPlayingBufferingShow() {
        mControlView.renderStartBuffering()
    }

    override fun setProgressAndTime(
        progress: Int,
        secProgress: Int,
        currentTime: Int,
        totalTime: Int,
        forceChange: Boolean
    ) {
        if (mSeekContinueFlag)
            return
        PlayerLogger.d("setProgressAndTime(progress=${progress},currentTime=${currentTime}),totalTime=${totalTime}")
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime, forceChange)
        mControlView.setProgressAndTime(progress, secProgress, currentTime, totalTime)
    }

    override fun resetProgressAndTime() {
        if (mSeekContinueFlag)
            return
        super.resetProgressAndTime()
        mControlView.setProgressAndTime(0, 0, 0, 0)
    }

    override fun loopSetProgressAndTime() {
        if (mSeekContinueFlag)
            return
        super.loopSetProgressAndTime()
        mControlView.setProgressAndTime(0, 0, 0, 0)
    }


    /**
     * 继承后重写可替换为你需要的布局
     * 如果不想使用任何控制view，则可以重写该方法提供布局
     */
    override fun getLayoutId(): Int {
        return R.layout.amtv_gsy_video_player_control
    }

    override fun setSeekBeforeStart(position: Long) {
        seekOnStart = position
    }

    override fun startPlay() {
        startButtonLogic()
    }

    override fun setDataSource(
        url: String,
        cacheWithPlay: Boolean,
        cachePath: File?,
        title: String?
    ): Boolean {
        mControlView.setTitle(title)
        return setUp(url, cacheWithPlay, cachePath, title)
    }

    override fun setDataSource(
        url: String,
        cacheWithPlay: Boolean,
        cachePath: File?,
        mapHeadData: Map<String, String>?,
        title: String?
    ): Boolean {
        mControlView.setTitle(title)
        return setUp(url, cacheWithPlay, cachePath, mapHeadData, title)
    }

    override fun setCallBack(callback: VideoPlayerCallback) {
        setVideoAllCallBack(callback)
    }

    override fun getPlayerControlView(): TVVideoPlayerControlView {
        return this
    }

    override fun changeUIToPrepareLoading() {
        changeUiToPreparingShow()
    }
//    private fun showControlLayout() {
//
//        if (mControlView.isControlLayoutVisible()) {
//            val current = gsyVideoManager.currentPosition.toInt()
//            val max = gsyVideoManager.duration.coerceAtLeast(1).toInt()
//            mControlView.showControlFloat(
//                (current * 100f / max).toInt(),
//                current, max
//            )
//        }
//    }

    private val delayHideFloatLayoutRunnable = Runnable {
        mControlView.hideControlFloat()
    }

    private fun showControlFloatLayout() {
        removeCallbacks(delayHideFloatLayoutRunnable)
        postDelayed(delayHideFloatLayoutRunnable, 2000)
        mControlView.showControlFloat(
            (mSeekPositionChanged * 100f / mSeekMaxDuration).toInt(),
            mSeekPositionChanged.toInt(), mSeekMaxDuration.toInt()
        )
        PlayerLogger.d("showControlFloatLayout(progress=${(mSeekPositionChanged * 100f / mSeekMaxDuration).toInt()},duration=${mSeekPositionChanged},total=${mSeekMaxDuration}")
    }

    private fun handleLeftDownEvent(event: KeyEvent): Boolean {
        return handleSeekEventDown(event, false)
    }

    private fun handleRightDownEvent(event: KeyEvent): Boolean {
        return handleSeekEventDown(event, true)
    }

    /**
     * @param isIncrement 增加？
     */
    private fun handleSeekEventDown(event: KeyEvent, isIncrement: Boolean): Boolean {
        startDismissControlViewTimer()
        if (event.repeatCount == 0 && !mControlView.isControlLayoutVisible()) {
            //第一次按键时，如果没有显示控制器，则先显示控制器
            resetContinueSeekParams()
            mControlView.showControlLayout()
            return true
        }
        //持续处理seek中
        mSeekContinueFlag = true
        //记录变化的开始位置
        if (mSeekPositionChanged <= 0) {
            mSeekMaxDuration = gsyVideoManager.duration
            mSeekPositionChanged = gsyVideoManager.currentPosition
        }
        val now = System.currentTimeMillis()
        if (now - mLastRenderSeekTime < MIN_SEEK_RENDER_TIME) {
            return true
        }

        mLastRenderSeekTime = now


        //每指定时间增加一个缩放倍数
        //eventTime和downtime可能相同：第一次按下时，控制器已经显示，则会出现这个情况
        mSeekScale = ceil((event.eventTime - event.downTime).coerceAtLeast(1) / SEEK_SCALE_INCREMENT_INTERVAL).toInt()
            .coerceAtMost(MAX_SEEK_SCALE)

        val changeValue = mSeekScale * SEEK_STEP
        mSeekPositionChanged = if (isIncrement) {
            (mSeekPositionChanged + changeValue).coerceAtMost(mSeekMaxDuration)
        } else {
            (mSeekPositionChanged - changeValue).coerceAtLeast(0)
        }
        showControlFloatLayout()
        return true
    }

    /**
     * 重制连续seek参数
     */
    private fun resetContinueSeekParams() {
        mSeekContinueFlag = false
        mSeekScale = 1
        mLastRenderSeekTime = 0
        mSeekPositionChanged = -1
    }

    private fun handleSeekEventUp(): Boolean {
        if (mSeekContinueFlag) {
            PlayerLogger.d("handleSeekEventUp(mSeekPositionChanged=${mSeekPositionChanged}")
            gsyVideoManager.seekTo(mSeekPositionChanged)
            showControlFloatLayout()
            resetContinueSeekParams()
        }
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                return handleSeekEventUp()
            }
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                //切换播放状态
                clickStartIcon()
                return true
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("DEBUG_KEY", "onKeyLongPress(${keyCode},${event?.repeatCount})")
        return super.onKeyLongPress(keyCode, event)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                return handleLeftDownEvent(event)
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                return handleRightDownEvent(event)
            }
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN -> {
            }

//            KeyEvent.KEYCODE_VOLUME_UP -> {
//                //+ 音量
//                handleVolumeUp()
//            }
//            KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                //-音量
//                handleVolumeDown()
//            }
//            KeyEvent.KEYCODE_VOLUME_MUTE -> {
//                //静音
//            }
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun ensureVolumeDialog() {
        if (!::mCustomVolumeDialog.isInitialized) {
            mCustomVolumeDialog = VolumeDialog(context, this)
        }
    }

    private fun handleVolumeUp() {
        ensureVolumeDialog()
        mCustomVolumeDialog.handleVolumeByUpKeyEvent(mAudioManager)
    }

    private fun handleVolumeDown() {
        ensureVolumeDialog()
        mCustomVolumeDialog.handleVolumeByDownKeyEvent(mAudioManager)
    }

    override fun showVolumeDialog(deltaY: Float, volumePercent: Int) {
        ensureVolumeDialog()
        mCustomVolumeDialog.showWithDelayDismiss()
    }

    override fun dismissVolumeDialog() {
        if (::mCustomVolumeDialog.isInitialized)
            mCustomVolumeDialog.dismiss()
    }


    //
//    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
//        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
//        //不给触摸快进，如果需要，屏蔽下方代码即可
//        mChangePosition = false
//
//        //不给触摸音量，如果需要，屏蔽下方代码即可
//        mChangeVolume = false
//
//        //不给触摸亮度，如果需要，屏蔽下方代码即可
//        mBrightness = false
//    }
//
//    override fun touchDoubleUp(e: MotionEvent?) {
//        //super.touchDoubleUp();
//        //不需要双击暂停
//    }

    companion object {

        /**
         * 拖动步长设置为10
         */
        private const val SEEK_STEP = 10000

        /**
         * 最多8倍,也就是拖动最快的时候时[SEEK_STEP]*[MAX_SEEK_SCALE]
         */
        private const val MAX_SEEK_SCALE = 8

        /**
         * 倍数增加时间，每隔多少时间，增加一倍步长：即连续拖动时间超过[SEEK_SCALE_INCREMENT_INTERVAL]
         * 将增加Seek倍数，直到达到[MAX_SEEK_SCALE]设定的最大倍数
         */
        private const val SEEK_SCALE_INCREMENT_INTERVAL = 3000f

        /**
         * 最小渲染render的时间：在[MIN_SEEK_RENDER_TIME]设置的时间内，只响应一次拖动
         */
        private const val MIN_SEEK_RENDER_TIME = 300

    }

}