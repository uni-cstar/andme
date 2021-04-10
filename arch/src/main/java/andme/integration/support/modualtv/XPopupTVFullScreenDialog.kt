package andme.integration.support.modualtv

import android.content.Context
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.FullScreenDialog
import com.lxj.xpopup.core.PopupInfo

/**
 * Created by Lucio on 2021/3/17.
 */
class XPopupTVFullScreenDialog(context: Context) : FullScreenDialog(context) {

    private lateinit var mContentView: BasePopupView

    override fun setContent(view: BasePopupView): FullScreenDialog {
        mContentView = view
        return super.setContent(view)
    }

    private inline val popupInfo: PopupInfo? get() = mContentView.popupInfo

    override fun onBackPressed() {
        val popupInfo = popupInfo
        if(!::mContentView.isInitialized || popupInfo == null
            || popupInfo.xPopupCallback == null || !popupInfo.xPopupCallback.onBackPressed(mContentView)){
            super.onBackPressed()
            return
        }
    }
}