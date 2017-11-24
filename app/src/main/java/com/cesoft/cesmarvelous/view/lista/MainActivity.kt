package com.cesoft.cesmarvelous.view.lista

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View

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
	private lateinit var vacioView: View
	//private lateinit var listaView: View

	//______________________________________________________________________________________________
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.act_main)
		vacioView = findViewById(R.id.imgVacio)

		viewModel = ViewModelProviders.of(this).get(ListaViewModel::class.java)
		//--- LISTA DE COMICS
		viewModel.lista.observe(this, Observer<List<Model.Comic>>
		{
			lista ->
			//swiperefresh.isRefreshing = false
			if(lista != null && lista.size > 0) {
				Log.e(TAG, "LISTA OBSERV:---------------"+lista.size+" ::::::::: "+lastVisibleItem)
				val adapter = ListaAdapter(lista)
				adapter.comic.observe(this, Observer<Model.Comic>{comic -> showDetalle(comic!!)})
				recyclerView.adapter = adapter
				goToItemList(lastVisibleItem)
				Snackbar.make(recyclerView, R.string.ok_net, Snackbar.LENGTH_LONG).show()

				//listaView.visibility = View.VISIBLE
				vacioView.visibility = View.GONE
			}
			else {
				Log.e(TAG, "LISTA OBSERV:---------------LISTA = NULL : "+lista?.isEmpty())
				if(recyclerView.adapter.itemCount < 1) {
					//listaView.visibility = View.GONE
					vacioView.visibility = View.VISIBLE
				}
				Snackbar.make(recyclerView, R.string.err_net, Snackbar.LENGTH_LONG).show()
			}
		})
		//---- MENSAJES
		viewModel.mensaje.observe(this, Observer<String>
		{
			mensaje ->
			Snackbar.make(recyclerView, mensaje!!, Snackbar.LENGTH_LONG).show()
		})
		//---- CARGANDO
		viewModel.loading.observe(this, Observer<Boolean>
		{
			isLoading ->
			Log.e(TAG, "isLoading:-----------------------"+isLoading)
			swiperefresh.isRefreshing = isLoading!!
		})

		//---------
		val scrollListener = InfiniteScrollListener({
			index ->
			Log.e(TAG, "InfiniteScrollListener:-----------------------"+index)
				lastVisibleItem = index
				viewModel.loadMoreComics()
			}, layoutManager)
		recyclerView.layoutManager = layoutManager
		recyclerView.addOnScrollListener(scrollListener)

		swiperefresh.setOnRefreshListener({
			lastVisibleItem = 0
			scrollListener.reset()
			viewModel.loadComicList()
		})

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
