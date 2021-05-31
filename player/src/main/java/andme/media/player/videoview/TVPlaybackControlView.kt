package andme.media.player.videoview

import andme.core.widget.progressbar.FloatStatableSeekBar
import andme.core.widget.setGone
import andme.core.widget.setVisible
import andme.lang.ONE_HOUR_TIME
import andme.lang.ONE_MINUTE_TIME
import andme.lang.orDefault
import andme.media.player.R
import andme.media.player.core.AMPlayer2
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.IntRange
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.ceil


/**
 * Created by Lucio on 2021/4/9.
 */
class TVPlaybackControlView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), AMPlayer2.Control {

    /*当前时间*/
    internal val currentPositionText: TextView

    /*总时间*/
    internal val durationText: TextView

    /*进度条*/
    private val floatSeekBar: FloatStatableSeekBar

    private val titleText: TextView

    private val playView: View
    private val pauseView: View

    private var player: AMPlayer2? = null

    /**
     * 单位ms
     */
    private var duration: Int = 0
    private var currentPosition: Int = 0
    private var timeFormat = DURATION_FORMAT_HMS

    private val visibilityListeners: CopyOnWriteArrayList<AMPlayer2.Control.VisibilityListener> =
        CopyOnWriteArrayList()

    private val updateProgressAction: Runnable
    private val hideAction: Runnable
    private var hideAtMs: Long = TIME_UNSET


    private var _isAttachToWindow: Boolean = false

    /*seek的步长倍数，最多8倍*/
    private var mSeekScale = 1

    /*持续seek的标记位*/
    private var mSeekContinueFlag: Boolean = false

    /*当前seek变化的位置*/
    private var mSeekPositionChanged: Int = -1

    /*seek变动的最大位置*/
    private var mSeekMaxDuration: Int = 0

