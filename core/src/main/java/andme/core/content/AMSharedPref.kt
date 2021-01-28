@file:JvmName("AMSharedPref")
@file:JvmMultifileClass

package andme.core.content

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.IntRange
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 重构SharedPreferences，提供更新处理方法
 * @param version 当前版本号，如果当前版本号大于之前的版本号，则会触发[onUpgrade]
 * @param onUpgrade 更新回调的方法
 */
@JvmOverloads
fun Context.getSharedPrefAM(
        name: String,
        @IntRange(from = 1, to = Long.MAX_VALUE) version: Long = 1,
        mode: Int = Context.MODE_PRIVATE,
        onUpgrade: OnAMSharedPrefUpgradeListener? = null
): SharedPreferences {
    return AMSharedPrefImpl(this, name, version, mode, onUpgrade)
}


/**
 * SharedPreferences 属性:本地变量与SharedPreferences之间同步修改
 */
class AMSharedPrefProperty<T> : ReadWriteProperty<Any?, T> {

    private var _defValue: T
    private var _key: String by Delegates.notNull<String>()
    private var _pref: SharedPreferences? = null
    private var _prefProvider: AMSharePrefProvider? = null

    /**
     * 用于SharedPreferences 动态变换的情况。 i.e. 用户缓存数据
     * @param provider 动态获取SharedPreferences接口
     * @param key
     * @param def 默认值
     */
    constructor(provider: AMSharePrefProvider, key: String, def: T) {
        _prefProvider = provider
        _key = key
        _defValue = def
    }

    /**
     * 用于存储的SharedPreferences不改变的情况。 i.e. 公共缓存
     * @param def 默认值
     */
    constructor(sp: SharedPreferences, key: String, def: T) {
        _pref = sp
        _key = key
        _defValue = def
    }

    /**
     * 获取有效的Pref
     */
    private fun getPriorityPref(): SharedPreferences? = _prefProvider?.pref ?: _pref

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val prefs = getPriorityPref() ?: return _defValue
        return findPreference(prefs, _key, _defValue)
    }

    //获取值
    private fun findPreference(prefs: SharedPreferences, key: String, default: T): T {
        return prefs.let {
            val result: Any = when (default) {
                is Long -> it.getLong(key, default)
                is Int -> it.getInt(key, default)
                is Float -> it.getFloat(key, default)
                is Boolean -> it.getBoolean(key, default)
                is String? -> it.getString(key, default).orEmpty()
                else -> throw  IllegalArgumentException("无法识别的数据类型")
            }
            result as T
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val prefs = getPriorityPref() ?: return
        putPreference(prefs, _key, value)
    }

    //存放值
    private fun putPreference(sp: SharedPreferences, key: String, value: T) {
        sp.edit().apply {
            when (value) {
                is Long -> putLong(key, value)
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                else -> throw  IllegalArgumentException("this type can not be saved into preference")
            }
        }.apply()
    }

}

/**
 * 提供动态的SharedPreference（比如根据特定条件创建的sp）
 */
interface AMSharePrefProvider {
    val pref: SharedPreferences
}

/**
 * SharedPreferences 升级回调
 */
fun interface OnAMSharedPrefUpgradeListener {
    fun onAMSharedPrefUpgrade(sp: SharedPreferences, oldVersion: Long, newVersion: Long)
}

inline fun SharedPreferences.edit(
        action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    editor.apply()
}