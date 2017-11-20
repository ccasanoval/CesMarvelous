package com.cesoft.cesmarvelous.view.lista

import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.ws.ComicDataResponse

/**
 * Created by ccasanova on 08/11/2017
 */
interface ComicContract {
	interface View {
		fun onSuccess(comicData: ComicDataResponse)
		fun onError(error: Throwable)
		fun showDetalle(comic: Model.Comic)
		//fun goToItemList(index: Int)
	}
	interface Presenter {
		fun loadComicList()
		fun loadMoreComics(adapter: ComicAdapter)
		fun showDetalle(comic: Model.Comic)
		fun unsubscribe()
	}
}