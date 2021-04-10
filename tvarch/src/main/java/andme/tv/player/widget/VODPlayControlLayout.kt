package andme.tv.player.widget

import andme.core.widget.progressbar.FloatSeekBar
import andme.lang.ONE_HOUR_TIME
import andme.lang.ONE_MINUTE_TIME
import andme.tv.arch.R
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Created by Lucio on 2021/4/9.
 * 电视端播放控制布局
 */
class VODPlayControlLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mDurationFormat = "%2d:%2d:%2d"

    internal val mCurrentDurationTv: TextView
    internal val mTotalDurationTv: TextView
    private val mFloatSB: FloatSeekBar

    /**
     * 单位ms
     */
    private var mTotalDuration: Int = 0
    private var mCurrentDuration: Int = 0

    init {
        inflate(context, R.layout.amtv_media_video_control_layout, this)
        mCurrentDurationTv = findViewById(R.id.current_duration_tv)
        mTotalDurationTv = findViewById(R.id.total_duration_tv)
        mFloatSB = findViewById(R.id.float_seek_bar)
    }

    /**
     * @param progress
     * @param currentTime 当前时间 单位ms
     * @param totalTime 总时间 单位ms
     */
    fun setProgressAndTime(
        @IntRange(from = 0,to = 100)
        progress: Int,
        @IntRange(from = 0,to = 100)
        secProgress: Int,
        currentTime: Int,
        totalTime: Int
    ) {
        mCurrentDuration = currentTime
        renderCurrentDuration()
        if(mTotalDuration != totalTime){
            mTotalDuration = totalTime
            renderTotalDuration()
        }
        mFloatSB.setProgress(progress, mCurrentDurationTv.text.toString())
    }

    /**
     * 淡出Float
     */
    fun fadeOutFloat() {
        mFloatSB.fadeOutFloat()
    }

    /**
     * 淡入Float
     */
    fun fadeInFloat() {
        mFloatSB.fadeInFloat()
    }
    private fun renderCurrentDuration() {
        mCurrentDurationTv.text = getDurationFormatText(mCurrentDuration)
    }

    private fun renderTotalDuration() {
        mCurrentDurationTv.text = getDurationFormatText(mTotalDuration)
    }

    private fun getDurationFormatText(duration: Int): String {
        val hour = duration / ONE_HOUR_TIME
        val minute = duration % ONE_HOUR_TIME / ONE_MINUTE_TIME
        val seconds = duration % ONE_HOUR_TIME % ONE_MINUTE_TIME / 1000
        if (mDurationFormat == DURATION_FORMAT_MS) {
            return String.format(mDurationFormat, minute, seconds)
        } else {
            return String.format(mDurationFormat, hour, minute, seconds)
        }
    }

    private fun prepareDurationFormat(duration: Long): String {
        if (duration < ONE_HOUR_TIME) {
            //在一个小时以内，使用 mm:ss
            return DURATION_FORMAT_MS
        } else {
            return DURATION_FORMAT_HMS
        }
    }

    companion object {

        private const val DURATION_FORMAT_MS = "%2d:%2d"

        private const val DURATION_FORMAT_HMS = "%2d:%2d:%2d"
    }
}