package com.anvipo.angram.applicationLayer.navigation.router

import com.anvipo.angram.global.ActivityFinishError
import com.anvipo.angram.presentationLayer.common.interfaces.NavigationController
import com.anvipo.angram.presentationLayer.common.interfaces.Presentable
import java.lang.ref.WeakReference

class RouterImp(
    override val rootController: WeakReference<NavigationController>
) : Routable {

    override var presentedViewController: WeakReference<Presentable>? = null

    override fun present(
        viewController: Presentable,
        withNavBar: Boolean,
        completion: (() -> Unit)?
    ) {
        TODO("not implemented")
    }

    override fun push(
        viewController: Presentable,
        hideTabBar: Boolean,
        completion: (() -> Unit)?
    ) {
        if (completion != null) {
            completions[WeakReference(viewController)] = completion
        }

        rootController.get()?.push(
            viewController = viewController,
            hideTabBar = hideTabBar
        ) {
            presentedViewController = WeakReference(viewController)
        }
    }

    override fun popViewController() {
        val rootController = this.rootController.get() ?: return

        val poppedController: Presentable?
        try {
            poppedController = rootController.popViewController()
        } catch (activityFinishError: ActivityFinishError) {
            return
        }

        if (poppedController != null) {
            runCompletionFor(poppedController)
        }

        val topViewController = rootController.topViewController ?: return

        presentedViewController = WeakReference(topViewController)
    }

    override fun popToViewController(viewController: Presentable) {
        TODO("not implemented")
    }

    override fun dismiss(completion: (() -> Unit)?) {
        TODO("not implemented")
    }

    override fun setRootViewController(
        viewController: Presentable,
        hideBar: Boolean
    ) {
        rootController.get()?.isNavigationBarHidden = hideBar

        rootController.get()?.setRootViewController(viewController = viewController) {
            presentedViewController = WeakReference(viewController)
        }
    }

    override fun popToRootViewController() {
        TODO("not implemented")
    }

    override fun pushAsPresent(viewController: Presentable) {
        TODO("not implemented")
    }

    override fun popAsPresent() {
        TODO("not implemented")
    }


    /// PRIVATE


    private val completions: MutableMap<WeakReference<Presentable>, () -> Unit> = mutableMapOf()

    private fun runCompletionFor(controller: Presentable) {
        for ((weakReferenceToController, completion) in completions) {
            val controllerFromCompletions = weakReferenceToController.get() ?: continue

            if (controllerFromCompletions == controller) {
                completion()

                completions.remove(weakReferenceToController)
                return
            } else {
                print("")
            }
        }
    }


}