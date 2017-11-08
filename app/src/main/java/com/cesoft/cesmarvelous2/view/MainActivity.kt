package com.cesoft.cesmarvelous2.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cesoft.cesmarvelous2.BuildConfig
import com.cesoft.cesmarvelous2.R
import com.cesoft.cesmarvelous2.util.Util
import com.cesoft.cesmarvelous2.util.Log
import com.cesoft.cesmarvelous2.ws.ComicDataResponse
import com.cesoft.cesmarvelous2.ws.MarvelWebService
import com.cesoft.cesmarvelous2.ws.MarvelWebService.Companion.ORDER_COMIC_TITLE
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

interface MainActivityViewModel { fun endCallProgress(any: Any?) }
class MainActivity : AppCompatActivity(), MainActivityViewModel {

	override fun endCallProgress(any: Any?) {
		Log.e(TAG, "endCallProgress:---------------------------------"+any)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.act_main)
	}

	override fun onResume() {
		super.onResume()
		Log.e(TAG, "onResume:---------------------------------")
		loadComicList(this)
	}



	private var service: MarvelWebService = MarvelWebService.create()



	private var _compoSub = CompositeSubscription()
	private val compoSub: CompositeSubscription
		get() {
			if(_compoSub.isUnsubscribed)
				_compoSub = CompositeSubscription()
			return _compoSub
		}

	lateinit var originalList: MutableList<ComicDataResponse>
	val defaultLimit = 20
	var countLimit = 0

	private fun manageSub(s: Subscription) = compoSub.add(s)
	fun unsubscribe() { compoSub.unsubscribe() }

	fun loadComicList(callback: MainActivityViewModel) {
		Log.e(TAG, "loadComicList:---------------------------------")

		val timestamp = Date().time
		val hash = Util.md5(timestamp.toString() + BuildConfig.PRIVATE_API_KEY + BuildConfig.PUBLIC_API_KEY)

		manageSub(
			service.getComics(ORDER_COMIC_TITLE, timestamp.toString(), BuildConfig.PUBLIC_API_KEY, hash, defaultLimit)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
				{
					c: ComicDataResponse ->
						callback.endCallProgress(c)
					Log.e(TAG, "loadComicList:--------1------------"+c.code+" : "+c.status)
					Log.e(TAG, "loadComicList:--------2------------"+c.data!!.count+" : "+c.data!!.total)
					Log.e(TAG, "loadComicList:--------3------------"+ c.data!!.results!![0].title)


						//originalList = c.data!!.results
						//countLimit = c.data.limit
				},
				{
					e -> callback.endCallProgress(e)
						Log.e(TAG, "loadComicList:e:--------------------------------", e)
				})
		)
	}

	companion object {
		val TAG: String = MainActivity::class.java.simpleName
	}
}
