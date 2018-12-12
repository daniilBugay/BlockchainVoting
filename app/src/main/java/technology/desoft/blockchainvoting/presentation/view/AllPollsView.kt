package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView

interface AllPollsView: MvpView {
    fun showPolls(polls: List<PollView>)
    fun loading()
    fun showError(message: String)
}