package com.cesoft.cesmarvelous.repo.fire

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

import com.cesoft.cesmarvelous.model.Model
import com.cesoft.cesmarvelous.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
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
						Log.e(TAG, "loadComicList: ---1----------- "+document.id + " => " + document.data)

						val json = gson.toJsonTree(document.data)
						Log.e(TAG, "loadComicList: ----2---------- "+json)

						val comic = gson.fromJson(json, Model.Comic::class.java)
						Log.e(TAG, "loadComicList: ----3---------- "+comic.thumbnail)

						//{title=100th Anniversary Special (2014) #1,
						// description=- Have the X-Men of 2061 achieved Xavier's dream of mutants and humans living in harmony? Or will there always be a need for the X-Men?,
						// thumbnail={extension=jpg, path=http://i.annihil.us/u/prod/marvel/i/mg/2/a0/53bae9abd8e6f},
						// id=49009, issueNumber=1.0, pageCount=32, isbn=}

						//val comic = document.toObject(Model.Comic::class.java)

						/*val id = document.data["id"] as Long
						val title = document.data["title"] as String
						val description = document.data["description"] as String
						var pageCount = document.data["pageCount"] as Long?
						if(pageCount == null)pageCount=0
						//val thumbnail = Model.Imagen(document.data["path"] as String, document.data["extension"] as String)
						val extension = document.data["thumbnail.extension"] as String
						val path = document.data["thumbnail.path"] as String
						val thumbnail = Model.Imagen(path, extension)
						val issueNumber = document.data["issueNumber"] as Double
						val isbn = document.data["isbn"] as String
						val comic = Model.Comic(id, title, description, pageCount, thumbnail, issueNumber, isbn)
*/
						data.add(comic)
						//Log.e(TAG, "loadComicList: -------------- comic = "+comic.title)
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
	companion object {
		val TAG: String = Fire::class.java.simpleName
		val ROOT_COLLECTION = "comics"
	}
}