@file:JvmName("AMSharedPref")
@file:JvmMultifileClass
package andme.core.content

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Lucio on 2020-03-10.
 */


internal const val SHARED_PREF_VERSION_CODE = "_am_sp_version_code"

/**
 * 实现可以检查更新的SharedPreferences
 * @param name 文件名字
 * @param mode
 */
internal class AMSharedPrefImpl constructor(
        ctx: Context,
        name: String,
        version: Long,
        mode: Int,
        onUpdate: OnAMSharedPrefUpgradeListener?
) : SharedPreferences by ctx.getSharedPreferences(name, mode) {
    init {
        //检测版本更新
        val oldVersion: Long = getLong(SHARED_PREF_VERSION_CODE, 0)
        if (version > oldVersion) {
            //写入新的版本号
            edit().putLong(SHARED_PREF_VERSION_CODE, version).apply()
            onUpdate?.onAMSharedPrefUpgrade(this, oldVersion, version)
        }
    }
}
