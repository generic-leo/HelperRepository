package com.leo.homeloan.data.retro

import com.leo.homeloan.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit





object RetrofitConfiguration{
    private fun getOkHttpClient(): OkHttpClient {
        val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        connectionSpec.tlsVersions(TlsVersion.TLS_1_2).build()

        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build()

        val certificatePinner = CertificatePinner.Builder()
                .add("pratham.xyz", "sha256/47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=")
                .build()

        val okhttpClient : OkHttpClient.Builder = when (BuildConfig.DEBUG){
            true -> {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                OkHttpClient.Builder().addInterceptor(interceptor)
            }
            false -> OkHttpClient.Builder()
        }

        okhttpClient.connectTimeout(60, TimeUnit.SECONDS)
        okhttpClient.readTimeout(60, TimeUnit.SECONDS)
        okhttpClient.writeTimeout(60, TimeUnit.SECONDS)


        return okhttpClient.build()
    }

    fun retrofitClient(): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}