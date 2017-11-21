package com.cesoft.cesmarvelous.view.lista

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager

import com.cesoft.cesmarvelous.R
import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.util.InfiniteScrollListener
import com.cesoft.cesmarvelous.util.Log
import com.cesoft.cesmarvelous.view.detalle.DetalleActivity
import com.cesoft.cesmarvelous.view.detalle.DetalleViewModel.Companion.PARAM_MODEL
import com.google.gson.Gson
import kotlinx.android.synthetic.main.act_main.*


/**
 * Created by ccasanova on 08/11/2017
 */
class MainActivity : AppCompatActivity() {

	private lateinit var viewModel : ListaViewModel
	private val layoutManager = LinearLayoutManager(this)
	private var lastVisibleItem = 0

	//______________________________________________________________________________________________
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.act_main)

		viewModel = ViewModelProviders.of(this).get(ListaViewModel::class.java)
		viewModel.lista.observe(this, Observer<List<Model.Comic>>
		{
			lista ->
			swiperefresh.isRefreshing = false
			if(lista != null) {
				Log.e(TAG, "LISTA OBSERV:---------------"+lista.size)
				val adapter = ComicAdapter(lista)
				adapter.comic.observe(this, Observer<Model.Comic>{comic -> showDetalle(comic!!)})
				recyclerView.adapter = adapter
				goToItemList(lastVisibleItem)
				Snackbar.make(recyclerView, R.string.ok_net, Snackbar.LENGTH_LONG).show()
			}
			else {
				Log.e(TAG, "LISTA OBSERV:---------------LISTA = NULL")
				Snackbar.make(recyclerView, R.string.err_net, Snackbar.LENGTH_LONG).show()
			}
		})
		viewModel.mensajes.observe(this, Observer<String>
		{
			mensaje ->
			Snackbar.make(recyclerView, mensaje!!, Snackbar.LENGTH_LONG).show()
		})


		val scrollListener = InfiniteScrollListener({
			index ->
				lastVisibleItem = index
				viewModel.loadMoreComics(recyclerView.adapter as ComicAdapter)
				swiperefresh.isRefreshing = true
			}, layoutManager)
		recyclerView.layoutManager = layoutManager
		recyclerView.addOnScrollListener(scrollListener)

		swiperefresh.setOnRefreshListener({
			lastVisibleItem = 0
			scrollListener.reset()
			viewModel.loadComicList()
		})

		viewModel.loadComicList()
		swiperefresh.isRefreshing = true
	}
	//______________________________________________________________________________________________
	private fun goToItemList(index: Int)
	{
		layoutManager.scrollToPositionWithOffset(index, 0)
	}

	//______________________________________________________________________________________________
	fun showDetalle(comic: Model.Comic) {
		val intent = Intent(this, DetalleActivity::class.java)
		val json = Gson().toJson(comic)
		intent.putExtra(PARAM_MODEL, json)
		startActivity(intent)
	}

	//______________________________________________________________________________________________
	companion object {
		val TAG: String = MainActivity::class.java.simpleName
	}
}
