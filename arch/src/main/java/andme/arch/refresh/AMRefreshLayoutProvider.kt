package andme.arch.refresh

/**
 * Created by Lucio on 2020/12/16.
 */
interface AMRefreshLayoutProvider {

    fun getRefreshLayout(): AMRefreshLayout?

    fun getLoadMoreLayout(): AMLoadMoreLayout?
}