package andme.core.content

import android.os.CountDownTimer

/**
 * Created by Lucio on 2020/12/3.
 */
object AMCountDownTimerManager {

    private val mTimers = HashMap<String, AMCountDownTimer>()

    /**
     * 启动倒计时定时器
     * 如果对应key的计时器已经存在并正在运行，则复用之前的倒计时对象
     * @param key 用于唯一指定定时器的key
     * @param total 倒计时时间
     * @param interval 执行间隔时间

     * @param callback 回调
     * @return
     */
    fun start(key: String, total: Long, interval: Long, callback: Callback): AMCountDownTimer {
        var timer: AMCountDownTimer? = mTimers[key]
        if (timer != null) {//当前队列中已有同key的倒计时器
            timer.addCallbackDistinct(callback)
            return timer
        } else {
            timer = AMCountDownTimer(key, total, interval).also {
                it.addCallback(callback)
                mTimers[key] = it
            }
            timer.start()
            return timer
        }
    }

    /**
     * 移除定时器
     */
    fun remove(key: String) {
        val timer = mTimers[key] ?: return
        timer.clearCallbacks()
        mTimers.remove(key)
    }

    fun isContains(key: String): Boolean {
        return mTimers.containsKey(key)
    }

    fun get(key: String): AMCountDownTimer? {
        return mTimers[key]
    }

    interface Callback {
        fun onTick(millisUntilFinished: Long, total: Long, interval: Long)

        fun onFinish()
    }

    /**
     * @param key 定时器标识
     * @param totalMillis 总时间
     * @param interval 间隔执行时间
     */
    class AMCountDownTimer(val key: String, val totalMillis: Long, val interval: Long) {

        var isFinished = false
            private set

        var isCanceled: Boolean = false
            private set

        //实际的timer实现
        val timer: CountDownTimer

        private val callbacks: MutableList<Callback> = mutableListOf()

        init {
            timer = object : CountDownTimer(totalMillis, interval) {
                override fun onTick(millisUntilFinished: Long) {
                    onTickInternal(millisUntilFinished)
                }

                override fun onFinish() {
                    onFinishInternal()
                }
            }
        }

        /**
         * 只供Manager调用
         */
        @Synchronized
        internal fun start() {
            isFinished = false
            isCanceled = false
            timer.start()
        }

        @Synchronized
        fun cancel() {
            isCanceled = true
            timer.cancel()
        }

        private fun onTickInternal(millisUntilFinished: Long) {
            isFinished = false
            isCanceled = false
            callbacks.forEach {
                it.onTick(millisUntilFinished, totalMillis, interval)
            }
        }

        private fun onFinishInternal() {
            isFinished = true
            //移除自己
            mTimers.remove(key)
            //触发回调
            callbacks.forEach {
                it.onFinish()
            }
            //解除回调绑定
            clearCallbacks()
        }

        /**
         * 清除回调
         */
        fun clearCallbacks() {
            callbacks.clear()
        }

        fun addCallback(callback: Callback) {
            callbacks.add(callback)
        }

        fun addCallbackDistinct(callback: Callback) {
            if (callbacks.contains(callback))
                return
            addCallback(callback)
        }

        fun removeCallback(callback: Callback) {
            callbacks.remove(callback)
        }
    }
}


