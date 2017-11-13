package com.cesoft.cesmarvelous2.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by ccasanova on 08/11/2017
 */
object Util {
	private val TAG: String = Util::class.java.name

	//______________________________________________________________________________________________
	fun md5(stringToHash: String): String {
		val MD5 = "MD5"

		try {
			val digest = MessageDigest.getInstance(MD5)
			digest.update(stringToHash.toByteArray())
			val messageDigest = digest.digest()

			val hexString = StringBuilder()
			for(aMessageDigest in messageDigest) {
				var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
				while(h.length < 2)
					h = "0" + h
				hexString.append(h)
			}
			return hexString.toString()
		}
		catch(e: NoSuchAlgorithmException) {
			Log.e(TAG, "md5:e:-----------------------------------------------------------------",e)
		}

		return ""
	}

}