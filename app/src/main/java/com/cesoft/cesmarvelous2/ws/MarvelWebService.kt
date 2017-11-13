package com.cesoft.cesmarvelous2.ws


import rx.Observable
import retrofit.http.*
import retrofit.Retrofit
import retrofit.GsonConverterFactory
import retrofit.RxJavaCallAdapterFactory
import com.google.gson.GsonBuilder

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
		@Query("limit") limit: Int)
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
//			val loggingInterceptor = HttpLoggingInterceptor()
//			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//			val client = OkHttpClient()
//			client.interceptors().add(loggingInterceptor)
			// Create an interceptor which catches requests and logs the info you want
			/*val logRequests = object : RequestInterceptor() {
				fun execute(request: Request): Request {
					Log.i("REQUEST INFO", request.toString())
					return request // return the request unaltered
				}
			}
			val client = OkHttpClient()
			val requestInterceptors = client.requestInterceptors()
			requestInterceptros.add(logRequests)*/

			val restAdapter = Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
				//.client(client)
				.build()
			return restAdapter.create(MarvelWebService::class.java)
		}
	}

}
