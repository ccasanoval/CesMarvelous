package com.cesoft.cesmarvelous.view.detalle

import android.os.Build
import android.text.Html
import com.cesoft.cesmarvelous.model.Model


/**
 * Created by ccasanova on 13/11/2017
 */
class DetalleBinder(var model: Model.Comic) {

	companion object {
		//https://developer.marvel.com/documentation/images
		private val IMAGE_TYPE = "/standard_fantastic."//amazing
	}

	//______________________________________________________________________________________________
	//var imgUrl = imgUrl()
	fun imgUrl(): String = model.thumbnail.path + IMAGE_TYPE + model.thumbnail.extension
	/*object ImageViewBindingAdapter {
		@BindingAdapter("bind:imgUrl")
		@JvmStatic
		fun loadImage(view: ImageView, url: String) {
			Picasso.with(view.context).load(url).into(view)
		}
	}*/

	//______________________________________________________________________________________________
	var title = model.title
	//______________________________________________________________________________________________
	var description = getDescripcion()
	private fun getDescripcion() : String
	{
		if(model.description == null)return ""
		return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
			Html.fromHtml(model.description, Html.FROM_HTML_MODE_COMPACT).toString()
		else
			Html.fromHtml(model.description).toString()
	}
	//______________________________________________________________________________________________
	var issueNumber = model.issueNumber.toString()
	var pageCount = model.pageCount.toString()
	var id = model.id.toString()
	var index = model.index.toString()
}