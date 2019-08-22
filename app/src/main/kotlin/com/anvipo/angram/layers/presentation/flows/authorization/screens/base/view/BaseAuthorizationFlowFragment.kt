package com.anvipo.angram.layers.presentation.flows.authorization.screens.base.view

import androidx.lifecycle.observe
import com.anvipo.angram.layers.core.base.classes.BaseFragment
import com.anvipo.angram.layers.core.events.parameters.EnableViewEventsParameters
import com.anvipo.angram.layers.core.events.parameters.ShowViewEventParameters
import com.anvipo.angram.layers.presentation.flows.authorization.screens.base.viewModel.BaseAuthorizationFlowViewModel

abstract class BaseAuthorizationFlowFragment : BaseFragment() {

    abstract override val viewModel: BaseAuthorizationFlowViewModel

    override fun setupViewModelsObservers() {
        super.setupViewModelsObservers()
        viewModel
            .showNextButtonEvents
            .observe(this) {
                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                when (it) {
                    ShowViewEventParameters.SHOW -> showNextButton()
                    ShowViewEventParameters.HIDE -> hideNextButton()
                }
            }

        viewModel
            .enableNextButtonEvents
            .observe(this) {
                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                when (it) {
                    EnableViewEventsParameters.ENABLE -> enableNextButton()
                    EnableViewEventsParameters.DISABLE -> disableNextButton()
                }
            }
    }

    protected abstract fun showNextButton()
    protected abstract fun hideNextButton()

    protected abstract fun enableNextButton()
    protected abstract fun disableNextButton()

}