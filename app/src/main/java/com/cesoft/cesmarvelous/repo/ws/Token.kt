package com.cesoft.cesmarvelous.repo.ws

import com.cesoft.cesmarvelous.BuildConfig
import com.cesoft.cesmarvelous.util.Util

/**
 * Created by ccasanova on 13/11/2017
 */
class Token
{
	val public = BuildConfig.PUBLIC_API_KEY
	val serial = java.util.Date().time.toString()
	val hash = Util.md5(serial + BuildConfig.PRIVATE_API_KEY + BuildConfig.PUBLIC_API_KEY)
}