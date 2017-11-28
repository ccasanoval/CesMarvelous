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
			loading.value = false
			loadComicList()
		})
	}

	//______________________________________________________________________________________________
	override fun onCleared() {
		fire.fin()
	}

	//TODO: eliminar dependencia entre repositorios !!!
	//////////////// FIRE STORE ///////////////////////////////////////////////////////////////////
	//______________________________________________________________________________________________
	fun loadComicList() {
		if(loading.value != false)return
		loading.value = true
		Log.e(TAG, "loadComicList: ------b--------------------------- ")

		//TODO: Repository

		if(isFireInit) {
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
		else {
			/// WEB SERVICE
			getComicFromWS()
		}
	}

	//______________________________________________________________________________________________
	private fun addComicsFB(newLista: List<Model.Comic>) {
		//if(lista.value != null)fire.deleteComics(lista.value!!)
		fire.addComics(newLista)
	}
	//______________________________________________________________________________________________
	fun getCleanComicFromWS() {
		Log.e(TAG, "getCleanComicFromWS:------------------------------------------- ")
		fire.deleteAllComics({
			getComicFromWS()
		})
	}

	//////////////// WEB SERVICE ///////////////////////////////////////////////////////////////////
	//______________________________________________________________________________________________
	fun getComicFromWS() {
		Log.e(TAG, "getComicFromWS:------------------------------------------- ")

		offset = 0
		val token = Token()
		service.getComics(MarvelWebService.ORDER_COMIC_TITLE, token.serial, token.public, token.hash, LIMIT, offset)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({ res: ComicDataResponse ->
				Log.e(TAG, "getComicFromWS:a-----------"+offset+"----------------- DATA: "+res.data?.count)
				Log.e(TAG, "getComicFromWS:c-----------"+res.data?.results?.size+"----------------- DATA: "+res.data?.count)
				if(res.data != null && res.data.count > 0 && res.data.cleanResults != null)
				{
					val datos = res.data.cleanResults!!
					var i = 1
					for(item in datos) item.index = i++

					Log.e(TAG, "getComicFromWS:z----------------------- DATA: "+res.data.results?.size+" / "+res.data.cleanResults?.size)
					lista.value = datos
					offset = res.data.count

					addComicsFB(datos)
					Log.e(TAG, "getComicFromWS:b-----------"+offset+"-----------------")
				}
				else
				{
					//offset = 0
					Log.e(TAG, "getComicFromWS:---------------------------- NO HAY DATOS")
				}
				loading.value = false
			},
			{
				e:Throwable ->
				//offset = 0
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

		//getComicFromWS()
		val token = Token()
		service.getComics(MarvelWebService.ORDER_COMIC_TITLE, token.serial, token.public, token.hash, LIMIT, offset)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({ res: ComicDataResponse ->
				Log.e(TAG, "loadMoreComics:a-----------"+offset+"----------------- DATA: "+res.data?.count)
				if(res.data != null && res.data.count > 0 && res.data.cleanResults != null)
				{
					val datos = res.data.cleanResults!!

					//TODO: llevar a getComicFromWS y borrar todo esto
					val union = ArrayList<Model.Comic>()
					if(lista.value != null)
					union.addAll(lista.value as List<Model.Comic>)
					union.addAll(datos)

					if(lista.value == null) {
						var i = 0
						for(item in union) item.index = i++
					}
					else {
						var i = lista.value!![lista.value!!.lastIndex].index +1
						for(item in datos) item.index = i++
					}

					lista.value = union
					offset = union.size
					fire.addComics(datos)
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