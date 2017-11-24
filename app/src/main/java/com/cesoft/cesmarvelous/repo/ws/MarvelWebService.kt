package com.cesoft.cesmarvelous.repo.ws


import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by ccasanova on 08/11/2017
 */
//https://developer.marvel.com/docs
interface MarvelWebService {

	//______________________________________________________________________________________________
	@Headers("Accept: */*")
	@GET("/v1/public/comics")
	fun getComics(
		@Query("orderBy") orderBy: String,
		@Query("ts") ts: String,
		@Query("apikey") apiKey: String,
		@Query("hash") hash: String,
		@Query("limit") limit: Int,
		@Query("offset") offset: Int)
			: Observable<ComicDataResponse>

	//______________________________________________________________________________________________
	/*
	fun getComics(defaultLimit: Int) : Observable<ComicDataResponse>
	{
		val timestamp = java.util.Date().time
		val hash = Util.md5(timestamp.toString() + BuildConfig.PRIVATE_API_KEY + BuildConfig.PUBLIC_API_KEY)
		return _getComics_(ORDER_COMIC_TITLE, timestamp.toString(), BuildConfig.PUBLIC_API_KEY, hash, defaultLimit)
	}*/

	//______________________________________________________________________________________________
	companion object {
		val ORDER_COMIC_TITLE: String = "title"
		//private val BASE_URL = "http://gateway.marvel.com"
		private val BASE_URL = "https://gateway.marvel.com:443"

		fun create() : MarvelWebService {
			val restAdapter = Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
				.build()
			return restAdapter.create(MarvelWebService::class.java)
		}
	}

}
