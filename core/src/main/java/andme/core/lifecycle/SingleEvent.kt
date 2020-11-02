package andme.core.lifecycle

/**
 * Created by Lucio on 2019-11-02.
 * 用于在ViewModel中只执行一次的事件：比如Snakbar，dialog，toast等
 * 注意：只有一个观察者能够接收到通知
 */
class SingleEvent : SingleLiveEvent<Any?>()