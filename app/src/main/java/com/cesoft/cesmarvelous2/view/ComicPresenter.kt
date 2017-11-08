package com.cesoft.cesmarvelous2.view

import com.cesoft.cesmarvelous2.BuildConfig
import com.cesoft.cesmarvelous2.model.Model
import com.cesoft.cesmarvelous2.util.Log
import com.cesoft.cesmarvelous2.util.Util
import com.cesoft.cesmarvelous2.ws.ComicDataResponse
import com.cesoft.cesmarvelous2.ws.MarvelWebService
import com.cesoft.cesmarvelous2.ws.MarvelWebService.Companion.ORDER_COMIC_TITLE
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by ccasanova on 08/11/2017
 */
class ComicPresenter(private var view: ComicContract.View) : ComicContract.Presenter {

	private var service: MarvelWebService = MarvelWebService.create()
	private var _compoSub = CompositeSubscription()
	private val compoSub: CompositeSubscription
		get() {
			if(_compoSub.isUnsubscribed)
				_compoSub = CompositeSubscription()
			return _compoSub
		}

	var originalList: List<Model.Comic> ?= null
	val defaultLimit = 20
	var countLimit = 0

	//______________________________________________________________________________________________
	private fun manageSub(s: Subscription) = compoSub.add(s)
	fun unsubscribe() { compoSub.unsubscribe() }

	//______________________________________________________________________________________________
	override fun loadComicList() {
		Log.e(TAG, "loadComicList:---------------------------------")

		val timestamp = java.util.Date().time
		val hash = Util.md5(timestamp.toString() + BuildConfig.PRIVATE_API_KEY + BuildConfig.PUBLIC_API_KEY)

		manageSub(
			service.getComics(ORDER_COMIC_TITLE, timestamp.toString(), BuildConfig.PUBLIC_API_KEY, hash, defaultLimit)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
				{
					c: ComicDataResponse ->
						view.endCallProgress(c)
						Log.e(TAG, "loadComicList:--------1------------"+c.code+" : "+c.status)
						Log.e(TAG, "loadComicList:--------2------------"+c.data!!.count+" : "+c.data!!.total)
						Log.e(TAG, "loadComicList:--------3------------"+ c.data!!.results!![0].title)

						originalList = c.data.results
						countLimit = c.data.count
				},
				{
					e ->
						view.endCallProgress(e)
						Log.e(TAG, "loadComicList:e:--------------------------------", e)
				})
		)
	}

	override fun loadMoreComics(adapter: ComicAdapter)
	{
		val timestamp = java.util.Date().time
		val hash = Util.md5(timestamp.toString()+BuildConfig.PRIVATE_API_KEY+BuildConfig.PUBLIC_API_KEY)

		manageSub(
				service.getComics(ORDER_COMIC_TITLE, timestamp.toString(), BuildConfig.PUBLIC_API_KEY, hash, countLimit + defaultLimit)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(
					{
						c ->
							view.endCallProgress(c)
							updateIndexesForRequests(adapter, c)
					},
					{
						e ->
							view.endCallProgress(e)
							Log.e(TAG, "loadComicList:e:--------------------------------", e)
					})
		)
	}
	fun updateIndexesForRequests(adapter: ComicAdapter, response: ComicDataResponse) {
		adapter.response = response
		adapter.notifyItemRangeChanged(countLimit, countLimit + defaultLimit)
		//originalList = response.data.results
		countLimit += defaultLimit
	}


	//______________________________________________________________________________________________
	companion object {
		val TAG: String = ComicPresenter::class.java.simpleName
	}
}