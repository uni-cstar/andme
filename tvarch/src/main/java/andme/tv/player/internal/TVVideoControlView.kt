package andme.tv.player.internal

import andme.core.binding.bindTextOrGone
import andme.core.widget.isVisible
import andme.core.widget.progressbar.FloatSeekBar
import andme.core.widget.progressbar.FloatStatableSeekBar
import andme.core.widget.setGone
import andme.core.widget.setVisible
import andme.lang.ONE_HOUR_TIME
import andme.lang.ONE_MINUTE_TIME
import andme.lang.toHMS
import andme.tv.arch.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.annotation.IntRange
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import moe.codeest.enviews.ENPlayView


/**
 * Created by Lucio on 2021/4/9.
 */
class TVVideoControlView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    /*播放或暂停view*/
    private val mPlayOrPauseView: ENPlayView

    /*底部播放控制view：当前播放进度、进度条、总进度*/
    private val mControlGroup: Group

    /*当前时间*/
    internal val mCurrentDurationTv: TextView

    /*总时间*/
    internal val mTotalDurationTv: TextView

    /*进度条*/
    private val mFloatSB: FloatStatableSeekBar

    private val mBufferingBgV: View
    private val mBufferingView: ProgressBar
    private val mTitleText: TextView
    private val mHintText: TextView
    private val mThumbContainer: RelativeLayout

