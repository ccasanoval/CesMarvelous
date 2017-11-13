package com.cesoft.cesmarvelous.view.comics

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.cesoft.cesmarvelous.model.Model
import com.squareup.picasso.Picasso


/**
 * Created by ccasanova on 08/11/2017
 */
class ComicViewModel(var model: Model.Comic) {

	companion object {
		//https://developer.marvel.com/documentation/images
		val IMAGE_TYPE =
				"/landscape_amazing."
				//"/landscape_incredible."
	}
	var imgUrl: String = model.thumbnail.path + IMAGE_TYPE + model.thumbnail.extension

	object ImageViewBindingAdapter {
		@BindingAdapter("bind:imgUrl")
		@JvmStatic
		fun loadImage(view: ImageView, url: String) {
			Picasso.with(view.context).load(url).into(view)
		}
	}
}
