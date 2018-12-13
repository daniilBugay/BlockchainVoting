package technology.desoft.blockchainvoting.navigation.navigations

import technology.desoft.blockchainvoting.navigation.Navigation
import technology.desoft.blockchainvoting.presentation.view.MainView

class SignInNavigation: Navigation<MainView> {
    override fun apply(view: MainView) {
        view.showSignInScreen()
    }
}