package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView

interface AllPollsView: MvpView {
    fun showPolls(polls: List<PollAndAuthor>)
    fun showError(message: String)
}