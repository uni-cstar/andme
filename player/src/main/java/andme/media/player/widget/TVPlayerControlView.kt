package andme.media.player.widget

import andme.core.widget.isVisible
import andme.core.widget.progressbar.FloatStatableSeekBar
import andme.core.widget.setGone
import andme.core.widget.setVisible
import andme.lang.ONE_HOUR_TIME
import andme.lang.ONE_MINUTE_TIME
import andme.lang.orDefault
import andme.media.player.R
import andme.media.player.core.AMPlayer
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.IntRange
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.ceil


/**
 * Created by Lucio on 2021/4/9.
 */
class TVPlayerControlView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    /*当前时间*/
    internal val currentPositionText: TextView

    /*总时间*/
    internal val durationText: TextView

    /*进度条*/
    private val floatSeekBar: FloatStatableSeekBar

    private val titleText:TextView

    private val playOrPauseView:ENPlayView

    private var player: AMPlayer? = null

    /**
     * 单位ms
     */
    private var duration: Int = 0
    private var currentPosition: Int = 0
    private var timeFormat = DURATION_FORMAT_HMS

    private val visibilityListeners: CopyOnWriteArrayList<VisibilityListener> =
        CopyOnWriteArrayList()

    private val updateProgressAction: Runnable
    private val hideAction: Runnable
    private var hideAtMs: Long = TIME_UNSET

    /*显示超时时间，即显示多少时间后自动隐藏*/
    private var showTimeoutMs = 0

    private var _isAttachToWindow: Boolean = false


    /*seek的步长倍数，最多8倍*/
    private var mSeekScale = 1

    /*持续seek的标记位*/
    private var mSeekContinueFlag: Boolean = false

    /*当前seek变化的位置*/
    private var mSeekPositionChanged: Long = -1

    /*seek变动的最大文职*/
    private var mSeekMaxDuration: Long = 0

    /*上一次渲染seek变化的时间*/
    private var mLastRenderSeekTime: Long = 0

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        inflate(context, R.layout.am_tv_player_control_view, this)
        currentPositionText = findViewById(R.id.current_duration_tv)
        durationText = findViewById(R.id.total_duration_tv)
        floatSeekBar = findViewById(R.id.float_seek_bar)
        playOrPauseView = findViewById(R.id.playOrPause)
        titleText = findViewById(R.id.title_tv)
        updateProgressAction = Runnable {
            updateProgress()
        }
        R.color.exo_black_opacity_70
        hideAction = Runnable {
            hide()
        }

        showTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS
        if (attrs != null) {
            val a = context
                .theme
                .obtainStyledAttributes(attrs, R.styleable.TVPlayerControlView, 0, 0)
            showTimeoutMs = a.getInt(R.styleable.TVPlayerControlView_show_timeout, showTimeoutMs)
            a.recycle()
        }

        if(background == null){
            setBackgroundResource(R.drawable.am_player_control_bg)
        }

    }

    private val isVisible: Boolean get() = visibility == View.VISIBLE

    fun getShowTimeoutMs(): Int {
        return showTimeoutMs
    }

    /**
     * Sets the playback controls timeout. The playback controls are automatically hidden after this
     * duration of time has elapsed without user input.
     *
     * @param showTimeoutMs 控制器显示持续时间，如果设置的是个负数，会导致控制器显示之后一直显示
     */
    fun setShowTimeoutMs(showTimeoutMs: Int) {
        this.showTimeoutMs = showTimeoutMs
        if (isVisible) {
            // Reset the timeout.
            delayHide()
        }
    }

    /**
     * Adds a [VisibilityListener].
     *
     * @param listener The listener to be notified about visibility changes.
     */
    fun addVisibilityListener(listener: VisibilityListener) {
        visibilityListeners.add(listener)
    }

    /**
     * Removes a [VisibilityListener].
     *
     * @param listener The listener to be removed.
     */
    fun removeVisibilityListener(listener: VisibilityListener) {
        visibilityListeners.remove(listener)
    }

    fun hide() {
        if (isVisible) {
            visibility = View.GONE
            for (visibilityListener in visibilityListeners) {
                visibilityListener.onVisibilityChange(visibility)
            }
            removeCallbacks(updateProgressAction)
            removeCallbacks(hideAction)
            hideAtMs = TIME_UNSET
        }
    }

    /**
     * Shows the playback controls. If [.getShowTimeoutMs] is positive then the controls will
     * be automatically hidden after this duration of time has elapsed without user input.
     */
    fun show() {
        if (!isVisible) {
            visibility = View.VISIBLE
            for (visibilityListener in visibilityListeners) {
                visibilityListener.onVisibilityChange(visibility)
            }
            updateAll()
            requestPlayPauseFocus()
        }
        // Call hideAfterTimeout even if already visible to reset the timeout.
        delayHide()
    }

    fun setPlayer(player: AMPlayer?) {
        if (this.player == player) {
            return
        }
        this.player?.removePlayerStateChangedListener(playerListener)
        this.player = player
        player?.addPlayerStateChangedListener(playerListener)
//        player?.addListener(componentListener)
        updateAll()
    }

    private val playerListener = AMPlayer.OnAMPlayerStateChangedListener {

        updatePlayPauseButton()
    }

    private fun requestPlayPauseFocus() {
//        val shouldShowPauseButton: Boolean = shouldShowPauseButton()
//        if (!shouldShowPauseButton && playButton != null) {
//            playButton.requestFocus()
//        } else if (shouldShowPauseButton && pauseButton != null) {
//            pauseButton.requestFocus()
//        }
    }

    private fun updateAll() {
        updatePlayPauseButton()
//        updateNavigation()
//        updateRepeatModeButton()
//        updateShuffleButton()
        updateTimeline()
    }


    fun updatePlayPauseButton() {
        if(!isVisible || !_isAttachToWindow)
            return
        if(player?.shouldShowPauseButton().orDefault()){
            playOrPauseView.play()
        }else{
            playOrPauseView.pause()
        }
    }

    private fun updateTimeline() {
        updateProgress()
    }

    private fun delayHide() {
        removeCallbacks(hideAction)
        if (showTimeoutMs > 0) {
            hideAtMs = SystemClock.uptimeMillis() + showTimeoutMs
            if (_isAttachToWindow) {
                postDelayed(hideAction, showTimeoutMs.toLong())
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
            delayHide()
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
        val player: AMPlayer = this.player ?: return


        var position: Long = 0
        var bufferedPosition: Long = 0
        position = player.getCurrentPosition()
        bufferedPosition = player.getBufferingPosition()
        val duration = player.getDuration().coerceAtLeast(1)
        setProgressAndTime(
            (position * 1.0 / duration * 100).toInt(),
            (bufferedPosition * 1.0 / duration * 100).toInt(), position.toInt(), duration.toInt()
        )

        removeCallbacks(updateProgressAction)

        val playbackState = player.getCurrentState()
        if (playbackState != Player.STATE_ENDED && playbackState != Player.STATE_IDLE) {
            postDelayed(updateProgressAction, MAX_UPDATE_INTERVAL_MS)
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

    /** Listener to be notified about changes of the visibility of the UI control.  */
    fun interface VisibilityListener {
        /**
         * Called when the visibility changes.
         *
         * @param visibility The new visibility. Either [View.VISIBLE] or [View.GONE].
         */
        fun onVisibilityChange(visibility: Int)
    }

    fun setTitle(title:CharSequence?){
        titleText.text = title
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                return handleLeftDownEvent(event)
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                return handleRightDownEvent(event)
            }
//            KeyEvent.KEYCODE_DPAD_UP,
//            KeyEvent.KEYCODE_DPAD_DOWN -> {
//            }
        }
        return super.onKeyDown(keyCode, event)
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
        delayHide()
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


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                return handleSeekEventUp()
            }
        }
//        when (keyCode) {
//            KeyEvent.KEYCODE_DPAD_CENTER -> {
//                if(player.isPlaying()){
//                    player.stop()
//                }else{
//                    player.start()
//                }
//                return true
//            }
//        }


        return super.onKeyUp(keyCode, event)
    }

    private fun handleSeekEventUp(): Boolean {
        if (mSeekContinueFlag) {
            player?.seekTo(mSeekPositionChanged)
            showControlFloatLayout()
            resetContinueSeekParams()
            return true
        }else{
            return false
        }

    }

    companion object {

        private const val DURATION_FORMAT_MS = "%02d:%02d"

        private const val DURATION_FORMAT_HMS = "%02d:%02d:%02d"

        private const val ANIM_DURATION = 800L

        private const val TIME_UNSET = Long.MIN_VALUE + 1

        const val DEFAULT_SHOW_TIMEOUT_MS = 5000

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
        private const val SEEK_SCALE_INCREMENT_INTERVAL = 3000f

        /**
         * 最小渲染render的时间：在[MIN_SEEK_RENDER_TIME]设置的时间内，只响应一次拖动
         */
        private const val MIN_SEEK_RENDER_TIME = 300

        /*float显示的时间，即超过该时间后将自动隐藏*/
        private const val CONTROL_FLOAT_SHOW_TIME = 2500L

        /*Control显示的时间，即超过该时间后将自动隐藏*/
        private const val CONTROL_SHOW_TIME = 6000L
    }
}