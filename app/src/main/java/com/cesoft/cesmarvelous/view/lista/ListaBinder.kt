package com.cesoft.cesmarvelous.view.lista

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.util.Log
import com.squareup.picasso.Picasso


/**
 * Created by ccasanova on 08/11/2017
 */
class ListaBinder(var model: Model.Comic) {

	companion object {
		val TAG: String = ListaBinder::class.java.simpleName
		//https://developer.marvel.com/documentation/images
		val IMAGE_TYPE =
				"/landscape_amazing."
				//"/landscape_incredible."
	}
	var imgUrl: String = if(model != null && model.thumbnail != null) model.thumbnail.path + IMAGE_TYPE + model.thumbnail.extension
						else ""

	object ImageViewBindingAdapter {
		@BindingAdapter("bind:imgUrl")
		@JvmStatic
		fun loadImage(view: ImageView, path: String?) {
			//Log.e(TAG, "ImageViewBindingAdapter-----------------------"+path)
			if(path != null && path.trim().isNotEmpty())
				Picasso.with(view.context).load(path).into(view)
		}
	}
}
