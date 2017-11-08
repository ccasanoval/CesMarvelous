package com.cesoft.cesmarvelous2.di

import dagger.Component
import javax.inject.Singleton

/**
 * Created by ccasanova on 06/11/2017
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {


}
