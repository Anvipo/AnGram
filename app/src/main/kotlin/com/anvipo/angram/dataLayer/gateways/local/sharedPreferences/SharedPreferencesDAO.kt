package com.anvipo.angram.dataLayer.gateways.local.sharedPreferences

interface SharedPreferencesDAO {

    fun saveEnabledProxyId(enabledProxyId: Int?)
    fun getEnabledProxyId(): Int?

}