package com.anvipo.angram.dataLayer.gateways.local.sharedPreferences

import android.annotation.SuppressLint
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.ResourceManager

class SharedPreferencesDAOImp(
    private val resourceManager: ResourceManager
) : SharedPreferencesDAO {

    override fun getEnabledProxyId(): Int? =
        resourceManager
            .getSharedPreferences()
            .getString(
                resourceManager.getString(R.string.enabled_proxy_id),
                null
            )
            ?.toIntOrNull()

    @SuppressLint("ApplySharedPref")
    override fun saveEnabledProxyId(enabledProxyId: Int?) {
        val sharedPreferences = resourceManager.getSharedPreferences()

        with(sharedPreferences.edit()) {
            putString(resourceManager.getString(R.string.enabled_proxy_id), enabledProxyId?.toString())
            commit()
        }
    }

}