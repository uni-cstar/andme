package andme.tv.leanback.multistate

import andme.lang.orDefault
import andme.tv.arch.R
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

/**
 * A fragment for displaying an error indication.
 */
open class ErrorFragment : BaseMultiStateFragment() {

    private var mErrorFrame: ErrorView? = null
    private var mDrawable: Drawable? = null
    private var mMessage: CharSequence? = null
    private var mButtonText: String? = null

    /**
     * 按钮点击事件：默认回调到viewmodel中
     */
    private var mButtonClickListener: View.OnClickListener? = View.OnClickListener {
        multiStateViewModel.onErrorButtonClickEvent.call()
    }

    private var mBackgroundDrawable: Drawable? = null



    /**
     * Returns true if the background is translucent.
     */
    var isBackgroundTranslucent = true
        private set

    /**
     * Sets the default background.
     *
     * @param translucent True to set a translucent background.
     */
    fun setDefaultBackground(translucent: Boolean) {
        mBackgroundDrawable = null
        isBackgroundTranslucent = translucent
        updateBackground()
        updateMessage()
    }
    /**
     * Returns the background drawable.  May be null if a default is used.
     */
    /**
     * Sets a drawable for the fragment background.
     *
     * @param drawable The drawable used for the background.
     */
    var backgroundDrawable: Drawable?
        get() = mBackgroundDrawable
        set(drawable) {
            mBackgroundDrawable = drawable
            if (drawable != null) {
                val opacity = drawable.opacity
                isBackgroundTranslucent = (opacity == PixelFormat.TRANSLUCENT
                        || opacity == PixelFormat.TRANSPARENT)
            }
            updateBackground()
            updateMessage()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mErrorFrame = ErrorView(requireContext())
        updateBackground()
        updateImageDrawable()
        updateMessage()
        updateButton()
        initViewModelEvent()
        return mErrorFrame
    }

    private fun initViewModelEvent() {
        multiStateViewModel.errorMessageEvent.let {
            it.removeObservers(this)
            it.observe(this, Observer {
                message = it
            })
        }

        multiStateViewModel.errorButtonTextEvent.let {
            it.removeObservers(this)
            it.observe(this, Observer {
                buttonText = it
            })
        }

        multiStateViewModel.errorBackgroundEvent.let {
            it.removeObservers(this)
            it.observe(this, Observer {
                backgroundDrawable = it
            })
        }

        multiStateViewModel.errorImageEvent.let {
            it.removeObservers(this)
            it.observe(this, Observer {
                imageDrawable = it
            })
        }
    }

    private fun updateBackground() {
        mErrorFrame?.let {
            if (mBackgroundDrawable != null) {
                it.background = mBackgroundDrawable
            } else {
                it.setBackgroundColor(
                    it.resources.getColor(
                        if (isBackgroundTranslucent) R.color.lb_error_background_color_translucent else R.color.lb_error_background_color_opaque
                    )
                )
            }
        }
    }

    private fun updateMessage() {
        mErrorFrame?.message = mMessage
    }

    private fun updateImageDrawable() {
        mErrorFrame?.imageDrawable = mDrawable
    }

    private fun updateButton() {
        mErrorFrame?.let {
            it.buttonText = mButtonText
            it.buttonClickListener = mButtonClickListener
        }
    }

    override fun onStart() {
        super.onStart()
        mErrorFrame?.requestFocus()
    }


    companion object{

        @JvmStatic
        fun newInstance():ErrorFragment{
            return ErrorFragment()
        }
    }
}