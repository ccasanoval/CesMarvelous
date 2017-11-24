package com.cesoft.cesmarvelous.repo.fire

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson


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

		/*authListener = FirebaseAuth.AuthStateListener {
			firebaseAuth ->
			val user = firebaseAuth.currentUser
			if(user != null)
			{
				callback(user, null)
				Log.e(TAG, "onAuthStateChanged:signed_in:" + user.uid)
			}
			else
			{
				Log.e(TAG, "onAuthStateChanged:signed_out")
			}
		}
		auth.addAuthStateListener(authListener)*/

		//---------------
		/*auth.signInWithEmailAndPassword(FIRE_EMAIL, FIRE_PASS)
			.addOnCompleteListener({
				task ->
				if(task.isSuccessful)
				{
					Log.e(TAG, "signInWithEmailAndPassword: OK")
				}
				else
				{
					Log.e(TAG, "signInWithEmailAndPassword: KK")
				}
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
		//if(true)return
		if( ! isInit)return
		//for(comic in lista)addComic(comic)
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

			val ref = db.collection(ROOT_COLLECTION).document()
			batch.set(ref, data)
		}

		batch.commit().addOnCompleteListener(OnCompleteListener<Void> {
			task ->
			if(task.isSuccessful) {
				Log.e(TAG, "addComics: ------------------ Write batch succeeded.")
			}
			else {
				Log.e(TAG, "addComics: ------------------- write batch failed.", task.exception)
			}
		})
	}
	//______________________________________________________________________________________________
	/*fun addComic(comic: Model.Comic) {
		// Create a new user with a first and last name
		Log.e(TAG, "addComic:---------------------------- comic : "+comic.title)

		val hash = HashMap<String, Any>()
		hash.put("id", comic.id)
		hash.put("title", comic.title)
		hash.put("description", comic.description)
		hash.put("isbn", comic.isbn)
		hash.put("issueNumber", comic.issueNumber)
		hash.put("pageCount", comic.pageCount)
		hash.put("thumbnail_ext", comic.thumbnail.extension)
		hash.put("thumbnail_path", comic.thumbnail.path)

		// Add a new document with a generated ID
		db.collection(ROOT_COLLECTION)
			.add(hash)
			.addOnSuccessListener {
				documentReference ->
					Log.e(TAG, "addComic:--------------DocumentSnapshot added with ID: " + documentReference.id) }
			.addOnFailureListener {
				e ->
					Log.e(TAG, "addComic:---------------------Error adding document", e) }
	}*/

	//______________________________________________________________________________________________
	fun getComics(callback: (List<Model.Comic>, Throwable?) -> Unit) {
		db.collection(ROOT_COLLECTION)
			.get()
			.addOnCompleteListener(OnCompleteListener { task ->
				//TODO : to fire
				if(task.isSuccessful && !task.result.isEmpty) {
					val data: MutableList<Model.Comic> = arrayListOf()
					for(document: DocumentSnapshot in task.result) {
						val json = gson.toJsonTree(document.data)
						val comic = gson.fromJson(json, Model.Comic::class.java)
						//val comic = document.toObject(Model.Comic::class.java)
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
	fun deleteComics() {
		db.collection(ROOT_COLLECTION).document().delete()
	}

	//______________________________________________________________________________________________
	companion object {
		val TAG: String = Fire::class.java.simpleName
		val ROOT_COLLECTION = "comics"
	}
}