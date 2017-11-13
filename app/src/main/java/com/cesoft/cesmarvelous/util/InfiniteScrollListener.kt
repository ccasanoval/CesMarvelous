package com.cesoft.cesmarvelous.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by ccasanova on 08/11/2017
 */
class InfiniteScrollListener(private val funcion:(Int) -> Unit, private val layoutManager: LinearLayoutManager)
	: RecyclerView.OnScrollListener() {

	private var previousTotal = 0
	private var loading = true
	private val visibleThreshold = 1
	private var firstVisibleItem = 0
	private var visibleItemCount = 0
	private var totalItemCount = 0

	fun reset() {
		previousTotal = 0
		firstVisibleItem = 0
		visibleItemCount = 0
		totalItemCount = 0
	}

	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		super.onScrolled(recyclerView, dx, dy)
		if (dy > 0) {
			visibleItemCount = recyclerView.childCount
			totalItemCount = layoutManager.itemCount
			firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

			if(loading) {
				if(totalItemCount > previousTotal) {
					loading = false
					previousTotal = totalItemCount
				}
			}
			if(!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				funcion(firstVisibleItem)
				loading = true
			}
		}
	}

	companion object {
		//private val TAG: String = InfiniteScrollListener::class.java.simpleName
	}
}