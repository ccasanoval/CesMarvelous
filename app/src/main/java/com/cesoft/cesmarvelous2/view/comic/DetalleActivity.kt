package com.cesoft.cesmarvelous2.view.comic

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.cesoft.cesmarvelous2.R
import com.cesoft.cesmarvelous2.databinding.ActDetalleBinding
import com.cesoft.cesmarvelous2.model.Model
import com.cesoft.cesmarvelous2.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * Created by ccasanova on 13/11/2017
 */
class DetalleActivity : AppCompatActivity() {

	//______________________________________________________________________________________________
	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if(item != null && item.itemId == android.R.id.home)
			finish()
		return true
	}
	//______________________________________________________________________________________________
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//setContentView(R.layout.act_detalle)

		val binding: ActDetalleBinding = DataBindingUtil.setContentView(this, R.layout.act_detalle)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		val comicType = object : TypeToken<Model.Comic>(){}.type
		val comic = Gson().fromJson<Model.Comic>(intent.getStringExtra(DetalleActivity.PARAM_MODEL), comicType)

		Log.e(TAG, "DATA:------------------------"+comic.title+" \n"+comic.description)
		binding.viewmodel = DetalleViewModel(comic)
	}

	//______________________________________________________________________________________________
	companion object {
		val TAG = DetalleActivity::class.java.simpleName
		val PARAM_MODEL = "comic"
	}
}
