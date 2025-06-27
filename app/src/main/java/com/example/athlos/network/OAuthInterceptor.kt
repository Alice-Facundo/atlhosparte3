package com.example.athlos.network

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLEncoder
import java.util.SortedMap
import java.util.TreeMap
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class OAuthInterceptor(
    private val consumerKey: String,
    private val consumerSecret: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val nonce = UUID.randomUUID().toString().replace("-", "")
        val timestamp = (System.currentTimeMillis() / 1000).toString()

        val params = TreeMap<String, String>().apply {
            put("oauth_consumer_key", consumerKey)
            put("oauth_nonce", nonce)
            put("oauth_signature_method", "HMAC-SHA1")
            put("oauth_timestamp", timestamp)
            put("oauth_version", "1.0")

            for (name in originalUrl.queryParameterNames) {
                put(name, originalUrl.queryParameter(name) ?: "")
            }
        }

        val baseString = buildBaseString(originalRequest.method, originalUrl.newBuilder().query(null).build().toString(), params)
        val signature = generateSignature(baseString, consumerSecret)

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("oauth_consumer_key", consumerKey)
            .addQueryParameter("oauth_nonce", nonce)
            .addQueryParameter("oauth_signature_method", "HMAC-SHA1")
            .addQueryParameter("oauth_timestamp", timestamp)
            .addQueryParameter("oauth_version", "1.0")
            .addQueryParameter("oauth_signature", signature)
            .build()

        val newRequest = originalRequest.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }

    private fun buildBaseString(method: String, baseUrl: String, params: SortedMap<String, String>): String {
        val paramString = params.map {
            "${encode(it.key)}=${encode(it.value)}"
        }.joinToString("&")

        return listOf(
            method.uppercase(),
            encode(baseUrl),
            encode(paramString)
        ).joinToString("&")
    }

    private fun generateSignature(baseString: String, consumerSecret: String): String {
        val key = "$consumerSecret&"
        val mac = Mac.getInstance("HmacSHA1")
        val secret = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "HmacSHA1")
        mac.init(secret)
        val hmac = mac.doFinal(baseString.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(hmac, Base64.NO_WRAP)
    }

    private fun encode(value: String): String {
        return URLEncoder.encode(value, "UTF-8")
            .replace("+", "%20")
            .replace("*", "%2A")
            .replace("%7E", "~")
    }
}