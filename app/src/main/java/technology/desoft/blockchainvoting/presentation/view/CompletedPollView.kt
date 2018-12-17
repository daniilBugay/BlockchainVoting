package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView

interface CompletedPollView: MvpView {
    fun showDetails(pollView: PollView)
    fun showPollResult(options: List<OptionResult>)
}