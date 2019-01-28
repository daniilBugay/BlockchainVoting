package technology.desoft.blockchainvoting.navigation.navigations

import android.view.View
import technology.desoft.blockchainvoting.navigation.Navigation
import technology.desoft.blockchainvoting.presentation.view.MainView
import technology.desoft.blockchainvoting.presentation.view.PollAndAuthor

class CompletedPollDetailsNavigation(
    private val pollAndAuthor: PollAndAuthor,
    private val view: View? = null
): Navigation<MainView> {
    override fun apply(view: MainView) {
        view.showCompletedPollDetails(pollAndAuthor, this.view)
    }
}