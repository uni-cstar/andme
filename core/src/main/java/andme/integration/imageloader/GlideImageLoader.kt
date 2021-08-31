package andme.integration.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

/**
 * Created by Lucio on 2020-11-11.
 */
object GlideImageLoader : ImageLoader {

    /**
     * Clears disk cache.
     * This method should always be called on a background thread, since it is a blocking call.
     */
    @WorkerThread
    override fun clearDiskCache(ctx: Context) {
        Glide.get(ctx).clearDiskCache()
    }

    /**
     * Clears as much memory as possible.
     * call this method must on Main Thread
     *
     * @see android.content.ComponentCallbacks.onLowMemory
     * @see android.content.ComponentCallbacks2.onLowMemory
     */
    @MainThread
    override fun clearMemoryCache(ctx: Context) {
        Glide.get(ctx).clearMemory()
    }

    /**
     * 清除[view]上的图片加载请求
     */
    override fun clear(view: View) {
        Glide.with(view).clear(view)
    }

    override fun load(imageView: ImageView, url: String?) {
        Glide.with(imageView)
                .load(url)
                .into(imageView)
    }

    /**
     * 加载图片
     */
    override fun load(imageView: ImageView, url: String?, placeHolder: Int) {
        Glide.with(imageView)
                .load(url)
                .placeholder(placeHolder)
                .into(imageView)
    }

    override fun load(imageView: ImageView, url: String?, placeHolder: Drawable) {
        Glide.with(imageView)
                .load(url)
                .placeholder(placeHolder)
                .into(imageView)
    }

    override fun load(imageView: ImageView, url: String?, placeHolder: Int, errorPlaceHolder: Int) {
        Glide.with(imageView)
                .load(url)
                .placeholder(placeHolder)
                .error(errorPlaceHolder)
                .into(imageView)
    }

    override fun load(imageView: ImageView, url: String?, placeHolder: Drawable, errorPlaceHolder: Drawable) {
        Glide.with(imageView)
                .load(url)
                .placeholder(placeHolder)
                .error(errorPlaceHolder)
                .into(imageView)
    }

    override fun load(imageView: ImageView, url: String?, placeHolder: Int, errorPlaceHolder: Drawable) {
        Glide.with(imageView)
                .load(url)
                .placeholder(placeHolder)
                .error(errorPlaceHolder)
                .into(imageView)
    }

    override fun load(imageView: ImageView, url: String?, placeHolder: Drawable, errorPlaceHolder: Int) {
        Glide.with(imageView)
                .load(url)
                .placeholder(placeHolder)
                .error(errorPlaceHolder)
                .into(imageView)
    }

    fun loadCircleImage(
            imageView: ImageView, url: String?
    ) {
       
        Glide.with(imageView)
                .load(url)
                .transform(CenterCrop(), CircleCrop())
                .into(imageView)
    }
    
    fun loadCircleImage(
            imageView: ImageView, url: String?,
            @DrawableRes placeHolder: Int
    ) {
        val placeHolderLoader = Glide.with(imageView)
                .load(placeHolder)
                .transform(CenterCrop(), CircleCrop())
        Glide.with(imageView)
                .load(url)
                .thumbnail(placeHolderLoader)
                .transform(CenterCrop(), CircleCrop())
                .into(imageView)
    }

    fun loadCircleImage(
            imageView: ImageView, url: String?,
            placeHolder: Drawable
    ) {
        val placeHolderLoader = Glide.with(imageView)
                .load(placeHolder)
                .transform(CenterCrop(), CircleCrop())
        Glide.with(imageView)
                .load(url)
                .thumbnail(placeHolderLoader)
                .transform(CenterCrop(), CircleCrop())
                .into(imageView)
    }

    fun loadRoundedImage(
            imageView: ImageView,
            url: String?,
            roundingRadius: Int
    ) {
        Glide.with(imageView)
                .load(url)
                .transform(CenterCrop(), RoundedCorners(roundingRadius))
                .into(imageView)
    }

    fun loadRoundedImage(
            imageView: ImageView,
            url: String?,
            @DrawableRes placeHolder: Int,
            roundingRadius: Int
    ) {
        val placeHolderLoader = Glide.with(imageView)
                .load(placeHolder)
                .transform(CenterCrop(), RoundedCorners(roundingRadius))
        Glide.with(imageView)
                .load(url)
                .thumbnail(placeHolderLoader)
                .transform(CenterCrop(), RoundedCorners(roundingRadius))
                .into(imageView)
    }

    fun loadRoundedImage(
            imageView: ImageView,
            url: String?,
            placeHolder: Drawable,
            roundingRadius: Int
    ) {
        val placeHolderLoader = Glide.with(imageView)
                .load(placeHolder)
                .transform(CenterCrop(), RoundedCorners(roundingRadius))
        Glide.with(imageView)
                .load(url)
                .thumbnail(placeHolderLoader)
                .transform(CenterCrop(), RoundedCorners(roundingRadius))
                .into(imageView)
    }

}