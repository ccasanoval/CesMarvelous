package com.cesoft.cesmarvelous.view.lista

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.cesoft.cesmarvelous.R
import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.repo.fire.Fire
import com.cesoft.cesmarvelous.util.Log
import com.cesoft.cesmarvelous.repo.ws.ComicDataResponse
import com.cesoft.cesmarvelous.repo.ws.MarvelWebService
import com.cesoft.cesmarvelous.repo.ws.Token
import com.google.android.gms.tasks.OnCompleteListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.google.gson.JsonElement
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_comic.view.*


/**
 * Created by ccasanova on 20/11/2017
 */
class ListaViewModel(app: Application) : AndroidViewModel(app)
{
	val lista : MutableLiveData<List<Model.Comic>> = MutableLiveData()
	val mensaje : MutableLiveData<String> = MutableLiveData()

	private val defaultLimit = 25
	private var countLimit = 0

	private var service = MarvelWebService.create()
	private val fire = Fire()
	private var isFireInit = false

	init {
		fire.ini({ usr, e ->
			isFireInit = usr != null
			if(e != null) {
				Log.e(TAG, "INIT: Firebase:e:----------------------------------------", e)
				mensaje.value = getApplication<Application>().getString(R.string.firebase_connection_error)
			}
			loadComicList()
		})
	}

	//______________________________________________________________________________________________
	override fun onCleared() {
		fire.fin()
	}

	//______________________________________________________________________________________________
	fun loadComicList() {
		Log.e(TAG, "loadComicList: --------------------------------- ")

		//TODO: Repository

		if(isFireInit) {
			/// FIREBASE
			fire.getComics({ data, e ->
				if(!data.isEmpty()) {
					lista.value = data
					countLimit = data.size
				}
				else {
					Log.e(TAG, "loadComicList:Firebase:e:------------------------ ", e)
					/// WEB SERVICE
					getComicFromWS()
				}
			})
		}
		else {
			/// WEB SERVICE
			getComicFromWS()
		}
	}
	//______________________________________________________________________________________________
	private fun getComicFromWS() {
		Log.e(TAG, "getComicFromWS:------------------------------------------- ")

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
					if(res.data.count > 0) {
						Log.e(TAG, "getComicFromWS:--------------------------- POR QUE DICE Q ESTO NO VA?")
						fire.addComics(res.data.results!!)
					}
				}
				else
				{
					lista.value = null
					Log.e(TAG, "getComicFromWS:---------------------------- NO HAY DATOS")
				}
			},
			{
				e:Throwable ->
				lista.value = null
				Log.e(TAG, "getComicFromWS:e:-------------------------------------------"+e, e)
			})
	}

	//______________________________________________________________________________________________
	fun loadMoreComics(adapter: ListaAdapter)
	{
		Log.e(TAG, "loadMoreComics:---------------------------------------")

		val token = Token()
		service.getComics(MarvelWebService.ORDER_COMIC_TITLE, token.serial, token.public, token.hash, countLimit + defaultLimit)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
			{
				res ->
				if(res.data != null && res.data.count > 0)
				{
					lista.value = res.data.results
					updateIndexesForRequests(adapter, lista.value!!)
				}
				else
				{
					//lista.value = null
					Log.e(TAG, "loadMoreComics:-------------------- NO HAY DATOS")
				}
			},
			{
				e:Throwable ->
				//lista.value = null
				Log.e(TAG, "loadMoreComics:e:------------------------------------------", e)
			})
	}
	//______________________________________________________________________________________________
	private fun updateIndexesForRequests(adapter: ListaAdapter, lista: List<Model.Comic>) {
		adapter.lista = lista
		adapter.notifyItemRangeChanged(countLimit, countLimit + defaultLimit)
		countLimit += defaultLimit
	}

	//______________________________________________________________________________________________
	companion object {
		val TAG: String = ListaViewModel::class.java.simpleName
	}
}