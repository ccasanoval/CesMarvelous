package com.cesoft.cesmarvelous2.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cesoft.cesmarvelous2.R
import com.cesoft.cesmarvelous2.databinding.ItemComicBinding
import com.cesoft.cesmarvelous2.model.Model
import com.cesoft.cesmarvelous2.ws.ComicDataResponse

/**
 * Created by ccasanova on 08/11/2017
 */
class ComicAdapter(var response: ComicDataResponse)
	: RecyclerView.Adapter<ComicAdapter.ItemComicViewHolder>() {

	//______________________________________________________________________________________________
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemComicViewHolder {
		val itemCharacterBinding = DataBindingUtil.inflate<ItemComicBinding>(
				LayoutInflater.from(parent.context),
				R.layout.item_comic,
				parent,
				false)

		return ItemComicViewHolder(itemCharacterBinding)
	}

	//______________________________________________________________________________________________
	override fun onBindViewHolder(holder: ItemComicViewHolder, position: Int) {
		holder.bindItemComic(response.data!!.results!![position])
	}

	//______________________________________________________________________________________________
	override fun getItemCount() = response.data!!.results!!.size

	////////////////////////////////////////////////////////////////////////////////////////////////
	class ItemComicViewHolder(private val binding: ItemComicBinding)
		: RecyclerView.ViewHolder(binding.cardView) {
		fun bindItemComic(character: Model.Comic) {
			val viewmodel = ComicViewModel.Item(itemView.context, character)
			//binding.cardView.setOnClickListener({ viewmodel.openDetailActivity() })
			binding.viewmodel = viewmodel
		}
	}

}