//    private val mPlayBtnOnBar: ImageView
//    private val mPlayBtnOnBarShowAnim: Animation
//    private val mPlayBtnOnBarHideAnim: Animation
//    private var mPlayBtnOnBarAniming: Boolean = false
//private var mPlayBtnOnBarContainer: FrameLayout

    /**
     * 单位ms
     */
    private var mTotalDuration: Int = 0
    private var mCurrentDuration: Int = 0
    private var mDurationFormat = DURATION_FORMAT_HMS

    /*标题*/
    private var mTitle: String? = null

    /*播放开始前的seek*/
    private var mSeekOnStart: Int = 0

    /**
     * 是否已经播放过
     */
    private var mHasPlayed: Boolean = false



    init {
        inflate(context, R.layout.amtv_media_video_control_layout, this)
//        mPlayBtnOnBarContainer = findViewById(R.id.fff)
//        mPlayBtnOnBar = findViewById(R.id.play_btn_on_progress)
        mPlayOrPauseView = findViewById(R.id.playOrPause)
        mPlayOrPauseView.setDuration(ANIM_DURATION.toInt())
        mControlGroup = findViewById(R.id.control_group)

        mCurrentDurationTv = findViewById(R.id.current_duration_tv)
        mTotalDurationTv = findViewById(R.id.total_duration_tv)
        mFloatSB = findViewById(R.id.float_seek_bar)


        mBufferingBgV = findViewById(R.id.buffering_bg)
        mBufferingView = findViewById(R.id.buffering_view)

        mTitleText = findViewById(R.id.title_tv)
        mHintText = findViewById(R.id.hint_tv)

        mThumbContainer = findViewById(R.id.thumb)

//        mPlayBtnOnBarShowAnim = ScaleAnimation(
//            0.4f,
//            1.0f,
//            0.4f,
//            1.0f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f
//        ).also {
//            it.duration = ANIM_DURATION
//            it.setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationStart(animation: Animation?) {
//                    mPlayBtnOnBar.setVisible()
//
//                }
//
//                override fun onAnimationEnd(animation: Animation?) {
//                    mPlayBtnOnBarAniming = false
//                }
//
//                override fun onAnimationRepeat(animation: Animation?) {
//                }
//            })
//        }
//
//        mPlayBtnOnBarHideAnim = ScaleAnimation(
//            1.0f,
//            0.4f,
//            1.0f,
//            0.4f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f
//        ).also {
//            it.duration = ANIM_DURATION
//            it.setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationStart(animation: Animation?) {
//                }
//
//                override fun onAnimationEnd(animation: Animation?) {
//                    mPlayBtnOnBarAniming = false
//                    mPlayBtnOnBar.setGone()
//                }
//
//                override fun onAnimationRepeat(animation: Animation?) {
//                }
//            })
//        }
    }


    fun setTitle(title: String?) {
        mTitleText.text = title
        mTitle = title
    }

    /**
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
        setProgressAndTime(progress,mFloatSB.getSecondaryProgress(),currentTime, totalTime)
    }

    /**
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
        if (mTotalDuration != totalTime) {
            mTotalDuration = totalTime
            mDurationFormat = prepareDurationFormat(totalTime)
            renderDuration(mTotalDurationTv, totalTime)
        }

        mCurrentDuration = currentTime
        renderDuration(mCurrentDurationTv, currentTime)


        mFloatSB.setProgress(progress, mCurrentDurationTv.text.toString())
//        mFloatSB.showSecondaryIndicator()

//        if (mPlayBtnOnBarAniming) {
//            //如果在进行放大动画，则不进行定位，避免动画跳跃
//            mFloatSB.setProgressOnly(progress,secProgress, mCurrentDurationTv.text.toString())
//        } else {
//            mFloatSB.setProgress(progress, mCurrentDurationTv.text.toString())
//            mFloatSB.showSecondaryIndicator()
////            updateBarPlayButtonLayout()
//        }

    }

//    private fun updateBarPlayButtonLayout() {
//        val left = (mFloatSB.left + mFloatSB.indicatorX - mPlayBtnOnBar.width / 2f).toInt()
//        mPlayBtnOnBarContainer.setPadding(left, 0, 0, 0)
////        val lp = mPlayBtnOnBar.layoutParams as LayoutParams
////        if (lp.leftMargin != left) {
////            lp.leftMargin = left
////            mPlayBtnOnBar.layoutParams = layoutParams
////        }
////        mPlayBtnOnBar.translationX = left
//    }

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
        return if (mDurationFormat == DURATION_FORMAT_MS) {
            String.format(mDurationFormat, minute, seconds)
        } else {
            String.format(mDurationFormat, hour, minute, seconds)
        }
    }

    private fun renderDuration(tv: TextView, duration: Int) {
        tv.text = getDurationFormatText(duration)
    }

    private fun hideAllBufferingView() {
        mBufferingBgV.setGone()
        mBufferingView.setGone()
        mTitleText.setGone()
        mHintText.setGone()
    }

    /**
     * 渲染为初始化情况
     */
    fun renderInit() {
        mControlGroup.setGone()
        mBufferingBgV.setGone()
        mBufferingView.setGone()
        mHintText.setGone()
        mThumbContainer.setVisible()
        mTitleText.setVisible()
        mPlayOrPauseView.setVisible()
    }

    /**
     * 准备中
     * @param title 标题
     * @param hint 缓冲中
     */
    fun renderPreparing() {
        mControlGroup.setGone()
        mPlayOrPauseView.setGone()
        mThumbContainer.setGone()
        mBufferingBgV.setVisible()
        mBufferingView.setVisible()
        mTitleText.setVisible()
        mHintText.setVisible()

        if (!mHasPlayed && mSeekOnStart > 0) {
            mHintText.text = "正在启动,上次观看至${mSeekOnStart.toLong().toHMS()}"
            mSeekOnStart = -1
        } else {
            mHintText.text = "正在启动，请稍后..."
        }
    }


    /**
     * 开始缓冲
     */
    fun renderStartBuffering() {
        mControlGroup.setVisible()
//        mPlayOrPauseView.setVisible()
        mThumbContainer.setGone()
        mBufferingBgV.setVisible()
        mBufferingView.setVisible()
        mTitleText.setVisible()
        mHintText.bindTextOrGone("正在缓冲，请稍后...")
    }

    /**
     * 开始播放
     */
    fun renderStartPlaying() {
        mHasPlayed = true
        hideAllBufferingView()
        mControlGroup.setVisible()
        mPlayOrPauseView.setVisible()
        mPlayOrPauseView.play()
        mThumbContainer.setGone()

        mFloatSB.showFirstIndicatorWithAnim()

//        mPlayBtnOnBar.clearAnimation()
//        mPlayBtnOnBar.startAnimation(mPlayBtnOnBarHideAnim)
//        mPlayBtnOnBarAniming = true
    }

    /**
     * 渲染暂停
     */
    fun renderPause() {
        hideAllBufferingView()
        mTitleText.setVisible()
        mControlGroup.setVisible()
        mThumbContainer.setGone()
        mPlayOrPauseView.setVisible()
        mPlayOrPauseView.pause()

        mFloatSB.showSecondaryIndicatorWithAnim()
//        mPlayBtnOnBar.visibility = View.VISIBLE
//        mPlayBtnOnBar.clearAnimation()
//        mPlayBtnOnBar.startAnimation(mPlayBtnOnBarShowAnim)
//        mPlayBtnOnBarAniming = true
    }

    /**
     * 渲染播放结束
     */
    fun renderComplete() {
        hideAllBufferingView()
        mTitleText.setVisible()
        mHintText.bindTextOrGone("播放完成")
        mThumbContainer.setVisible()
        mControlGroup.setVisible()
        mPlayOrPauseView.setVisible()
        mPlayOrPauseView.pause()

    }

    fun renderError() {
        hideAllBufferingView()
        mTitleText.setVisible()
        mHintText.bindTextOrGone("播放错误")
        mControlGroup.setGone()
        mPlayOrPauseView.setVisible()
        mPlayOrPauseView.pause()
    }


    fun hideAllWidget() {
        hideAllBufferingView()
        mThumbContainer.setGone()
        mControlGroup.setGone()
        mPlayOrPauseView.setGone()
    }

    fun showControlLayout(){
        mControlGroup.setVisible()
    }

    fun isControlLayoutVisible():Boolean{
        return mControlGroup.isVisible
    }

    fun hideControlLayout(){
        mControlGroup.setGone()
    }

    fun showControlFloat(@IntRange(from = 0, to = 100)
                            progress: Int,
                            currentTime: Int,
                            totalTime: Int){
        mFloatSB.showFloat()
        setProgressAndTime(progress, currentTime, totalTime)
    }

    fun hideControlFloat(){
        mFloatSB.hideFloat()
    }

    /**
     * 淡出Float
     */
    fun showFloat() {
        mFloatSB.showFloat()
    }

    /**
     * 淡入Float;如果正在进行淡入动画，或者已经在绘制状态，则不处理
     */
    fun hideFloat() {
        mFloatSB.hideFloat()
    }

    fun showFirstIndicatorWithAnim() {
        mFloatSB.showFirstIndicatorWithAnim()
    }

    /**
     * 显示二级指示器
     */
    fun showSecondaryIndicatorWithAnim() {
        mFloatSB.showSecondaryIndicatorWithAnim()
    }

    fun showFirstIndicator(){
        mFloatSB.showFirstIndicator()
    }

    fun showSecondaryIndicator(){
        mFloatSB.showSecondaryIndicator()
    }

    companion object {

        private const val DURATION_FORMAT_MS = "%02d:%02d"

        private const val DURATION_FORMAT_HMS = "%02d:%02d:%02d"

        private const val ANIM_DURATION = 800L
    }
}