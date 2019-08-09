package com.anvipo.angram.dataLayer.gateways.localGateway.sharedPreferences

interface SharedPreferencesDAO {

    fun saveEnabledProxyId(enabledProxyId: Int)
    fun getEnabledProxyId(): Int

}