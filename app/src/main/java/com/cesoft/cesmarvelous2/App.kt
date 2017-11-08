package com.cesoft.cesmarvelous2

import android.app.Application
import com.cesoft.cesmarvelous2.di.AppComponent
import com.cesoft.cesmarvelous2.di.AppModule

//https://developer.marvel.com/docs
//Your public key
//b82400e9f7f5f39555bbf96f1b1bedf9
//Your private key
//60df808b06d0371ef6c63d41f1dfa1ed730a7ac6

/**
 * Created by ccasanova on 08/11/2017
 */
class App : Application() {
	override fun onCreate() {
		super.onCreate()

		/// DAGGER
		appComponent = DaggerAppComponent.builder()
			.appModule(AppModule(this))
			.build()
	}


	companion object {
		lateinit var appComponent: AppComponent
	}
}