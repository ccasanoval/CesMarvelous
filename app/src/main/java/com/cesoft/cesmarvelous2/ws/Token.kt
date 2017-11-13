package com.cesoft.cesmarvelous2.ws

import com.cesoft.cesmarvelous2.BuildConfig
import com.cesoft.cesmarvelous2.util.Util

/**
 * Created by ccasanova on 13/11/2017
 */
class Token//val timeCounter: String, val hash: String)
{
	val public = BuildConfig.PUBLIC_API_KEY
	val serial = java.util.Date().time.toString()
	val hash = Util.md5(serial + BuildConfig.PRIVATE_API_KEY + BuildConfig.PUBLIC_API_KEY)
}