package com.cesoft.cesmarvelous2.view

import android.content.Context
import android.databinding.BindingAdapter
import android.widget.ImageView
import com.cesoft.cesmarvelous2.model.Model
import com.squareup.picasso.Picasso


/**
 * Created by ccasanova on 08/11/2017
 */
object ComicViewModel {

	////////////////////////////////////////////////////////////////////////////////////////////////
	class Lista {
		lateinit var originalList: MutableList<Model.Comic>
		val defaultLimit = 20
		var countLimit = 0
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	class Item(val context: Context, var model: Model.Comic) {

		companion object {
			val IMAGE_TYPE = "/landscape_incredible."
		}

		var imageUrl = modelImgUrl()

		fun modelImgUrl(): String = model.thumbnail.path + IMAGE_TYPE + model.thumbnail.extension

		object ImageViewBindingAdapter {
			@BindingAdapter("bind:imageUrl")
			@JvmStatic
			fun loadImage(view: ImageView, url: String) {
				Picasso.with(view.context).load(url).into(view)
			}
		}
	}
}
