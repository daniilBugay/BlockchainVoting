package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView

interface CompletedPollView: MvpView {
    fun showDetails(pollAndAuthor: PollAndAuthor)
    fun showPollResult(options: List<OptionResult>)
}