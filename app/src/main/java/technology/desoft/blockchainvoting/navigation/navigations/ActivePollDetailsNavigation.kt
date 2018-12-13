package technology.desoft.blockchainvoting.navigation.navigations

import android.view.View
import technology.desoft.blockchainvoting.navigation.Navigation
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.PollView

class ActivePollDetailsNavigation(private val poll: PollView, private val view: View): Navigation<MainView> {
    override fun apply(view: MainView) {
        view.showActivePollDetails(poll, this.view)
    }
}