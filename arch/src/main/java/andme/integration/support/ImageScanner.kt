package andme.integration.support

import andme.integration.support.modual.XPopupImageLoaderImpl
import android.content.Context
import android.widget.ImageView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener
import com.lxj.xpopup.interfaces.XPopupImageLoader

/**
 * Created by Lucio on 2021/6/23.
 */

object ImageScanner {


    fun invoke(
        ctx: Context,
        url: Any,
        imageLoader: XPopupImageLoader = XPopupImageLoaderImpl,
    ) {
        XPopup.Builder(ctx).asImageViewer(null, url, imageLoader).show()
    }

    fun invoke(
        srcView: ImageView,
        url: String,
        imageLoader: XPopupImageLoader = XPopupImageLoaderImpl,
    ) {
        XPopup.Builder(srcView.context).asImageViewer(srcView, url, imageLoader).show()
    }

    fun invoke(
        srcView: ImageView,
        currentPosition:Int,
        urls: List<Any>,
        srcViewUpdateListener: OnSrcViewUpdateListener,
        imageLoader: XPopupImageLoader = XPopupImageLoaderImpl,
    ) {
        XPopup.Builder(srcView.context).asImageViewer(srcView,currentPosition,urls,srcViewUpdateListener,imageLoader).show()
    }

    fun invoke(
        ctx: Context,
        currentPosition:Int,
        urls: List<Any>,
        imageLoader: XPopupImageLoader = XPopupImageLoaderImpl,
    ) {
        XPopup.Builder(ctx).asImageViewer(null,currentPosition,urls,null,imageLoader).show()
    }

}