package com.cesoft.cesmarvelous.view.comics

import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.util.Log
import com.cesoft.cesmarvelous.ws.ComicDataResponse
import com.cesoft.cesmarvelous.ws.MarvelWebService
import com.cesoft.cesmarvelous.ws.MarvelWebService.Companion.ORDER_COMIC_TITLE
import com.cesoft.cesmarvelous.ws.Token
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by ccasanova on 08/11/2017
 */
class ComicPresenter(private var view: ComicContract.View) : ComicContract.Presenter {

	private var comicList: List<Model.Comic> ?= null
	private val defaultLimit = 25
	private var countLimit = 0

	private var service: MarvelWebService = MarvelWebService.create()
	private var _compoSub = CompositeSubscription()
	private val compoSub: CompositeSubscription
		get() {
			if(_compoSub.isUnsubscribed)
				_compoSub = CompositeSubscription()
			return _compoSub
		}


	//______________________________________________________________________________________________
	private fun manageSub(s: Subscription) = compoSub.add(s)
	override fun unsubscribe() { compoSub.unsubscribe() }

	//______________________________________________________________________________________________
	override fun loadComicList() {
		//Log.e(TAG, "loadComicList:-------------------------------------------------------------")
		val token = Token()
		manageSub(
			service.getComics(ORDER_COMIC_TITLE, token.serial, token.public, token.hash, defaultLimit)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
				{
					c: ComicDataResponse ->
						if(c.data != null) {
							comicList = c.data.results
							countLimit = c.data.count
							view.onSuccess(c)
						}
						else
							view.onError(Exception("No hay datos"))
				},
				{
					e ->
						view.onError(e)
						Log.e(TAG, "loadComicList:e:-------------------------------------------", e)
				})
		)
	}

	//______________________________________________________________________________________________
	override fun loadMoreComics(adapter: ComicAdapter)
	{
		//Log.e(TAG, "loadMoreComics:------------------------------------------------------")
		val token = Token()
		manageSub(
			service.getComics(ORDER_COMIC_TITLE, token.serial, token.public, token.hash, countLimit + defaultLimit)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
				{
					c ->
						view.onSuccess(c)
						updateIndexesForRequests(adapter, c)
					//Log.e(TAG, "loadMoreComics:-------------------"+countLimit+"-"+defaultLimit+"------------")
				},
				{
					e ->
						view.onError(e)
						Log.e(TAG, "loadMoreComics:e:------------------------------------------", e)
				})
		)
	}
	//______________________________________________________________________________________________
	private fun updateIndexesForRequests(adapter: ComicAdapter, response: ComicDataResponse) {
		adapter.response = response
		adapter.notifyItemRangeChanged(countLimit, countLimit + defaultLimit)
		countLimit += defaultLimit
	}

	//______________________________________________________________________________________________
	override fun showDetalle(comic: Model.Comic) {
		view.showDetalle(comic)
	}


	//______________________________________________________________________________________________
	companion object {
		val TAG: String = ComicPresenter::class.java.simpleName
	}
}