    /*上一次渲染seek变化的时间*/
    private var mLastRenderSeekTime: Long = 0

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        inflate(context, R.layout.am_tv_video_play_control_view, this)
        currentPositionText = findViewById(R.id.current_duration_tv)
        durationText = findViewById(R.id.total_duration_tv)
        floatSeekBar = findViewById(R.id.float_seek_bar)
        playView = findViewById(R.id.play)
        pauseView = findViewById(R.id.pause)
        titleText = findViewById(R.id.title_tv)
        updateProgressAction = Runnable {
            updateProgress()
        }
        hideAction = Runnable {
            hide()
        }
        if (background == null) {
            setBackgroundResource(R.color.black_opacity_30)
        }

    }

    private val componentListener =
        object : AMPlayer2.OnAMPlayerPreparedListener, AMPlayer2.OnAMPlayerCompleteListener,
            AMPlayer2.OnAMPlayerErrorListener {
            override fun onAMPlayerError(player: AMPlayer2, e: Throwable): Boolean {
                show()
                return true
            }

            override fun onAMPlayerComplete(player: AMPlayer2) {
                show()
            }

            override fun onAMPlayerPrepared(player: AMPlayer2) {
                /**
                 * 要延迟显示；否则show方法中判断当前播放器没有在播放中，不会更新进度条
                 */
                postDelayed(Runnable {
                    show()
                }, 1000)

            }

        }

    private val isVisible: Boolean get() = visibility == View.VISIBLE

    /**
     * Shows the playback controls. If [.getShowTimeoutMs] is positive then the controls will
     * be automatically hidden after this duration of time has elapsed without user input.
     */
    fun show() {
        show(DEFAULT_SHOW_TIMEOUT_MS)
        delayHideOrNever()
    }

    override fun show(timeout: Long) {
        if (!isVisible) {
            visibility = View.VISIBLE
            requestFocus()
            for (visibilityListener in visibilityListeners) {
                visibilityListener.onVisibilityChange(visibility)
            }
        }
        updateAll()
        delayHide(timeout)
    }


    /**
     * 隐藏控制器
     */
    override fun hide() {
        if (!isVisible) {
            return
        }
        visibility = View.GONE
        for (visibilityListener in visibilityListeners) {
            visibilityListener.onVisibilityChange(visibility)
        }
        removeCallbacks(updateProgressAction)
        removeCallbacks(hideAction)
        hideAtMs = TIME_UNSET
    }

    override fun setPlayer(player: AMPlayer2?) {
        if (this.player == player) {
            return
        }

        this.player?.let {
            it.removeErrorListener(componentListener)
            it.removeCompletionListener(componentListener)
            it.removePreparedListener(componentListener)
        }

        this.player = player
        this.player?.let {
            it.addErrorListener(componentListener)
            it.addCompletionListener(componentListener)
            it.addPreparedListener(componentListener)
        }
        updateAll()
    }


    private fun updateAll(useAnim: Boolean = true) {
        updateProgress()
        updatePlayPauseButton()
        if (useAnim) {
            updateProgressIndicatorWithAnim()
        } else {
            updateProgressIndicator()
        }

    }

    /**
     * 如果当前正在播放中，则延迟显示，否则永久显示
     * */
    fun delayHideOrNever() {
        if (player?.isPlaying().orDefault()) {
            delayHide(DEFAULT_SHOW_TIMEOUT_MS)
        } else {
            delayHide(TIME_UNSET)
        }
    }

    private fun delayHide(timeout: Long) {
        removeCallbacks(hideAction)
        if (timeout > 0) {
            hideAtMs = SystemClock.uptimeMillis() + timeout
            if (_isAttachToWindow) {
                postDelayed(hideAction, timeout)
            }
        } else {
            hideAtMs = TIME_UNSET
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        _isAttachToWindow = true
        if (hideAtMs != TIME_UNSET) {
            val delayMs: Long = hideAtMs - SystemClock.uptimeMillis()
            if (delayMs <= 0) {
                hide()
            } else {
                postDelayed(hideAction, delayMs)
            }
        } else if (isVisible) {
            delayHideOrNever()
        }
        updateAll()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _isAttachToWindow = false
        removeCallbacks(updateProgressAction)
        removeCallbacks(hideAction)
    }

    private fun updateProgress() {
        if (!isVisible || !_isAttachToWindow || mSeekContinueFlag) {
            return
        }
        val player: AMPlayer2 = this.player ?: return
        var position: Int = 0
        var bufferedPosition: Int = 0
        position = player.getCurrentPosition()
        bufferedPosition = player.getBufferingPosition()
        val duration = player.getDuration().coerceAtLeast(1)
        setProgressAndTime(
            (position * 1.0 / duration * 100).toInt(),
            (bufferedPosition * 1.0 / duration * 100).toInt(), position, duration
        )

        removeCallbacks(updateProgressAction)
        if (player.isPlaying()) {
            postDelayed(
                updateProgressAction,
                MAX_UPDATE_INTERVAL_MS - (position % MAX_UPDATE_INTERVAL_MS)
            )
        }
    }

    /**
     * 设置进度和时间
     * @param progress  进度
     * @param secProgress 缓冲进度
     * @param currentTime 当前时间 单位ms
     * @param totalTime 总时间 单位ms
     */
    fun setProgressAndTime(
        @IntRange(from = 0, to = 100)
        progress: Int,
        currentTime: Int,
        totalTime: Int
    ) {
        setProgressAndTime(progress, floatSeekBar.getSecondaryProgress(), currentTime, totalTime)
    }

    /**
     * 设置进度和时间
     * @param progress  进度
     * @param secProgress 缓冲进度
     * @param currentTime 当前时间 单位ms
     * @param totalTime 总时间 单位ms
     */
    fun setProgressAndTime(
        @IntRange(from = 0, to = 100)
        progress: Int,
        @IntRange(from = 0, to = 100)
        secProgress: Int,
        currentTime: Int,
        totalTime: Int
    ) {
        if (duration != totalTime) {
            duration = totalTime
            timeFormat = prepareDurationFormat(totalTime)
            renderDuration(durationText, totalTime)
        }

        currentPosition = currentTime
        renderDuration(currentPositionText, currentTime)
        floatSeekBar.setProgress(progress, secProgress, currentPositionText.text.toString())
    }

    private fun prepareDurationFormat(duration: Int): String {
        if (duration < ONE_HOUR_TIME) {
            //在一个小时以内，使用 mm:ss
            return DURATION_FORMAT_MS
        } else {
            return DURATION_FORMAT_HMS
        }
    }

    /**
     * 根据Total的格式，格式化时间
     */
    private fun getDurationFormatText(duration: Int): String {
        val hour = duration / ONE_HOUR_TIME
        val minute = duration % ONE_HOUR_TIME / ONE_MINUTE_TIME
        val seconds = duration % ONE_HOUR_TIME % ONE_MINUTE_TIME / 1000
        return if (timeFormat == DURATION_FORMAT_MS) {
            String.format(timeFormat, minute, seconds)
        } else {
            String.format(timeFormat, hour, minute, seconds)
        }
    }

    private fun renderDuration(tv: TextView, duration: Int) {
        tv.text = getDurationFormatText(duration)
    }

    /**
     * 显示时间浮层
     */
    fun showControlFloat(
        @IntRange(from = 0, to = 100)
        progress: Int,
        currentTime: Int,
        totalTime: Int
    ) {
        floatSeekBar.showFloat()
        setProgressAndTime(progress, currentTime, totalTime)
    }

    /**
     * 淡入Float;如果正在进行淡入动画，或者已经在绘制状态，则不处理
     */
    fun showFloat() {
        floatSeekBar.showFloat()
    }

    fun hideControlFloat() {
        floatSeekBar.hideFloat()
    }

    /**
     * 淡出Float
     */
    fun hideFloat() {
        floatSeekBar.hideFloat()
    }

    /**
     * 动画显示一级指示器（圆点指示器）
     */
    fun showFirstIndicatorWithAnim() {
        floatSeekBar.showFirstIndicatorWithAnim()
    }

    /**
     * 动画显示二级指示器（图标指示器）
     */
    fun showSecondaryIndicatorWithAnim() {
        floatSeekBar.showSecondaryIndicatorWithAnim()
    }

    /**
     * 显示一级指示器（圆点指示器）
     */
    fun showFirstIndicator() {
        floatSeekBar.showFirstIndicator()
    }

    /**
     * 显示二级指示器（图标指示器）
     */
    fun showSecondaryIndicator() {
        floatSeekBar.showSecondaryIndicator()
    }


    fun setTitle(title: CharSequence?) {
        titleText.text = title
    }

    /**
     * 分发按键事件
     */
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event == null) return super.dispatchKeyEvent(event)
        var handled = false
        if (event.action == KeyEvent.ACTION_DOWN) {
            handled = handleKeyDownEvent(event)
        } else if (event.action == KeyEvent.ACTION_UP) {
            handled = handleKeyUpEvent(event)
        }
        if (handled)
            return true
        return super.dispatchKeyEvent(event)
    }

    private fun handleKeyUpEvent(event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                return handleSeekEventUp()
            }
            else -> {
                return false
            }
        }
    }

    /**
     * 处理按键按下事件
     */
    private fun handleKeyDownEvent(event: KeyEvent): Boolean {
        val keyCode = event.keyCode
        //是否是第一次按下按键
        val uniqueDown = (event.repeatCount == 0 && event.action == KeyEvent.ACTION_DOWN)

        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                return handleLeftDownEvent(event)
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                return handleRightDownEvent(event)
            }
            KeyEvent.KEYCODE_HEADSETHOOK,
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
            KeyEvent.KEYCODE_SPACE,
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                //播放&暂停之间进行切换
                if (uniqueDown) {
                    doPauseResume()
                    updateAll(true)
                    delayHideOrNever()
                }
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PLAY -> {
                if (uniqueDown && !player?.isPlaying().orDefault()) {
                    player?.start()
                    updateAll(true)
                    delayHideOrNever()
                }
                return true
            }
            KeyEvent.KEYCODE_MEDIA_STOP, KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                if (uniqueDown && player?.isPlaying().orDefault()) {
                    player?.pause()
                    updateAll(true)
                    delayHideOrNever()
                }
                return true
            }
            KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_MENU -> {
                if (uniqueDown) {
                    hide()
                }
                return true
            }
        }
        return false
    }

    /**
     * 暂停或播放切换
     */
    private fun doPauseResume() {
        if (player?.isPlaying().orDefault()) {
            player?.pause()
        } else {
            player?.start()
        }
    }

    /**
     * 更新播放&暂停按钮状态
     */
    private fun updatePlayPauseButton() {
        if (!isVisible || !_isAttachToWindow)
            return
        if (player?.isPlaying().orDefault()) {
            pauseView.setVisible()
            playView.setGone()
        } else {
            playView.setVisible()
            pauseView.setGone()
        }
    }

    private fun updateProgressIndicator() {
        if (player?.isPlaying().orDefault()) {
            showFirstIndicator()
        } else {
            showSecondaryIndicator()
        }
    }

    private fun updateProgressIndicatorWithAnim() {
        if (player?.isPlaying().orDefault()) {
            showFirstIndicatorWithAnim()
        } else {
            showSecondaryIndicatorWithAnim()
        }
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
        val player = player ?: return false

        delayHideOrNever()
        mSeekContinueFlag = true
        //记录变化的开始位置
        if (mSeekPositionChanged < 0) {
            mSeekMaxDuration = player.getDuration()
            mSeekPositionChanged = player.getCurrentPosition()
        }
        val now = System.currentTimeMillis()
        if (now - mLastRenderSeekTime < MIN_SEEK_RENDER_TIME) {
            return true
        }

        mLastRenderSeekTime = now


        //每指定时间增加一个缩放倍数
        //eventTime和downtime可能相同：第一次按下时，控制器已经显示，则会出现这个情况
        mSeekScale =
            ceil((event.eventTime - event.downTime).coerceAtLeast(1) / SEEK_SCALE_INCREMENT_INTERVAL).toInt()
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

    /*延迟隐藏控制器Float的Runnable*/
    private val delayHideFloatLayoutRunnable = Runnable {
        hideFloat()
    }

    private fun showControlFloatLayout() {
        removeCallbacks(delayHideFloatLayoutRunnable)
        postDelayed(delayHideFloatLayoutRunnable, CONTROL_FLOAT_SHOW_TIME)
        showControlFloat(
            (mSeekPositionChanged * 100f / mSeekMaxDuration).toInt(),
            mSeekPositionChanged.toInt(), mSeekMaxDuration.toInt()
        )
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
            player?.seekTo(mSeekPositionChanged)
            showControlFloatLayout()
            resetContinueSeekParams()
            return true
        } else {
            return false
        }
    }

    /**
     * Adds a [VisibilityListener].
     *
     * @param listener The listener to be notified about visibility changes.
     */
    override fun addVisibilityListener(listener: AMPlayer2.Control.VisibilityListener) {
        visibilityListeners.add(listener)
    }

    /**
     * Removes a [VisibilityListener].
     *
     * @param listener The listener to be removed.
     */
    override fun removeVisibilityListener(listener: AMPlayer2.Control.VisibilityListener) {
        visibilityListeners.remove(listener)
    }

    companion object {

        private const val DURATION_FORMAT_MS = "%02d:%02d"

        private const val DURATION_FORMAT_HMS = "%02d:%02d:%02d"

        private const val ANIM_DURATION = 800L

        private const val TIME_UNSET = Long.MIN_VALUE + 1

        const val DEFAULT_SHOW_TIMEOUT_MS = 5000L

        /** The maximum interval between time bar position updates.  */
        private const val MAX_UPDATE_INTERVAL_MS = 1000L

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
        private const val SEEK_SCALE_INCREMENT_INTERVAL = 10000f

        /**
         * 最小渲染render的时间：在[MIN_SEEK_RENDER_TIME]设置的时间内，只响应一次拖动
         */
        private const val MIN_SEEK_RENDER_TIME = 32

        /*float显示的时间，即超过该时间后将自动隐藏*/
        private const val CONTROL_FLOAT_SHOW_TIME = 2500L

        /*Control显示的时间，即超过该时间后将自动隐藏*/
        private const val CONTROL_SHOW_TIME = 6000L
    }
}