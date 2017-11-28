package com.cesoft.cesmarvelous.view.lista

import android.arch.lifecycle.MutableLiveData
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cesoft.cesmarvelous.R
import com.cesoft.cesmarvelous.databinding.ItemComicBinding
import com.cesoft.cesmarvelous.model.Model

/**
 * Created by ccasanova on 08/11/2017
 */
class ListaAdapter(private var lista: List<Model.Comic>)
	: RecyclerView.Adapter<ListaAdapter.ItemComicViewHolder>() {

	var comic = MutableLiveData<Model.Comic>()

	//______________________________________________________________________________________________
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemComicViewHolder {
		val itemBinding = DataBindingUtil.inflate<ItemComicBinding>(
				LayoutInflater.from(parent.context),
				R.layout.item_comic,
				parent,
				false)

		return ItemComicViewHolder(itemBinding, {comic -> this.comic.value = comic})
	}

	//______________________________________________________________________________________________
	override fun onBindViewHolder(holder: ItemComicViewHolder, position: Int) {
		holder.bindItemComic(lista[position])
	}

	//______________________________________________________________________________________________
	override fun getItemCount() = lista.size

	////////////////////////////////////////////////////////////////////////////////////////////////
	class ItemComicViewHolder(private val binding: ItemComicBinding, private val showDetalle: (Model.Comic) -> Unit)
		: RecyclerView.ViewHolder(binding.cardView) {

		fun bindItemComic(comic: Model.Comic) {
			binding.binder = ListaBinder(comic)
			binding.cardView.setOnClickListener({
				showDetalle(comic)
			})
		}
	}

}