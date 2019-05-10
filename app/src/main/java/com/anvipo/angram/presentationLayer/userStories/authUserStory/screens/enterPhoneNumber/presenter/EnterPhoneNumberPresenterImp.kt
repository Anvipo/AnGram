package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.R
import com.anvipo.angram.businessLogicLayer.gateways.tdLibGateway.errors.TdApiError
import com.anvipo.angram.businessLogicLayer.useCases.enterPhoneNumberUseCase.EnterPhoneNumberUseCase
import com.anvipo.angram.coreLayer.CoreHelpers.assertionFailure
import com.anvipo.angram.coreLayer.CoreHelpers.debugLog
import com.anvipo.angram.coreLayer.ResourceManager
import com.anvipo.angram.coreLayer.base.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.common.baseClasses.BasePresenterImp
import com.anvipo.angram.presentationLayer.userStories.authUserStory.coordinator.interfaces.AuthorizationCoordinatorEnterPhoneNumberOutput
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view.EnterPhoneNumberView
import com.arellomobile.mvp.InjectViewState
import com.github.florent37.runtimepermission.kotlin.PermissionException
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


@InjectViewState
class EnterPhoneNumberPresenterImp(
    override val useCase: EnterPhoneNumberUseCase,
    override val coordinator: AuthorizationCoordinatorEnterPhoneNumberOutput,
    private val resourceManager: ResourceManager
) : BasePresenterImp<EnterPhoneNumberView>(), EnterPhoneNumberPresenter {

    override fun coldStart() {
        viewState.hideNextButton()

        viewState.setMaxLengthOfPhoneNumber(phoneNumberLength.toInt())

        val readPhoneStatePermissionGranted = ActivityCompat.checkSelfPermission(
            resourceManager.context,
            readPhoneStatePermission
        ) == PackageManager.PERMISSION_GRANTED

        if (!readPhoneStatePermissionGranted) {
            viewState.askForReadPhoneStatePermission()
        } else {
            onHasReadPhoneStatePermission()
        }
    }

    override fun onNextButtonPressed(enteredPhoneNumber: String) {
        val onNextButtonPressedCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        viewState.showProgress()

        onNextButtonPressedJob = launch(
            context = coroutineContext + onNextButtonPressedCoroutineExceptionHandler
        ) {
            useCase.setAuthenticationPhoneNumberCatching(enteredPhoneNumber)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                    }

                    coordinator.onEnterCorrectPhoneNumber(enteredPhoneNumber)
                }
                .onFailure { error ->
                    val errorMessage: String = resourceManager.run {
                        when (error) {
                            is TdApiError.Custom.EmptyParameter ->
                                getString(R.string.phone_number_cant_be_empty)
                            is TdApiError.Custom.BadRequest ->
                                getString(R.string.phone_number_invalid)
                            else -> getString(R.string.unknown_error)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        viewState.hideProgress()
                        viewState.showErrorAlert(errorMessage)
                    }
                }
        }
    }

    override fun onPhoneNumberTextChanged(text: CharSequence?) {
        if (text == null) {
            viewState.hideNextButton()
            return
        }

        if (text.length.toUInt() < phoneNumberLength) {
            viewState.hideNextButton()
            return
        }

        viewState.showNextButton()
    }

    override fun onBackPressed() {
        coordinator.onPressedBackButtonInEnterPhoneNumberScreen()
    }

    override fun onCanceledProgressDialog() {
        onNextButtonPressedJob?.cancel()
        viewState.showSnackMessage(resourceManager.getString(R.string.query_canceled))
    }

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun cancelAllJobs() {
        onNextButtonPressedJob?.cancel()
        askPermissionsJob?.cancel()
    }

    override fun onAskForReadPhoneStatePermissionPositiveClick() {
        askReadPhoneStatePermission()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == BaseFragment.fromApplicationSettingsRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                onHasReadPhoneStatePermission()
            }
        }
    }

    private var onNextButtonPressedJob: Job? = null
    private var askPermissionsJob: Job? = null

    private fun askReadPhoneStatePermission() {
        val askPermissionsCoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            if (BuildConfig.DEBUG) {
                val errorText = error.localizedMessage
                debugLog(errorText)
                viewState.showErrorAlert(errorText)
            }
        }

        askPermissionsJob = launch(
            context = coroutineContext + askPermissionsCoroutineExceptionHandler
        ) {
            val currentFragment = this@EnterPhoneNumberPresenterImp.currentFragment

            if (currentFragment == null) {
                assertionFailure("currentFragment == null")
                return@launch
            }

            try {
                val result = currentFragment.askPermission(readPhoneStatePermission)

                //all permissions already granted or just granted
                debugLog(result.accepted.toString())

                onHasReadPhoneStatePermission()
            } catch (permissionException: PermissionException) {
                if (permissionException.hasForeverDenied()) {
                    debugLog("permissionException.hasForeverDenied():")

                    //the list of forever denied permissions, user has check 'never ask again'
                    permissionException.foreverDenied.forEach { permission ->
                        debugLog(permission)
                    }

                    viewState.askForGoToSettingsForReadPhoneStatePermission()
                }

                if (permissionException.hasDenied()) {
                    debugLog("permissionException.hasDenied():")

                    //the list of denied permissions
                    permissionException.denied.forEach { permission ->
                        debugLog(permission)
                    }

                    viewState.askForReadPhoneStatePermission(withNoOption = true)
                }
            }
        }
    }

    private fun onHasReadPhoneStatePermission() {
        debugLog("onHasReadPhoneStatePermission")
    }

    private val readPhoneStatePermission = Manifest.permission.READ_PHONE_STATE
    private val phoneNumberLength: UInt
        get() {
            val telephonyManager =
                resourceManager.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            return when (telephonyManager.simCountryIso) {
                "ru" -> 12u
                // TODO
                else -> 12u
            }
        }

}
