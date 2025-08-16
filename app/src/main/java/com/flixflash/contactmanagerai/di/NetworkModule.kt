package com.flixflash.contactmanagerai.di

import com.flixflash.contactmanager.BuildConfig
import com.flixflash.contactmanagerai.data.network.CallerIdApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
	@Provides @Singleton
	fun provideOkHttp(): OkHttpClient {
		val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
		return OkHttpClient.Builder().addInterceptor(logger).build()
	}

	@Provides @Singleton
	fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
		.baseUrl(BuildConfig.CALLERID_BASE_URL)
		.client(client)
		.addConverterFactory(MoshiConverterFactory.create())
		.build()

	@Provides @Singleton
	fun provideCallerIdApi(retrofit: Retrofit): CallerIdApi = retrofit.create(CallerIdApi::class.java)
}