package com.cesoft.cesmarvelous.view.detalle

import android.arch.lifecycle.ViewModel
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.cesoft.cesmarvelous.model.Model


/**
 * Created by ccasanova on 20/11/2017
 */
class DetalleViewModel : ViewModel()
{
	lateinit var model: Model.Comic

	//______________________________________________________________________________________________
	fun load(intent: Intent)
	{
		val comicType = object : TypeToken<Model.Comic>(){}.type
		model = Gson().fromJson<Model.Comic>(intent.getStringExtra(PARAM_MODEL), comicType)
	}
	companion object {
		val PARAM_MODEL = "comic"
	}
}