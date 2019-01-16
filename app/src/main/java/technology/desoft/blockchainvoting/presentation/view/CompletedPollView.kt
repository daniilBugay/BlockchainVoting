package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView
import technology.desoft.blockchainvoting.model.network.polls.PollOption

interface CompletedPollView: MvpView {
    fun showDetails(pollAndAuthor: PollAndAuthor)
    fun showPollResult(options: List<PollOption>)
}