package com.cesoft.cesmarvelous2.view.comic

import android.content.Context
import android.content.Intent
import android.databinding.BindingAdapter
import android.os.Build
import android.text.Html
import android.widget.ImageView
import com.cesoft.cesmarvelous2.model.Model
import com.squareup.picasso.Picasso


/**
 * Created by ccasanova on 13/11/2017
 */
class DetalleViewModel(var model: Model.Comic) {

	companion object {
		//https://developer.marvel.com/documentation/images
		val IMAGE_TYPE = "/standard_fantastic."//amazing
	}

	var imgUrl = imgUrl()
	fun imgUrl(): String = model.thumbnail.path + IMAGE_TYPE + model.thumbnail.extension
	object ImageViewBindingAdapter {
		@BindingAdapter("bind:imgUrl")
		@JvmStatic
		fun loadImage(view: ImageView, url: String) {
			Picasso.with(view.context).load(url).into(view)
		}
	}

	var description = getDescripcion()

	private fun getDescripcion() : String
	{
		if(model == null || model.description == null)return ""
		return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
			Html.fromHtml(model.description, Html.FROM_HTML_MODE_COMPACT).toString()
		else
			Html.fromHtml(model.description).toString()
	}
}