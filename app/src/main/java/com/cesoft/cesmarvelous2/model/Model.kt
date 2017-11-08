package com.cesoft.cesmarvelous2.model


/**
 * Created by ccasanova on 08/11/2017
 */
object Model {
	data class Comic(val id: Long, val title: String, val description: String,
		val pageCount: Int, val thumbnail: Imagen, val images: List<Imagen>)

	data class Imagen(val path: String, val extension: String)

}
