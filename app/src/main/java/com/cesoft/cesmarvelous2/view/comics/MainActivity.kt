package com.cesoft.cesmarvelous2.view.comics

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager

import com.cesoft.cesmarvelous2.R
import com.cesoft.cesmarvelous2.model.Model
import com.cesoft.cesmarvelous2.util.InfiniteScrollListener
import com.cesoft.cesmarvelous2.util.Log
import com.cesoft.cesmarvelous2.view.comic.DetalleActivity
import com.cesoft.cesmarvelous2.ws.ComicDataResponse
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

		//val binding: ActMainBinding = DataBindingUtil.setContentView(this, R.layout.act_main)
		//comicViewModel = ComicViewModel.Lista()
		//binding.viewmodel = comicViewModel

		//setSupportActionBar(binding.toolbar)

		//linearLayout = LinearLayoutManager(this)
		recyclerView.layoutManager = layoutManager
		recyclerView.addOnScrollListener(
			InfiniteScrollListener(
			{
				index ->
				lastVisibleItem = index
				presenter.loadMoreComics(recyclerView.adapter as ComicAdapter)
			},
			layoutManager))

//		swiperefresh.setOnRefreshListener({
//			Log.e(TAG, "REFRESH:----------------------------------")
//			presenter.loadComicList()
//		})

		presenter.loadComicList()
	}
	//______________________________________________________________________________________________
	private fun goToItemList(index: Int)
	{
		layoutManager.scrollToPositionWithOffset(index, 0)
	}

	//______________________________________________________________________________________________
	override fun onStart() {
		super.onStart()
		//presenter.loadComicList()
		Log.e(TAG, "----------------- START ------------------------")

	}
	//______________________________________________________________________________________________
	override fun onDestroy() {
		presenter.unsubscribe()
		super.onDestroy()
		Log.e(TAG, "----------------- DESTROY ------------------------")
	}

	//______________________________________________________________________________________________
	override fun onSuccess(comicData: ComicDataResponse) {
		//swiperefresh.isRefreshing = false
		Log.e(TAG, "onSuccess:---------------------------------"+comicData+"-----"+lastVisibleItem)
		recyclerView.adapter = ComicAdapter(comicData, presenter)
		//goToItemList(if(lastVisibleItem > 0)lastVisibleItem+2 else lastVisibleItem)
		goToItemList(lastVisibleItem)
	}
	//______________________________________________________________________________________________
	override fun onError(error: Throwable) {
		//swiperefresh.isRefreshing = false
		Log.e(TAG, "onError:---------------------------------"+error)
		Snackbar.make(recyclerView, "Error al cargar los datos desde el servidor", Snackbar.LENGTH_LONG).show()
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
