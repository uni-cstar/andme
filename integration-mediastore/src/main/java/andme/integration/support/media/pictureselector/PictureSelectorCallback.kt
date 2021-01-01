package andme.integration.support.media.pictureselector

import andme.integration.media.PictureSelector
import andme.integration.support.media.Util
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

/**
 * Created by Lucio on 2020-11-11.
 * 自定义图片选择回调：用于包装第三方库的图片回调
 */

class PictureSelectorCallback(val callback: PictureSelector.Callback) : OnResultCallbackListener<LocalMedia> {
    override fun onResult(result: MutableList<LocalMedia>?) {
        callback.onPictureSelectResult(Util.mapResult(result))
    }

    override fun onCancel() {
        callback.onPictureSelectCanceled()
    }
}