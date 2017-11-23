package com.cesoft.cesmarvelous.repo.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import com.cesoft.cesmarvelous.model.Model

/**
 * Created by ccasanova on 21/11/2017
 */
@Dao
interface ComicDao {

	@Query("SELECT * FROM repos")
	fun loadAllRepos(): Flowable<List<Model.Comic>>

	@Query("SELECT * FROM repos WHERE organization = :organization")
	fun loadAllRepos(organization: String?): Flowable<List<Model.Comic>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(products: MutableList<Model.Comic>) : Unit

}