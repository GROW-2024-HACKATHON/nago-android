package com.grow.nago.local.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.grow.nago.local.keystore.AndroidKeyStoreUtil
import com.grow.nago.root.NagoApplication

class NagoSharedPreferences(context: Context = NagoApplication.applicationContext()) {

    private var myKeystore: AndroidKeyStoreUtil

    companion object {
        private var keystore: AndroidKeyStoreUtil? = null
        private var nagoSharedPreferences: NagoSharedPreferences? = null

        @Synchronized
        fun getKeyStore(context: Context): AndroidKeyStoreUtil {
            if (keystore == null) {
                keystore = AndroidKeyStoreUtil
                keystore!!.init(context)
            }
            return keystore as AndroidKeyStoreUtil
        }

        fun getNagoSharedPreferences(): NagoSharedPreferences {
            if (nagoSharedPreferences == null) {
                nagoSharedPreferences = NagoSharedPreferences()
            }
            return nagoSharedPreferences!!
        }
    }
    init {
        myKeystore = getKeyStore(context)
    }

    private val name: String = "name"
    private val tel: String = "tedl"
    private val email: String = "email"
    private val prefsFilename = "prefsNago"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)


    var myName: String
        get() = myKeystore.decrypt(prefs.getString(name, "").toString())
        set(value) = prefs.edit().putString(name, myKeystore.encrypt(value)).apply()

    var myTel: String
        get() = myKeystore.decrypt(prefs.getString(tel, "").toString())
        set(value) = prefs.edit().putString(tel, myKeystore.encrypt(value)).apply()

    var myEmail: String
        get() = myKeystore.decrypt(prefs.getString(email, "").toString())
        set(value) = prefs.edit().putString(email, myKeystore.encrypt(value)).apply()

}