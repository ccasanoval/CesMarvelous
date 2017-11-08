package com.cesoft.cesmarvelous2.ws


import rx.Observable

import retrofit.http.*
import retrofit.Retrofit
import retrofit.GsonConverterFactory
import retrofit.RxJavaCallAdapterFactory

import com.google.gson.GsonBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor

/**
 * Created by ccasanova on 08/11/2017
 */
//https://developer.marvel.com/docs
interface MarvelWebService {

	@Headers("Accept: */*")
	@GET("/v1/public/comics")
	fun getComics(
		@Query("orderBy") orderBy: String,
		@Query("ts") ts: String,
		@Query("apikey") apiKey: String,
		@Query("hash") hash: String,
		@Query("limit") limit: Int)
			: Observable<ComicDataResponse>


	companion object {
		val ORDER_COMIC_TITLE: String = "title"
		private val BASE_URL = "http://gateway.marvel.com"

		fun create() : MarvelWebService {
			val gsonBuilder = GsonBuilder()

			val loggingInterceptor = HttpLoggingInterceptor()
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

			val client = OkHttpClient()
			client.interceptors().add(loggingInterceptor)

			val restAdapter = Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
				.client(client)
				.build()

			return restAdapter.create(MarvelWebService::class.java)
		}
	}

}
