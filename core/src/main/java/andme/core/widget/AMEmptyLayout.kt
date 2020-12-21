package andme.core.widget

import andme.core.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.LinearLayoutCompat

/**
 * Created by Lucio on 2020/12/19.
 */
open class AMEmptyLayout : FrameLayout {

    private val container: LinearLayoutCompat
    private val imageView: ImageView
    private val textView: TextView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.am_empty_layout, this)
        container = findViewById(R.id.am_empty_content)
        imageView = findViewById(R.id.am_empty_image)
        textView = findViewById(R.id.am_empty_text)
    }

    fun setEmptyText(text: CharSequence): AMEmptyLayout {
        textView.text = text
        return this
    }

    fun setEmptyText(@StringRes resId: Int): AMEmptyLayout {
        textView.setText(resId)
        return this
    }

    fun setEmptyImage(@DrawableRes resId: Int): AMEmptyLayout {
        imageView.setImageResource(resId)
        return this
    }

    fun setEmptyImage(bmp: Bitmap): AMEmptyLayout {
        imageView.setImageBitmap(bmp)
        return this
    }

    fun setEmptyImage(drawable: Drawable): AMEmptyLayout {
        imageView.setImageDrawable(drawable)
        return this
    }

    fun setEmptyImageVisibility(visibility: Int): AMEmptyLayout {
        imageView.visibility = visibility
        return this
    }

    fun setOrientation(@LinearLayoutCompat.OrientationMode orientation: Int): AMEmptyLayout {
        container.orientation = orientation
        return this
    }

}