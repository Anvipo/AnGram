package com.anvipo.angram.layers.data.gateways.local.sharedPreferences

interface SharedPreferencesDAO {

    fun saveEnabledProxyId(enabledProxyId: Int?)
    fun getEnabledProxyId(): Int?

}