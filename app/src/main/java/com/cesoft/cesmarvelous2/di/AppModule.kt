package com.cesoft.cesmarvelous2.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

import com.cesoft.cesmarvelous2.App


/**
 * Created by ccasanova on 06/11/2017
 */
@Module
class AppModule(private val app: App) {

	@Provides
	@Singleton
	fun provideApplication(): App = app


	@Singleton
	@Provides
	fun provideContext(): Context = app.applicationContext

}