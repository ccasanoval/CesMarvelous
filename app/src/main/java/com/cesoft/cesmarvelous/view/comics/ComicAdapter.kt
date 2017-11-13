package com.cesoft.cesmarvelous.view.comics

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cesoft.cesmarvelous.R
import com.cesoft.cesmarvelous.databinding.ItemComicBinding
import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.ws.ComicDataResponse

/**
 * Created by ccasanova on 08/11/2017
 */
class ComicAdapter(var response: ComicDataResponse, private val presenter: ComicContract.Presenter)
	: RecyclerView.Adapter<ComicAdapter.ItemComicViewHolder>() {

	//______________________________________________________________________________________________
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemComicViewHolder {
		val itemBinding = DataBindingUtil.inflate<ItemComicBinding>(
				LayoutInflater.from(parent.context),
				R.layout.item_comic,
				parent,
				false)

		return ItemComicViewHolder(itemBinding, presenter)//TODO: dagger
	}

	//______________________________________________________________________________________________
	override fun onBindViewHolder(holder: ItemComicViewHolder, position: Int) {
		holder.bindItemComic(response.data!!.results!![position])
	}

	//______________________________________________________________________________________________
	override fun getItemCount() = response.data!!.results!!.size

	////////////////////////////////////////////////////////////////////////////////////////////////
	class ItemComicViewHolder(private val binding: ItemComicBinding, private val presenter: ComicContract.Presenter)
		: RecyclerView.ViewHolder(binding.cardView) {
		fun bindItemComic(comic: Model.Comic) {
			val viewmodel = ComicViewModel(comic)
			binding.cardView.setOnClickListener({ presenter.showDetalle(comic) })
			binding.viewmodel = viewmodel
		}
	}

}