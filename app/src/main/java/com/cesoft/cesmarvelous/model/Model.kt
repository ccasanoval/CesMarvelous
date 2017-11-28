package com.cesoft.cesmarvelous.model

/**
 * Created by ccasanova on 08/11/2017
 */
object Model {
	data class Comic(val id: Long, val title: String, val description: String,
		val pageCount: Long, var thumbnail: Imagen,
		val issueNumber: Double, val isbn: String) {
		var index: Int = 0
		var idFire: String = ""//TODO:fuera de aqui!
	}

	data class Imagen(val path: String, val extension: String)
}
