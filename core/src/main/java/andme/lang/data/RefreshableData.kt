package andme.lang.data

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2019/10/23.
 * 会刷新的数据：指定的[refreshTime]时间之后，重新调用[acquirer]获取数据
 * @param refreshTime 数据定时时间
 * @param acquirer 重新获取数据器
 */
class RefreshableData<T> constructor(val refreshTime: Long,
                                     val acquirer: (() -> T?)) : ReadOnlyProperty<Any?, T?> {

    //当前数据设置的时间
    private var _currentDataSetupTime: Long = 0
    private var _data: T? = null

    constructor(refreshTime: Long, default: T?, acquirer: (() -> T?)) : this(refreshTime, acquirer) {
        //此构造函数会在间隔refreshTime重新调用数据函数，主构造函数会在第一次使用值时调用获取数据函数
        _data = default
        _currentDataSetupTime = System.currentTimeMillis()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        if (System.currentTimeMillis() - _currentDataSetupTime >= refreshTime) {
            //如果超过了timingTime，则重新获取数据
            _data = acquirer.invoke()
            _currentDataSetupTime = System.currentTimeMillis()
        }
        return _data
    }

}