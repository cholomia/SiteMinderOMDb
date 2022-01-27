package com.pocholomia.sitemindertechnical.di

import com.pocholomia.sitemindertechnical.data.remote.MoviesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://www.omdbapi.com/"

    @Singleton
    @Provides
    fun converterFactory(): Converter.Factory = MoshiConverterFactory.create()

    @Singleton
    @Provides
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Singleton
    @Provides
    fun retrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun moviesApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)

}