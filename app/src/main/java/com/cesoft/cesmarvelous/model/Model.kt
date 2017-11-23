package com.cesoft.cesmarvelous.model

import com.google.firebase.firestore.DocumentReference


/**
 * Created by ccasanova on 08/11/2017
 */
object Model {
	data class Comic(val id: Long, val title: String, val description: String,
		val pageCount: Long, var thumbnail: Imagen,
		// val images: List<Imagen>, val variantDescription: String,
		val issueNumber: Double, val isbn: String) {
		//override fun toString() = ""
		//var thumbnail: DocumentReference
		//var thumbnail: Imagen = Imagen("","")
		//constructor():this(0, "", "", 0, 0.0, "")
	}

	data class Imagen(val path: String, val extension: String)
}
