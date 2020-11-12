package andme.integration.support.media

import andme.core.imageLoaderAM
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView

/**
 * Created by Lucio on 2020-11-10.
 */
internal object ImageLoaderEngine : ImageEngine {

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        imageLoaderAM.load(imageView,url)
    }

    override fun loadImage(context: Context, url: String, imageView: ImageView, longImageView: SubsamplingScaleImageView?, callback: OnImageCompleteCallback?) {
    }

    override fun loadImage(context: Context, url: String, imageView: ImageView, longImageView: SubsamplingScaleImageView?) {
    }

    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        imageLoaderAM.load(imageView, url)
    }

    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        imageLoaderAM.load(imageView, url)
    }
}