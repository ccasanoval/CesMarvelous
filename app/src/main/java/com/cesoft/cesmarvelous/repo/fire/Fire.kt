package com.cesoft.cesmarvelous.repo.fire

import com.google.firebase.firestore.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson

import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.util.Log


/**
 * Created by ccasanova on 21/11/2017
 */
class Fire {

	private val auth = FirebaseAuth.getInstance()
	private val db = FirebaseFirestore.getInstance()
	private lateinit var authListener: FirebaseAuth.AuthStateListener
	private var isInit: Boolean = false
	private var gson = Gson()

	//______________________________________________________________________________________________
	//init {
	fun ini(callback: (FirebaseUser?, Throwable?) -> Unit) {
		if(isInit)return

		/*authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
			val user = firebaseAuth.currentUser
			if(user != null)
			{
				callback(user, null)
				Log.e(TAG, "onAuthStateChanged:signed_in:" + user.uid)
			}
			else
				Log.e(TAG, "onAuthStateChanged:signed_out")
		}
		auth.addAuthStateListener(authListener)*/

		//---------------
		/*auth.signInWithEmailAndPassword(FIRE_EMAIL, FIRE_PASS).addOnCompleteListener({ task ->
				if(task.isSuccessful)
					Log.e(TAG, "signInWithEmailAndPassword: OK")
				else
					Log.e(TAG, "signInWithEmailAndPassword: KK")
			})*/

		//----------------
		try {
			auth.signInAnonymously()
				.addOnCompleteListener({ task ->
					if(task.isSuccessful) {
						Log.e(TAG, "signInAnonymously:success-----------------------")
						val user = auth.currentUser
						isInit = true
						callback(user, null)
					}
					else {
						Log.e(TAG, "signInAnonymously:failure------------------------", task.exception)
						callback(null, task.exception)
					}
				})
		}
		catch(e: com.google.firebase.FirebaseApiNotAvailableException) {
			Log.e(TAG, "Google Play Not Supported:e:-------------------------------------------",e)
			callback(null, e)
		}
	}
	//______________________________________________________________________________________________
	fun fin() {
		auth.removeAuthStateListener(authListener)
	}

	//______________________________________________________________________________________________
	fun addComics(lista: List<Model.Comic>) {
		Log.e(TAG, "addComics: ------------------ INI .................."+lista.size)

		//if(true)return
		if( ! isInit)return
		val batch = db.batch()
		for(comic in lista) {
			val data = HashMap<String, Any>()
			data.put("id", comic.id)
			data.put("title", comic.title)
			data.put("description", comic.description)
			data.put("isbn", comic.isbn)
			data.put("issueNumber", comic.issueNumber)
			data.put("pageCount", comic.pageCount)
			val thumbnail = HashMap<String, Any>()
			thumbnail.put("extension", comic.thumbnail.extension)
			thumbnail.put("path", comic.thumbnail.path)
			data.put("thumbnail", thumbnail)
			data.put("index", comic.index)

			val ref = db.collection(ROOT_COLLECTION).document()
			batch.set(ref, data)

			Log.e(TAG, "addComics: ------------------ LOOP .................."+comic.id)
		}

		batch.commit().addOnCompleteListener({
			task ->
			if(task.isSuccessful) {
				Log.e(TAG, "addComics: ------------------ Write batch succeeded..................")
			}
			else {
				Log.e(TAG, "addComics: ------------------- write batch failed.......................", task.exception)
			}
		})
	}

	//______________________________________________________________________________________________
	fun getComics(callback: (List<Model.Comic>, Throwable?) -> Unit) {
		db.collection(ROOT_COLLECTION)
			.orderBy("index")
			//.orderBy("title")
			//.limit(50)
			//.whereGreaterThan("population", 100000).orderBy("population").limit(2);
			.get()
			.addOnCompleteListener({ task ->
				if(task.isSuccessful && !task.result.isEmpty) {
					val data: MutableList<Model.Comic> = mutableListOf()
					for(document: DocumentSnapshot in task.result) {
						val json = gson.toJsonTree(document.data)
						val comic = gson.fromJson(json, Model.Comic::class.java)
						//val comic = document.toObject(Model.Comic::class.java)
						comic.idFire = document.id
						//Log.e(TAG, "loadComicList:Firebase:-------"+comic.index+" : "+comic.title+" : idFire: "+comic.idFire)
						data.add(comic)
					}
					callback(data, null)
				}
				else {
					Log.e(TAG, "loadComicList:Firebase:e:------------------------ ", task.exception)
					callback(emptyList(), task.exception)
				}
			})
	}

	//______________________________________________________________________________________________
	fun deleteAllComics(callback: () -> Unit) {
		Log.e(TAG, "deleteAllComics:------------------------------------------- ")
		db.collection(ROOT_COLLECTION)
			.get()
			.addOnCompleteListener({ task ->
				Log.e(TAG, "deleteAllComics:-------------------------addOnCompleteListener ")
				if(task.isSuccessful && !task.result.isEmpty) {
					for(document: DocumentSnapshot in task.result) {
						Log.e(TAG, "deleteAllComics:--------------ID: "+document.reference.id)
						document.reference.delete()
					}
				}
				callback()
			})
	}


	//______________________________________________________________________________________________
	companion object {
		val TAG: String = Fire::class.java.simpleName
		val ROOT_COLLECTION = "comics"
	}
}