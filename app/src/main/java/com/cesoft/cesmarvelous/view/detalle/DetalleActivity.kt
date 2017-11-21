package com.cesoft.cesmarvelous.view.detalle

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.cesoft.cesmarvelous.R
import com.cesoft.cesmarvelous.databinding.ActDetalleBinding
import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * Created by ccasanova on 13/11/2017
 */
class DetalleActivity : AppCompatActivity() {

	lateinit var viewModel : DetalleViewModel
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

		viewModel = ViewModelProviders.of(this).get(DetalleViewModel::class.java)
		viewModel.load(intent)

		Log.e(TAG, "DATA:------------------------"+viewModel.model.title+" \n"+viewModel.model.description)
		binding.binder = DetalleBinder(viewModel.model)
	}

	//______________________________________________________________________________________________
	companion object {
		val TAG = DetalleActivity::class.java.simpleName
	}
}
