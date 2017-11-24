package com.cesoft.cesmarvelous.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by ccasanova on 08/11/2017
 */
class InfiniteScrollListener(private val callback:(Int) -> Unit, private val layoutManager: LinearLayoutManager)
	: RecyclerView.OnScrollListener() {

	private var previousTotal = 0
	private var loading = true
	private val visibleThreshold = 1
	private var firstVisibleItem = 0
	private var visibleItemCount = 0
	private var totalItemCount = 0

	//______________________________________________________________________________________________
	fun reset() {
		previousTotal = 0
		firstVisibleItem = 0
		visibleItemCount = 0
		totalItemCount = 0
	}

	//______________________________________________________________________________________________
	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		super.onScrolled(recyclerView, dx, dy)
		//Log.e(TAG, "onScrolled------dx, "+dy+"---------- "+loading+" ------------------ "+totalItemCount +" :: "+ previousTotal)
		if(dy > 0) {
			visibleItemCount = recyclerView.childCount
			totalItemCount = layoutManager.itemCount
			firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

			if(loading) {
				if(totalItemCount >= previousTotal) {
					loading = false
					previousTotal = totalItemCount
				}
			}
			if(!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				callback(firstVisibleItem)
				loading = true
			}
		}
	}

	//______________________________________________________________________________________________
	companion object {
		private val TAG: String = InfiniteScrollListener::class.java.simpleName
	}
}