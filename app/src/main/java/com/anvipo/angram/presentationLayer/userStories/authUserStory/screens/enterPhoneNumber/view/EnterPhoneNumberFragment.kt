package com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.view


import androidx.appcompat.widget.Toolbar
import com.anvipo.angram.R
import com.anvipo.angram.coreLayer.MessageDialogFragment
import com.anvipo.angram.presentationLayer.common.baseClasses.BaseFragment
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.di.EnterPhoneNumberModule
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenter
import com.anvipo.angram.presentationLayer.userStories.authUserStory.screens.enterPhoneNumber.presenter.EnterPhoneNumberPresenterImp
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import org.koin.android.ext.android.get

class EnterPhoneNumberFragment : BaseFragment(), EnterPhoneNumberView {

    companion object {
        @JvmStatic
        fun createNewInstance(): EnterPhoneNumberView {
            return EnterPhoneNumberFragment()
        }
    }

    override fun showErrorAlert(message: String) {
        // TODO: translate strings

        MessageDialogFragment
            .create(
                message = message,
                title = "Error",
                positive = getString(android.R.string.ok)
            )
            .show(childFragmentManager, null)
    }

    override val presenter: EnterPhoneNumberPresenter by lazy { mPresenter }

    override val layoutRes: Int = R.layout.fragment_enter_phone_number

    override val actionBarTitle: String = "Enter your phone number"
    override val actionBarSubitle: String = ""
    override val actionBar: Toolbar by lazy { toolbar }

    override fun setupClickListeners() {
        enter_phone_number_next_button.setOnClickListener {
            val enteredPhoneNumber = enter_phone_number_edit_text.text.toString()

            presenter.onNextButtonPressed(enteredPhoneNumber)
        }
    }

    @Suppress("ProtectedInFinal")
    @ProvidePresenter
    protected fun providePresenter(): EnterPhoneNumberPresenterImp =
        get(EnterPhoneNumberModule.enterPhoneNumberPresenter)

    @InjectPresenter
    internal lateinit var mPresenter: EnterPhoneNumberPresenterImp

}
