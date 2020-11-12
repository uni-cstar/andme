package andme.integration.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread

/**
 * Created by Lucio on 2020-11-10.
 */
interface ImageLoader {

    /**
     * Clears disk cache.
     * This method should always be called on a background thread, since it is a blocking call.
     */
    @WorkerThread
    fun clearDiskCache(ctx: Context)

    /**
     * Clears as much memory as possible.
     * call this method must on Main Thread
     *
     * @see android.content.ComponentCallbacks.onLowMemory
     * @see android.content.ComponentCallbacks2.onLowMemory
     */
    @MainThread
    fun clearMemoryCache(ctx: Context)

    /**
     * 清除[view]上的图片加载请求
     */
    fun clear(view: View)

    fun load(imageView: ImageView, url: String?)

    fun load(imageView: ImageView, url: String?, @DrawableRes placeHolder: Int)

    fun load(imageView: ImageView, url: String?, placeHolder: Drawable)

    fun load(imageView: ImageView, url: String?, @DrawableRes placeHolder: Int, @DrawableRes errorPlaceHolder: Int)

    fun load(imageView: ImageView, url: String?, placeHolder: Drawable, errorPlaceHolder: Drawable)

    fun load(imageView: ImageView, url: String?, @DrawableRes placeHolder: Int, errorPlaceHolder: Drawable)

    fun load(imageView: ImageView, url: String?, placeHolder: Drawable, @DrawableRes errorPlaceHolder: Int)
}