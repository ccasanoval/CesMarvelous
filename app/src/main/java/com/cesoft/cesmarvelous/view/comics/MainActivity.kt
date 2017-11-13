package com.cesoft.cesmarvelous.view.comics

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager

import com.cesoft.cesmarvelous.R
import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.util.InfiniteScrollListener
import com.cesoft.cesmarvelous.util.Log
import com.cesoft.cesmarvelous.view.comic.DetalleActivity
import com.cesoft.cesmarvelous.ws.ComicDataResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.act_main.*


/**
 * Created by ccasanova on 08/11/2017
 */
class MainActivity : AppCompatActivity(), ComicContract.View {

	private lateinit var presenter: ComicContract.Presenter
	private val layoutManager = LinearLayoutManager(this)
	private var lastVisibleItem = 0

	//______________________________________________________________________________________________
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.act_main)
		presenter = ComicPresenter(this)

		val scrollListener = InfiniteScrollListener({
					index ->
					Log.e(TAG, "REFRESH:----- "+index+" -----------------------------")
					lastVisibleItem = index
					presenter.loadMoreComics(recyclerView.adapter as ComicAdapter)
					swiperefresh.isRefreshing = true
				},
				layoutManager)
		recyclerView.layoutManager = layoutManager
		recyclerView.addOnScrollListener(scrollListener)

		swiperefresh.setOnRefreshListener({
			Log.e(TAG, "REFRESH:-----0-----------------------------")
			lastVisibleItem = 0
			scrollListener.reset()
			presenter.loadComicList()
		})

		/// Cargar lista comics
		presenter.loadComicList()
		swiperefresh.isRefreshing = true
	}
	//______________________________________________________________________________________________
	private fun goToItemList(index: Int)
	{
		layoutManager.scrollToPositionWithOffset(index, 0)
	}
	//______________________________________________________________________________________________
	override fun onDestroy() {
		presenter.unsubscribe()
		super.onDestroy()
	}

	//______________________________________________________________________________________________
	override fun onSuccess(comicData: ComicDataResponse) {
		swiperefresh.isRefreshing = false
		recyclerView.adapter = ComicAdapter(comicData, presenter)
		goToItemList(lastVisibleItem)
		Snackbar.make(recyclerView, R.string.ok_net, Snackbar.LENGTH_LONG).show()
	}
	//______________________________________________________________________________________________
	override fun onError(error: Throwable) {
		swiperefresh.isRefreshing = false
		Snackbar.make(recyclerView, R.string.err_net, Snackbar.LENGTH_LONG).show()
		Log.e(TAG, "onError:e:-----------------------------------------------------------------",error)
	}

	//______________________________________________________________________________________________
	override fun showDetalle(comic: Model.Comic) {
		val intent = Intent(this, DetalleActivity::class.java)
		val json = Gson().toJson(comic)
		intent.putExtra(DetalleActivity.PARAM_MODEL, json)
		startActivity(intent)
	}

	//______________________________________________________________________________________________
	companion object {
		val TAG: String = MainActivity::class.java.simpleName
	}
}
