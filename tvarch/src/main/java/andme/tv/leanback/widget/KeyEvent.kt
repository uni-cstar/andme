package andme.tv.leanback.widget

import android.view.KeyEvent

/**
 * Created by Lucio on 2021/5/3.
 */


/**
 * 是否是第一次按下按键
 */
val KeyEvent.isUniqueDown:Boolean get() = action == KeyEvent.ACTION_DOWN && repeatCount == 0