package andme.integration.support.binding

import andme.core.binding.bindTextOrGone
import andme.core.imageLoaderAM
import andme.core.mApp
import andme.core.util.dip
import andme.integration.imageloader.GlideImageLoader
import andme.lang.orDefault
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

/**
 * Created by Lucio on 2021/1/14.
 */


/**
 * 默认圆角半径
 */
private val defaultRoundingRadius by lazy {
    mApp.dip(6)
}

@BindingAdapter("bindImageRes")
fun bindImageResource(view: ImageView, @DrawableRes resId: Int){
    view.setImageResource(resId)
}
/**
 * 图片加载绑定属性
 * 如未设置placeHolder属性，则使用默认图片占位
 */
@BindingAdapter(value = ["bindImage", "placeHolder"], requireAll = false)
fun bindImage(view: ImageView, imageUrl: String?, placeHolder: Drawable?) {
    if (placeHolder == null) {
        imageLoaderAM.load(view, imageUrl)
    } else {
        imageLoaderAM.load(view, imageUrl, placeHolder)
    }
}

inline fun bindImage(view: ImageView, imageUrl: String?, placeHolder: Int) {
    imageLoaderAM.load(view, imageUrl, placeHolder)
}

@BindingAdapter(value = ["bindRoundedImage", "placeHolder", "roundingRadius"], requireAll = false)
fun bindRoundedImage(view: ImageView, url: String?, placeHolder: Drawable?, roundingRadius: Int?) {
    if (placeHolder == null) {
        GlideImageLoader.loadRoundedImage(
            view,
            url,
            roundingRadius.orDefault(defaultRoundingRadius)
        )
    } else {
        GlideImageLoader.loadRoundedImage(
            view,
            url,
            placeHolder,
            roundingRadius.orDefault(defaultRoundingRadius)
        )
    }
}

@BindingAdapter(value = ["bindCircleImage", "placeHolder"], requireAll = false)
fun bindCircleImage(view: ImageView, url: String?, placeHolder: Drawable?) {
    if (placeHolder == null) {
        GlideImageLoader.loadCircleImage(view, url)
    } else {
        GlideImageLoader.loadCircleImage(view, url, placeHolder)
    }
}

@BindingAdapter("textOrGone")
fun bindTextOrGone(view: TextView, message: CharSequence?) {
    view.bindTextOrGone(message)
}

/**
 * 绑定文本；文本为空时隐藏控件，在文本不为空时并执行[onVisibleInvoke]回调
 */
inline fun bindTextOrGone(
    view: TextView,
    message: CharSequence?,
    noinline onVisibleInvoke: TextView.() -> Unit
) {
    view.bindTextOrGone( message, onVisibleInvoke)
}

@BindingAdapter("textOrInvisible")
fun bindTextOrInvisible(view: TextView, message: CharSequence?) {
    if (message.isNullOrEmpty()) {
        view.visibility = View.INVISIBLE
        view.text = ""
    } else {
        view.visibility = View.VISIBLE
        view.text = message
    }
}

@BindingAdapter("visibleOrGone")
fun bindVisibleOrGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOrGone")
fun bindVisibleOrGone(view: View, values: Collection<*>?) {
    view.visibility = if (!values.isNullOrEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOrInvisible")
fun bindVisibleOrNot(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("selection")
fun bindSelection(view: EditText, position: Int) {
    view.setSelection(position)
}

@BindingAdapter("bindActive")
fun bindSelected(view: View, active: Boolean) {
    view.isActivated = active
}
