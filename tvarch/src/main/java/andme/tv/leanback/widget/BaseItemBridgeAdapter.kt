package andme.tv.leanback.widget

import androidx.leanback.widget.ItemBridgeAdapter
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.PresenterSelector

/**
 * Created by Lucio on 2021/3/29.
 */
class BaseItemBridgeAdapter :ItemBridgeAdapter{

    constructor(adapter: ObjectAdapter?, presenterSelector: PresenterSelector?) : super(
        adapter,
        presenterSelector
    )

    constructor(adapter: ObjectAdapter?) : super(adapter)
    constructor() : super()


}