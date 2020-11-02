package andme.arch.activity

/**
 * Created by Lucio on 2020-11-02.
 * 返回键拥有者，用于传递给回调异步调用
 */
interface AMBackPressedOwner{

    /**
     * 调用基类的返回键处理方法
     */
    fun invokeSuperBackPressed()
}