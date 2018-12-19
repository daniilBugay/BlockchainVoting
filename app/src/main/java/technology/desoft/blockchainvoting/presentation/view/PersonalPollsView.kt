package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView

interface PersonalPollsView: MvpView {
    fun loading()
    fun showError(message: String)
    fun showPersonalPolls(personalPolls: List<PollView>)
    fun deletePersonalPoll(id: Long)
}