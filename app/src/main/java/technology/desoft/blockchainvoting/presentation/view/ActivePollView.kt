package technology.desoft.blockchainvoting.presentation.view

import com.arellomobile.mvp.MvpView
import technology.desoft.blockchainvoting.model.network.polls.PollOption

interface ActivePollView: MvpView {
    fun showDetails(pollAndAuthor: PollAndAuthor)
    fun showOptions(options: List<PollOption>)
    fun setSelectedOption(position: Int)
    fun lockButton()
    fun unlockButton()
    fun lockOptions()
    fun showError(message: String)
}