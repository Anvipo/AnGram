package com.anvipo.angram.presentationLayer.userStories.authUserStory

import com.anvipo.angram.applicationLayer.di.LaunchSystemModule
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module


object AuthUserStoryModule {

    private const val updatesExceptionHandlerFunctionName: String = "EnterPhoneNumberPresenter"
    internal val updatesExceptionHandler: StringQualifier = named(updatesExceptionHandlerFunctionName)


    val module: Module = module {

        single<EnterPhoneNumberPresenter>(updatesExceptionHandler) {
            EnterPhoneNumberPresenterImp(
                useCase = get(),
                onEnteredCorrectPhoneNumberNotifier = get(LaunchSystemModule.enterCorrectPhoneNumberNotifier),
                onBackButtonPressedNotifier = get(LaunchSystemModule.backButtonPressedInPhoneNumberScreen)
            )
        }

    }

}