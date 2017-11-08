package com.cesoft.cesmarvelous2

import android.app.Application
import com.cesoft.cesmarvelous2.di.AppComponent
import com.cesoft.cesmarvelous2.di.AppModule
import com.cesoft.cesmarvelous2.di.DaggerAppComponent

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