package andme.tv.leanback.multistate

import andme.tv.arch.R
import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Lucio on 2021/3/3.
 */
class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mImageView: ImageView? = null
    private var mTextView: TextView? = null
    private var mButton: Button? = null
    private var mDrawable: Drawable? = null
    private var mMessage: CharSequence? = null
    private var mButtonText: String? = null
    private var mButtonClickListener: View.OnClickListener? = null

    init {
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
        isFocusable = true
        isFocusableInTouchMode = true
        var id = this.id
        if (id <= 0) {
            id = View.generateViewId()
            this.id = id
        }
        nextFocusDownId = id
        nextFocusLeftId = id
        nextFocusUpId = id
        nextFocusRightId = id

        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        inflate(context, R.layout.amtv_multi_state_error_layout, this)
        initViews()
    }

    private fun initViews() {

        mImageView = findViewById<ImageView>(R.id.image_layout)
        updateImageDrawable()
        mTextView = findViewById<TextView>(R.id.message)
        updateMessage()
        mButton = findViewById<Button>(R.id.button)
        updateButton()

        mTextView?.let {
            val metrics: Paint.FontMetricsInt = getFontMetricsInt(it)
            val underImageBaselineMargin = resources.getDimensionPixelSize(
                R.dimen.lb_error_under_image_baseline_margin
            )
            setTopMargin(it, underImageBaselineMargin + metrics.ascent)
            val underMessageBaselineMargin = resources.getDimensionPixelSize(
                R.dimen.lb_error_under_message_baseline_margin
            )
            mButton?.let {
                setTopMargin(it, underMessageBaselineMargin - metrics.descent)
            }
        }
    }


    /**
     * Returns the drawable used for the error image.
     */
    /**
     * Sets the drawable to be used for the error image.
     *
     * @param drawable The drawable used for the error image.
     */
    var imageDrawable: Drawable?
        get() = mDrawable
        set(drawable) {
            mDrawable = drawable
            updateImageDrawable()
        }
    /**
     * Returns the error message.
     */
    /**
     * Sets the error message.
     *
     * @param message The error message.
     */
    var message: CharSequence?
        get() = mMessage
        set(message) {
            mMessage = message
            updateMessage()
        }
    /**
     * Returns the button text.
     */
    /**
     * Sets the button text.
     *
     * @param text The button text.
     */
    var buttonText: String?
        get() = mButtonText
        set(text) {
            mButtonText = text
            updateButton()
        }
    /**
     * Returns the button click listener.
     */
    /**
     * Set the button click listener.
     *
     * @param clickListener The click listener for the button.
     */
    var buttonClickListener: View.OnClickListener?
        get() = mButtonClickListener
        set(clickListener) {
            mButtonClickListener = clickListener
            updateButton()
        }

    private fun updateMessage() {
        mTextView?.let {
            it.text = mMessage
            it.visibility = if (TextUtils.isEmpty(mMessage)) View.GONE else View.VISIBLE
        }
    }

    private fun updateImageDrawable() {
        mImageView?.let {
            it.setImageDrawable(mDrawable)
            it.visibility = if (mDrawable == null) View.GONE else View.VISIBLE
        }
    }

    private fun updateButton() {
        mButton?.let {
            it.text = mButtonText
            it.setOnClickListener(mButtonClickListener)
            it.visibility = if (mButtonText.isNullOrEmpty()) View.GONE else View.VISIBLE
            it.requestFocus()
        }
    }


    companion object {

        private fun getFontMetricsInt(textView: TextView): Paint.FontMetricsInt {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.textSize = textView.textSize
            paint.typeface = textView.typeface
            return paint.fontMetricsInt
        }

        private fun setTopMargin(textView: TextView, topMargin: Int) {
            val lp = textView.layoutParams as MarginLayoutParams
            lp.topMargin = topMargin
            textView.layoutParams = lp
        }

    }
}