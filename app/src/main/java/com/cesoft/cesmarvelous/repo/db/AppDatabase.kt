package com.cesoft.cesmarvelous.repo.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.cesoft.cesmarvelous.model.Model

/**
 * Created by ccasanova on 21/11/2017
 */
@Database(entities = arrayOf(Model.Comic::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

	abstract fun comicDao(): ComicDao

	companion object {
		const val DATABASE_NAME = "cesmarvelous"
	}

}