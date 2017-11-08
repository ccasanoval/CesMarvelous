package com.cesoft.cesmarvelous2.view

/**
 * Created by ccasanova on 08/11/2017
 */
interface ComicContract {
	interface View {
		fun endCallProgress(any: Any?)
	}
	interface Presenter {
		fun loadComicList()
		fun loadMoreComics(adapter: ComicAdapter)
	}
}