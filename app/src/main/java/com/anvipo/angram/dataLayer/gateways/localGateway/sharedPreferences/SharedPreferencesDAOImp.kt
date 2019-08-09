package com.anvipo.angram.dataLayer.gateways.localGateway.sharedPreferences

import android.annotation.SuppressLint
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.ResourceManager

class SharedPreferencesDAOImp(
    private val resourceManager: ResourceManager
) : SharedPreferencesDAO {

    override fun getEnabledProxyId(): Int =
        resourceManager
            .getSharedPreferences()
            .getInt(
                resourceManager.getString(R.string.enabled_proxy_id),
                -1
            )

    @SuppressLint("ApplySharedPref")
    override fun saveEnabledProxyId(enabledProxyId: Int) {
        val sharedPreferences = resourceManager.getSharedPreferences()

        with(sharedPreferences.edit()) {
            putInt(resourceManager.getString(R.string.enabled_proxy_id), enabledProxyId)
            commit()
        }
    }

}