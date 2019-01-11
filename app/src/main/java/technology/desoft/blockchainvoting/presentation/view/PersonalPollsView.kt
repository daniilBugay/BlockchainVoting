package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView

interface PersonalPollsView: MvpView {
    fun showError(message: String)
    fun showPersonalPolls(personalPolls: MutableList<PollAndAuthor>)
}