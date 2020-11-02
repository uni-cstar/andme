package andme.arch.activity

/**
 * Created by Lucio on 2020-11-02.
 * 内部使用。用于取消订阅的链式设计。
 */
internal interface Cancellable {

    /**
     * 取消订阅.
     */
    fun cancel()
}