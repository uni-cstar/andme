package andme.integration.http

import okhttp3.Headers

/**
 * Created by SupLuo on 2016/5/17.
 * token interceptor for put token into headers of request
 *
 * usage : create an instance of this and as a parameter for ApiClient
 * 使用拦截器在请求中放入header参数，适合所有api统一处理的情况
 */
abstract class HeadersInterceptor : okhttp3.Interceptor {

    private inline fun Headers?.contains(key:String):Boolean{
        if(this == null)
            return false
        return key.isNotEmpty() && !this.get(key).isNullOrEmpty()
    }

    @Throws(java.io.IOException::class)
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        willRequestUrl(originalRequest.url().toString())

        val customHeaders = getHeaders()
        val originalHeaders = originalRequest.headers()

        val preHeaders = customHeaders?.filterNot {
            originalHeaders.contains(it.key)
        }
        
        if(preHeaders.isNullOrEmpty())
            return chain.proceed(originalRequest)

        val builder = originalRequest.newBuilder()
        preHeaders.forEach {
            builder.addHeader(it.key,it.value)
        }
        return chain.proceed(builder.build())
    }

    abstract fun getHeaders(): Map<String, String>?

    /**
     * execute before request the url
     * @param url
     */
    protected open fun willRequestUrl(url: String) {
        //nothing
    }
}