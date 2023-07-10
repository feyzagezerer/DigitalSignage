package com.fey.signage.di

import com.fey.signage.api.LoggingInterceptor
import com.fey.signage.api.OctopusApi
import com.fey.signage.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideLoggingInterceptor(): LoggingInterceptor{
        return LoggingInterceptor()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: LoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun octopusNewsApi(retrofit: Retrofit): OctopusApi = retrofit.create(OctopusApi::class.java)
}