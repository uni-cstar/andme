package andme.integration.support.modual

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File


/**
 * Created by Lucio on 2021/4/8.
 */
object XPopupImageLoaderImpl : XPopupImageLoader {
    override fun loadImage(position: Int, uri: Any, imageView: ImageView) {
        //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
        Glide.with(imageView).load(uri.toString())
            .apply(RequestOptions().override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL))
            .into(imageView)
    }

    override fun getImageFile(context: Context, uri: Any): File? {
        try {
            return Glide.with(context).downloadOnly().load(uri).submit().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}