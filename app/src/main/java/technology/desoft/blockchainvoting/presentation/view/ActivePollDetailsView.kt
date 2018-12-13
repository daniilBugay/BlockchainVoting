package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView

interface ActivePollDetailsView: MvpView {
    fun showDetails(pollView: PollView)
}