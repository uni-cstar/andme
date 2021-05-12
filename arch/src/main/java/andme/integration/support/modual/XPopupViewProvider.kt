package andme.integration.support.modual

import andme.core.content.layoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * Created by Lucio on 2021/3/17.
 */
class XPopupViewProvider {

    private var layoutId: Int = 0

    protected lateinit var contentView: View
        private set

    constructor(layoutId: Int) {
        this.layoutId = layoutId
    }

    constructor(contentView: View) {
        this.contentView = contentView
    }

    open fun onCreateView(parent: ViewGroup): View {
        return if (layoutId > 0) {
            parent.context.layoutInflater.inflate(layoutId, parent, false)
        } else {
            return contentView
        }
    }
}
