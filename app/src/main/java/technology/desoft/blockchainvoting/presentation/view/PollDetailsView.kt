package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView

interface PollDetailsView: MvpView {
    fun showDetails(pollView: PollView)
}