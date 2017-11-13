package com.cesoft.cesmarvelous2.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by ccasanova on 08/11/2017
 */
class InfiniteScrollListener(private val funcion:(Int) -> Unit, private val layoutManager: LinearLayoutManager)
	: RecyclerView.OnScrollListener() {

	private var previousTotal = 0
	private var loading = true
	private var visibleThreshold = 1
	private var firstVisibleItem = 0
	private var visibleItemCount = 0
	private var totalItemCount = 0

	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		super.onScrolled(recyclerView, dx, dy)
//Log.e(TAG, "onScrolled--"+loading+"-----------"+dy+"----------"+previousTotal+"-----------------------------"+totalItemCount+" - "+visibleItemCount+" <= "+firstVisibleItem+" + "+visibleThreshold+"---------")
		if (dy > 0) {
			visibleItemCount = recyclerView.childCount
			totalItemCount = layoutManager.itemCount
			firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

			if(loading) {
				if(totalItemCount > previousTotal) {
					//Log.e(TAG, "onScrolled----a-------"+loading+"----------------------------")
					loading = false
					previousTotal = totalItemCount
				}
			}
			if(!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				//Log.e(TAG, "onScrolled-----b------"+loading+"----------------------------")
				funcion(layoutManager.findFirstVisibleItemPosition())
				loading = true
			}
		}
	}

	companion object {
		//private val TAG: String = InfiniteScrollListener::class.java.simpleName
	}
}