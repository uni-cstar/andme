package andme.lang.data

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2019/10/23.
 * 会超时的数据：指定的[timeout]时间之后，获取的数据则为null. 设置数据之后，重新开始计时
 */
class TimerData<T> constructor(val timeout: Long) : ReadWriteProperty<Any, T?> {

    private var _data: T? = null
    private var _lastDataSetTime: Long = 0

    constructor(timeout: Long, default: T?) : this(timeout) {
        bindData(default)
    }

    private fun bindData(data: T?) {
        _data = data
        _lastDataSetTime = System.currentTimeMillis()
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        if (System.currentTimeMillis() - _lastDataSetTime >= timeout) {
            //如果超过了timingTime，则重新获取数据
            _data = null
        }
        return _data
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        bindData(value)
    }

}