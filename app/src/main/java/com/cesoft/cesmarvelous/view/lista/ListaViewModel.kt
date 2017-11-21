package com.cesoft.cesmarvelous.view.lista

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.util.Log
import com.cesoft.cesmarvelous.ws.ComicDataResponse
import com.cesoft.cesmarvelous.ws.MarvelWebService
import com.cesoft.cesmarvelous.ws.Token
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by ccasanova on 20/11/2017
 */
class ListaViewModel : ViewModel()
{
	val lista : MutableLiveData<List<Model.Comic>> = MutableLiveData()
	val mensajes : MutableLiveData<String> = MutableLiveData()

	private val defaultLimit = 25
	private var countLimit = 0

	private var service: MarvelWebService = MarvelWebService.create()


	//______________________________________________________________________________________________
	fun loadComicList() {
		//Log.e(TAG, "loadComicList:-------------------------------------------------------------")
		val token = Token()
		service.getComics(MarvelWebService.ORDER_COMIC_TITLE, token.serial, token.public, token.hash, defaultLimit)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
			{
				res: ComicDataResponse ->
				if(res.data != null)
				{
					lista.value = res.data.results
					countLimit = res.data.count
				}
				else
				{
					lista.value = null
					Log.e(TAG, "loadComicList:------------------ NO HAY DATOS")
				}
			},
			{
				e:Throwable ->
				lista.value = null
				Log.e(TAG, "loadComicList:e:-------------------------------------------"+e, e)
			})

	}

	//______________________________________________________________________________________________
	fun loadMoreComics(adapter: ComicAdapter)
	{
		val token = Token()
		service.getComics(MarvelWebService.ORDER_COMIC_TITLE, token.serial, token.public, token.hash, countLimit + defaultLimit)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
			{
				res ->
				if(res.data != null)
				{
					lista.value = res.data.results
					updateIndexesForRequests(adapter, lista.value!!)
				}
				else
				{
					lista.value = null
					Log.e(TAG, "loadMoreComics:-------------------- NO HAY DATOS")
				}
			},
			{
				e:Throwable ->
				lista.value = null
				Log.e(TAG, "loadMoreComics:e:------------------------------------------", e)
			})
	}
	//______________________________________________________________________________________________
	private fun updateIndexesForRequests(adapter: ComicAdapter, lista: List<Model.Comic>) {
		adapter.lista = lista
		adapter.notifyItemRangeChanged(countLimit, countLimit + defaultLimit)
		countLimit += defaultLimit
	}

	//______________________________________________________________________________________________
	companion object {
		val TAG: String = ListaViewModel::class.java.simpleName
	}
}