package com.cesoft.cesmarvelous.view.lista

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.cesoft.cesmarvelous.R
import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.repo.fire.Fire
import com.cesoft.cesmarvelous.util.Log
import com.cesoft.cesmarvelous.repo.ws.ComicDataResponse
import com.cesoft.cesmarvelous.repo.ws.MarvelWebService
import com.cesoft.cesmarvelous.repo.ws.Token
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by ccasanova on 20/11/2017
 */
class ListaViewModel(app: Application) : AndroidViewModel(app)
{
	val lista: MutableLiveData<List<Model.Comic>> = MutableLiveData()
	val mensaje: MutableLiveData<String> = MutableLiveData()
	val loading: MutableLiveData<Boolean> = MutableLiveData()

	private val LIMIT = 10
	private var offset = 0

	private var service = MarvelWebService.create()
	private val fire = Fire()
	private var isFireInit = false

	init {
		//loading.value = false
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
		if(loading.value!!)return
		loading.value = true
		Log.e(TAG, "loadComicList: --------------------------------- ")

		//TODO: Repository

		/*if(isFireInit) {
			/// FIREBASE
			fire.getComics({ data, e ->
				if(!data.isEmpty()) {
					lista.value = data
					offset = data.size
				}
				else {
					Log.e(TAG, "loadComicList:Firebase:e:------------------------ ", e)
					/// WEB SERVICE
					getComicFromWS()
				}
				loading.value = false
			})
		}
		else {*/
			/// WEB SERVICE
			getComicFromWS()
		//}
	}
	//______________________________________________________________________________________________
	private fun getComicFromWS() {
		//loading.value = true
		Log.e(TAG, "getComicFromWS:------------------------------------------- ")

		offset = 0
		val token = Token()
		service.getComics(MarvelWebService.ORDER_COMIC_TITLE, token.serial, token.public, token.hash, LIMIT, offset)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
			{
				res: ComicDataResponse ->
				Log.e(TAG, "getComicFromWS:a-----------"+offset+"----------------- DATA: "+res.data?.count)
				Log.e(TAG, "getComicFromWS:c-----------"+res.data?.results?.size+"----------------- DATA: "+res.data?.count)

				if(res.data != null && res.data.count > 0)
				{
					lista.value = res.data.results
					offset = res.data.count
					fire.deleteComics()
					fire.addComics(res.data.results!!)
					Log.e(TAG, "getComicFromWS:b-----------"+offset+"-----------------")
				}
				else
				{
					offset = 0
					lista.value = null
					Log.e(TAG, "getComicFromWS:---------------------------- NO HAY DATOS")
				}
				loading.value = false
			},
			{
				e:Throwable ->
				offset = 0
				//lista.value = null
				loading.value = false
				Log.e(TAG, "getComicFromWS:e:-------------------------------------------"+e, e)
			})
	}

	//______________________________________________________________________________________________
	fun loadMoreComics() {
		if(loading.value!!) {
			Log.e(TAG, "loadMoreComics:--------BUT ALREADY LODING....-------------------------------")

			return
		}
		loading.value = true

		Log.e(TAG, "loadMoreComics:---------------------------------------")

		val token = Token()
		service.getComics(MarvelWebService.ORDER_COMIC_TITLE, token.serial, token.public, token.hash, LIMIT, offset)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
			{
				res ->
				Log.e(TAG, "loadMoreComics:a-----------"+offset+"----------------- DATA: "+res.data?.count)

				if(res.data != null && res.data.count > 0)
				{
					val union = ArrayList<Model.Comic>()
					if(lista.value != null)
					union.addAll(lista.value as List<Model.Comic>)
					union.addAll(res.data.results as List<Model.Comic>)
					lista.value = union
					//lista.value = res.data.results
					//updateIndexesForRequests(adapter, lista.value!!)
					//offset += res.data.count
					offset = union.size
					fire.addComics(res.data.results)
					Log.e(TAG, "loadMoreComics:b-----------"+offset+"-----------------"+res.data.count)
				}
				else
				{
					//lista.value = null
					Log.e(TAG, "loadMoreComics:-------------------- NO HAY DATOS")
				}
				loading.value = false
			},
			{
				e:Throwable ->
				//lista.value = null
				loading.value = false
				Log.e(TAG, "loadMoreComics:e:------------------------------------------", e)
			})
	}
	//______________________________________________________________________________________________
	/*private fun updateIndexesForRequests(adapter: ListaAdapter, lista: List<Model.Comic>) {
		adapter.lista = lista
		adapter.notifyItemRangeChanged(offset-LIMIT, LIMIT)
	}*/

	//______________________________________________________________________________________________
	companion object {
		val TAG: String = ListaViewModel::class.java.simpleName
	}
}