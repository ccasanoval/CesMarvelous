package com.cesoft.cesmarvelous.util

import com.cesoft.cesmarvelous.BuildConfig

/**
 * Created by ccasanova on 08/11/2017
 */
object Log {
	fun e(tag: String, msg: String, e: Throwable? = null) {
		if(BuildConfig.DEBUG)
			android.util.Log.e(tag, msg, e)
	}
}
