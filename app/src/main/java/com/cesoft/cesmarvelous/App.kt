package com.cesoft.cesmarvelous

import android.app.Application
import com.cesoft.cesmarvelous.repo.db.DatabaseCreator


//TODO: Room, Cloud Firestore
//TODO: DI Kodein  -->  https://salomonbrys.github.io/Kodein/
//TODO: cambiar picaso por glide https://github.com/bumptech/glide
//TODO: Subir esto y CesDoom to diawi  https://www.diawi.com/

//Firestore Rules
//https://firebase.google.com/docs/firestore/security/get-started
//Proguard Firestore???
/*
# Add this global rule
-keepattributes Signature

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class com.yourcompany.models.** {
	*;
}*/
/**
 * Created by ccasanova on 21/11/2017
 */
class App : Application() {
	override fun onCreate() {
		super.onCreate()
		DatabaseCreator.createDb(this)
	}
}
