package com.flixflash.contactmanagerai.di

import com.flixflash.contactmanager.BuildConfig
import com.flixflash.contactmanagerai.data.network.CallerIdApi
import com.flixflash.contactmanagerai.data.network.SpamApi
import com.flixflash.contactmanagerai.data.network.RasaApi
import com.flixflash.contactmanagerai.data.settings.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
	@Provides @Singleton
	fun provideBaseUrlInterceptor(settings: SettingsRepository): Interceptor = Interceptor { chain ->
		val original: Request = chain.request()
		val current = runBlocking { settings.settingsFlow.first() }
		val base = current.callerIdUrl.ifBlank { BuildConfig.CALLERID_BASE_URL }.toHttpUrl()
		val newUrl = original.url.newBuilder()
			.scheme(base.scheme)
			.host(base.host)
			.port(base.port)
			.build()
		chain.proceed(original.newBuilder().url(newUrl).build())
	}

	@Provides @Singleton
	fun provideOkHttp(baseUrlInterceptor: Interceptor): OkHttpClient {
		val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
		return OkHttpClient.Builder().addInterceptor(baseUrlInterceptor).addInterceptor(logger).build()
	}

	@Provides @Singleton
	fun provideRetrofit(client: OkHttpClient, settings: SettingsRepository): Retrofit {
		val base = runBlocking { settings.settingsFlow.first().callerIdUrl.ifBlank { BuildConfig.CALLERID_BASE_URL } }
		return Retrofit.Builder()
			.baseUrl(base)
			.client(client)
			.addConverterFactory(MoshiConverterFactory.create())
			.build()
	}

	@Provides @Singleton
	fun provideCallerIdApi(retrofit: Retrofit): CallerIdApi = retrofit.create(CallerIdApi::class.java)

	@Provides @Singleton
	fun provideSpamApi(retrofit: Retrofit): SpamApi = retrofit.create(SpamApi::class.java)

	@Provides @Singleton
	fun provideRasaRetrofit(settings: SettingsRepository, baseClient: OkHttpClient): Retrofit {
		val base = runBlocking { settings.settingsFlow.first().rasaUrl.ifBlank { BuildConfig.RASA_BASE_URL } }.toHttpUrl()
		val client = baseClient.newBuilder().addInterceptor { chain ->
			val original = chain.request(); val url = original.url
			val newUrl = url.newBuilder().scheme(base.scheme).host(base.host).port(base.port).build()
			chain.proceed(original.newBuilder().url(newUrl).build())
		}.build()
		return Retrofit.Builder()
			.baseUrl(base)
			.client(client)
			.addConverterFactory(MoshiConverterFactory.create())
			.build()
	}

	@Provides @Singleton
	fun provideRasaApi(rasaRetrofit: Retrofit): RasaApi = rasaRetrofit.create(RasaApi::class.java)
}