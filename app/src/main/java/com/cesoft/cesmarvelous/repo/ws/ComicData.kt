package com.cesoft.cesmarvelous.repo.ws

import com.cesoft.cesmarvelous.model.Model


/**
 * Created by ccasanova on 08/11/2017
 */
class ComicData {

	val count: Int = 0
	//val total: Int = 0
	val results: List<Model.Comic>? = null

	val cleanResults: List<Model.Comic>?
		get() {
			if(results == null)return null
			return results.distinctBy { it.id }
		}
}
