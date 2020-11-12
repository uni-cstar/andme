package andme.integration.support.media

import andme.integration.media.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

/**
 * Created by Lucio on 2020-11-11.
 */

class PictureSelectorCallback(val callback:PictureSelector.Callback) : OnResultCallbackListener<LocalMedia> {
    override fun onResult(result: MutableList<LocalMedia>?) {
        callback.onPictureSelectResult(Util.mapResult(result))
    }

    override fun onCancel() {
        callback.onPictureSelectCanceled()
    }
}