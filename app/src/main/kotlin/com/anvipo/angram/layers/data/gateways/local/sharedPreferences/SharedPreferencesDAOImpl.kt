package com.anvipo.angram.layers.data.gateways.local.sharedPreferences

import androidx.core.content.edit
import com.anvipo.angram.R
import com.anvipo.angram.layers.core.ResourceManager

class SharedPreferencesDAOImpl(
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

    override fun saveEnabledProxyId(enabledProxyId: Int?) {
        resourceManager.getSharedPreferences().edit(commit = true) {
            putString(resourceManager.getString(R.string.enabled_proxy_id), enabledProxyId?.toString())
        }
    }

}